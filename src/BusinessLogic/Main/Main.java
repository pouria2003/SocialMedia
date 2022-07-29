package BusinessLogic.Main;

import BusinessLogic.Event.Event;
import BusinessLogic.Post.Comment;
import BusinessLogic.Post.Post;
import BusinessLogic.User.User;
import DataBase.ReadUser;
import Exceptions.LogicException.*;
import Exceptions.DataBaseExceptions.*;
import UI.MyProfile;
import UI.Profile;
import UI.Search;

import java.sql.SQLException;

interface Response {
    Event perform() throws SQLException;
}

public class Main {

    private static Response response;
    private static User user;
    private static Event event;
    private static User searched_user;
    private static boolean my_post; // false means other users post or comment
    private static String chat_name; // save current chat name

    public enum UserRequest {
        START_PAGE,
        SIGN_UP,
        SIGN_IN,
        HOME_PAGE,
        SEARCH,
        PROFILE,
        FOLLOW_LIST,
        MY_PROFILE,
        MY_FOLLOWERS_LIST,
        MY_FOLLOWINGS_LIST,
        NEW_POST,
        POSTS,
        POST,
        MY_POSTS,
        MY_POST,
        REPLY,
        COMMENTS,
        COMMENT,
        CHATS,
        SEARCH_CHAT,
        MESSAGES,
        NEW_MESSAGE,
        NEW_CHAT,
        SELECT_MESSAGE,
        SELECTED_MESSAGE
    }

    /// main() here function as an event handler
    /// it has a loop - run an ui function and receive an event and
    /// analyze that to detect appropriate response to run in next iteration of loop
    public static void main(String[] args) {

        response = () -> UI.StartPage.startPage(UI.StartPage.StartPageSituation.EMPTY);

        do {
            try {
                event = response.perform();

                switch (event.user_request) {
                    case START_PAGE -> startPage();
                    case SIGN_UP -> signUp();
                    case SIGN_IN -> signIn();
                    case HOME_PAGE -> homePage();
                    case SEARCH -> search();
                    case PROFILE -> profile();
                    case FOLLOW_LIST -> response = () -> UI.Profile.profile(searched_user, DataBase.Follow.doesFollow(
                            user.getUsername(), searched_user.getUsername()), Profile.ProfileSituation.NORMAL);
                    case MY_PROFILE -> myProfile();
                    case MY_FOLLOWERS_LIST -> myFollowersList();
                    case MY_FOLLOWINGS_LIST -> myFollowingsList();
                    case NEW_POST -> newPost();
                    case POSTS -> posts();
                    case POST, COMMENT -> post();
                    case MY_POSTS -> myPosts();
                    case MY_POST -> myPost();
                    case REPLY -> reply();
                    case COMMENTS -> comments();
                    case CHATS -> chats();
                    case SEARCH_CHAT -> searchChat();
                    case MESSAGES -> messages();
                    case NEW_MESSAGE -> newMessage();
                    case NEW_CHAT -> newChat();
                    case SELECT_MESSAGE -> selectMessage();
                }
            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                System.out.println(e.getMessage());
                UI.UI.dataBaseException();
            }

        } while (true);
    }

    private static void startPage() {
        switch (event.data[0]) {
            case "1" -> response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.EMPTY);
            case "2" -> response = () -> UI.SignIn.signIn(UI.SignIn.SignInSituation.EMPTY);
            case "3" -> exitProgram(0);
            default -> response = () -> UI.StartPage.startPage(UI.StartPage.StartPageSituation.INVALID_OPTION);
        }
    }

    private static void signUp() {
        String username = null;
        String password = null;
        String rep_password = null;
        String sec_ans1 = null;
        String sec_ans2 = null;
        String sec_ans3 = null;

        for(String str : event.data) {
            if(str.startsWith("username="))
                username = str.substring(9);
            else if(str.startsWith("password="))
                password = str.substring(9);
            else if(str.startsWith("repeatedpassword="))
                rep_password = str.substring(17);
            else if(str.startsWith("sec_ans1="))
                sec_ans1 = str.substring(9);
            else if(str.startsWith("sec_ans2="))
                sec_ans2 = str.substring(9);
            else if(str.startsWith("sec_ans3="))
                sec_ans3 = str.substring(9);

        }

        if(!password.equals(rep_password)) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.REPEATED_PASSWORD_NOT_MATCH);
            return;
        }

        if(sec_ans1.length() > 20 || sec_ans1.isEmpty() || sec_ans2.length() > 20
            || sec_ans2.isEmpty() || sec_ans3.length() > 20 || sec_ans3.isEmpty()) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.SECURITY_ANSWERS_LENGTH);
            return;
        }

        try {
            user = new User(username, password);
            DataBase.Signup.SignUserUp(user, sec_ans1, sec_ans2, sec_ans3);
            System.out.println(UI.UI.ANSI_GREEN + "sign up successfully" + UI.UI.ANSI_RESET);
            response = () -> UI.HomePage.homePage();
        } catch (PasswordLengthException ex) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.PASSWORD_LENGTH);
        } catch (WeakPasswordException ex) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.WEAK_PASSWORD);
        } catch (PasswordFormatException ex) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.PASSWORD_FORMAT);
        } catch (UsernameLengthException ex) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.USERNAME_LENGTH);
        } catch (UsernameFormatException ex) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.USERNAME_FORMAT);
        } catch (UsernameExistException ex) {
            response = () -> UI.SignUp.signUp(UI.SignUp.SignUpSituation.USERNAME_EXISTS);
        } catch (SQLException ex) {
            UI.SignUp.signUp(UI.SignUp.SignUpSituation.DATA_BASE);
        }
    }

    private static void signIn() {
        String username = null;
        String password = null;
        for(String str : event.data) {
            if (str.startsWith("username="))
                username = str.substring(9);
            else if (str.startsWith("password="))
                password = str.substring(9);
        }
        try {
            user = DataBase.ReadUser.readUser(username, password);
            System.out.println("username = " + user.getUsername() + " , pass = " + user.getPassword());
            response = () -> UI.HomePage.homePage();
        } catch (UsernameNotExistException ex) {
            response = () -> UI.SignIn.signIn(UI.SignIn.SignInSituation.USERNAME_NOT_FOUND);
        } catch (WrongPasswordException ex) {
            response = () -> UI.SignIn.signIn(UI.SignIn.SignInSituation.WRONG_PASSWORD);
        } catch (SQLException ex) {
            response = () -> UI.SignIn.signIn(UI.SignIn.SignInSituation.DATA_BASE);
        }
    }

    private static void homePage() {
        int user_option = Integer.parseInt(event.data[0]);
        switch (user_option) {
            case 0 -> exitProgram(0);
            case 1 -> response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
            case 2 -> response = () -> UI.Search.search(UI.Search.SearchSituation.Normal, null);
            case 3 -> response = () -> UI.StartPage.startPage(UI.StartPage.StartPageSituation.EMPTY);
        }
    }

    private static void search() {
        String username = event.data[0];
        if(username.equals("0")) {
            response = () -> UI.HomePage.homePage();
            return;
        }

        try {
            /// null for password means readUser() will not check if password matches or not
            User other_user = ReadUser.readUser(username, null);

            response = () -> Profile.profile(other_user, DataBase.Follow.doesFollow(
                    user.getUsername(), other_user.getUsername()), Profile.ProfileSituation.NORMAL);
            searched_user = other_user;

        } catch (UsernameNotExistException ex) {
            response = () -> UI.Search.search(UI.Search.SearchSituation.UsernameNotExist, username);
        }  catch (SQLException e) {
            response = () -> UI.Search.search(UI.Search.SearchSituation.DataBaseProblem, null);
        }
    }

    private static void profile() {
        int user_option = Integer.parseInt(event.data[0]);
        if(user_option == 0) {
            response = () -> UI.Search.search(Search.SearchSituation.Normal, null);
        }
        else if(user_option == 1) {
            try {
                if(DataBase.Follow.doesFollow(user.getUsername(), searched_user.getUsername())) {
                    DataBase.Follow.unfollow(user.getUsername(), searched_user.getUsername(),
                            user.getNumberOfFollowings() - 1, searched_user.getNumberOfFollowers() - 1);
                    user.removeFollowing();
                    searched_user.removeFollower();
                }
                else {
                    DataBase.Follow.follow(user.getUsername(), searched_user.getUsername(),
                            user.getNumberOfFollowings() + 1, searched_user.getNumberOfFollowers() + 1);
                    user.addFollowing();
                    searched_user.addFollower();
                }
                response = () -> UI.Profile.profile(searched_user,
                        DataBase.Follow.doesFollow(user.getUsername(), searched_user.getUsername()),
                        Profile.ProfileSituation.NORMAL);
            } catch (SQLException e) {
                response = () -> UI.Profile.profile(searched_user,
                        DataBase.Follow.doesFollow(user.getUsername(), searched_user.getUsername()),
                        Profile.ProfileSituation.DATABASE_EXCEPTION);
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        /// TODO : handle sql exception in get follow list properly

        else if(user_option == 2) {
            response = () -> UI.Post.posts(DataBase.Post.postsList(searched_user.getUsername()), searched_user.getUsername());
        }

        /// not updating response means execute previous response again
        else if(user_option == 3) {
            response = () -> Profile.followersOrFollowings(DataBase.Follow.followersList(searched_user.getUsername()),
                    searched_user.getUsername(), true);
        }
        else if(user_option == 4) {
            response = () -> Profile.followersOrFollowings(DataBase.Follow.followingsList(searched_user.getUsername()),
                    searched_user.getUsername(), false);
        }

    }

    private static void myProfile() {
        int user_option = Integer.parseInt(event.data[0]);
        if(user_option == 0) {
            response = () -> UI.HomePage.homePage();
        }

        /// TODO : handle sql exception in get follow list properly

        else if(user_option == 1) {
            response = () -> UI.MyProfile.myPosts(DataBase.Post.postsList(user.getUsername()));
        }
        else if(user_option == 2) {
            response = () -> MyProfile.myFollowersList(DataBase.Follow.followersList(user.getUsername()));
        }
        else if(user_option == 3) {
            response = () -> MyProfile.myFollowingsList(DataBase.Follow.followingsList(user.getUsername()));
        }
        else if(user_option == 4) {
            response = () -> UI.MyProfile.newPost(false);
        }
        else if(user_option == 5) {
            response = () -> UI.Chat.chats(DataBase.Chat.chats(user.getUsername()), user.getUsername());
        }
    }

    private static void myFollowersList() {
        String user_choice = event.data[0];
        if(user_choice.equals("0")) {
            response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
        }
        else {

            try {
                User other_user = DataBase.ReadUser.readUser(user_choice, null);
                DataBase.Follow.unfollow(user_choice, user.getUsername(),
                        other_user.getNumberOfFollowings() - 1,
                        user.getNumberOfFollowers() - 1);

                user.removeFollower();
                other_user.removeFollowing();

            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                UI.UI.dataBaseException();
            }
        }
    }

    private static void myFollowingsList() {
        String user_choice = event.data[0];
        if(user_choice.equals("0")) {
            response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
        }
        else {
            try {
                User other_user = DataBase.ReadUser.readUser(user_choice, null);
                DataBase.Follow.unfollow(user.getUsername(), user_choice,
                        user.getNumberOfFollowings() - 1,
                        other_user.getNumberOfFollowers() - 1);

                user.removeFollowing();
                other_user.removeFollower();
            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                UI.UI.dataBaseException();
            }
        }
    }

    private static void newPost() {
        String post_id = user.getUsername() + "_" + (user.getLastPostId() + 1);
        try {
            Post post = new Post(event.data[0], post_id, user.getUsername(), 0, 0);
            DataBase.Post.addPost(post, user.getUsername());
            user.addPost();
            response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
        }
        catch (PostLengthException ex) {
            response = () -> UI.MyProfile.newPost(true);
        } catch (SQLException e) {
            /// DeBug
            e.printStackTrace();
            UI.UI.dataBaseException();
        }

    }

    private static void posts() {
        if(event.data[0].equals("0")) {
            response = () -> UI.Profile.profile(searched_user, DataBase.Follow.doesFollow(
                    user.getUsername(), searched_user.getUsername()), Profile.ProfileSituation.NORMAL);
        }
        else {
            response = () -> UI.Post.post(DataBase.Post.post(event.data[0])
                    , DataBase.Post.doesLike(user.getUsername(), event.data[0]));
        }
    }

    private static void post() {
        if(event.data[0].equals("0")) {
            if(my_post)
                response = () -> UI.MyProfile.myPosts(DataBase.Post.postsList(user.getUsername()));
            else
                response = () -> UI.Post.posts(DataBase.Post.postsList(searched_user.getUsername()),
                        searched_user.getUsername());
        }
        else if(event.data[0].equals("1")) {
            try {

                if(DataBase.Post.doesLike(user.getUsername(), event.data[1]))
                    DataBase.Post.unLike(user.getUsername(), event.data[1]);
                else
                    DataBase.Post.like(user.getUsername(), event.data[1]);

                response = () -> UI.Post.post(DataBase.Post.post(event.data[1])
                        , DataBase.Post.doesLike(user.getUsername(), event.data[1]));

            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                UI.UI.dataBaseException();
            }
        }
        else if(event.data[0].equals("2")) {
            response = () -> UI.Post.reply(DataBase.Post.post(event.data[1]), false);
        }
        else if(event.data[0].equals("3")) {
            my_post = false;
            response = () -> UI.Post.comments(DataBase.Post.repliesList(event.data[1])
                    , DataBase.Post.post(event.data[1]));
        }
    }

    private static void reply() {
        try {
            String comment_id = event.data[1] + "_" + (DataBase.Post.post(event.data[1]).getNumberOfReplies() + 1);
            Comment comment = new Comment(event.data[0], comment_id, user.getUsername(), event.data[1], 0, 0);
            DataBase.Post.addComment(comment);
            DataBase.Post.post(event.data[1]).replied();
            response = () -> UI.Post.post(DataBase.Post.post(event.data[1])
                    , DataBase.Post.doesLike(user.getUsername(), event.data[1]));

        } catch (SQLException e) {
            /// DeBug
            e.printStackTrace();
            UI.UI.dataBaseException();
        }
    }

    private static void comments() {
        if(event.data[0].equals("0")) {
            if(my_post)
                response = () -> UI.MyProfile.myPosts(DataBase.Post.postsList(user.getUsername()));
            else
                response = () -> UI.Post.posts(DataBase.Post.postsList(searched_user.getUsername()), searched_user.getUsername());
        }
        else {
            response = () -> UI.Post.post(DataBase.Post.comment(event.data[0])
                    , DataBase.Post.doesLike(user.getUsername(), event.data[0]));
        }
    }

    private static void myPosts() {
        if(event.data[0].equals("0")) {
            response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
        }
        else {
            response = () -> UI.MyProfile.myPost(DataBase.Post.post(event.data[0]));
        }
    }

    private static void myPost() {
        if(event.data[0].equals("0")) {
            response = () -> UI.MyProfile.myPosts(DataBase.Post.postsList(user.getUsername()));
        }
        else if(event.data[0].equals("1")) {
            try {
                DataBase.Post.like(user.getUsername(), event.data[1]);
            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                UI.UI.dataBaseException();
            }
            response = () -> UI.MyProfile.myPost(DataBase.Post.post(event.data[1]));
        }
        else if(event.data[0].equals("2")) {
            try {
                DataBase.Post.deletePost(event.data[1], user.getUsername());
                user.deletePost();
                response = () -> UI.MyProfile.myPosts(DataBase.Post.postsList(user.getUsername()));
            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                UI.UI.dataBaseException();
            }
        }
        else if(event.data[0].equals("3")) {
            my_post = true;
            response = () -> UI.Post.reply(DataBase.Post.post(event.data[1]), false);
        }
        else if (event.data[0].equals("4")) {
            my_post = true;
            response = () -> UI.Post.comments(DataBase.Post.repliesList(event.data[1])
                    , DataBase.Post.post(event.data[1]));
        }
    }

    private static void chats() {
        String user_choice = event.data[0];
        if(user_choice.equals("0")) {
            response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
        } else if(user_choice.equals("1")) {
            response = () -> UI.Chat.searchChat(false, null);
        } else if(user_choice.equals("2")) {
            response = () -> UI.Chat.newChat(false, null);
        } else {
            chat_name = user_choice;
            response = () -> UI.Chat.messages(DataBase.Chat.getMessages(chat_name));
        }
    }

    private static void searchChat() {
        if(event.data[0].equals("0")) {
            response = () -> UI.Chat.chats(DataBase.Chat.chats(user.getUsername()), user.getUsername());
        }
        else {
            try {

                if(DataBase.Chat.isChatExists(user.getUsername() + " " + event.data[0])) {
                    chat_name = user.getUsername() + " " + event.data[0];
                    response = () -> UI.Chat.messages(DataBase.Chat.getMessages(chat_name));
                }
                else if(DataBase.Chat.isChatExists(event.data[0] + " " + user.getUsername())) {
                    chat_name = event.data[0] + " " + user.getUsername();
                    response = () -> UI.Chat.messages(DataBase.Chat.getMessages(chat_name));
                }
                else {
                    response = () -> UI.Chat.searchChat(true, event.data[0]);
                }

            } catch (SQLException e) {
                /// DeBug
                e.printStackTrace();
                UI.UI.dataBaseException();
            }
        }
    }

    private static void messages() {
        if(event.data[0].equals("0")) {
            response = () -> UI.Chat.chats(DataBase.Chat.chats(user.getUsername()), user.getUsername());
        }
        else if(event.data[0].equals("1")) {
            response = () -> UI.Chat.newMessage();
        }
    }

    private static void newMessage() {
        try {
            DataBase.Chat.newMessage(event.data[0], user.getUsername(), -1, chat_name);
            response = () -> UI.Chat.messages(DataBase.Chat.getMessages(chat_name));
        } catch (SQLException e) {
            /// DeBug
            e.printStackTrace();
            UI.UI.dataBaseException();
        }
    }

    private static void newChat() {
        if(event.data[0].equals("0")) {
            response = () -> UI.Chat.chats(DataBase.Chat.chats(user.getUsername()), user.getUsername());
        }
        else {
            try {
                if(DataBase.Chat.isChatExists(user.getUsername() + " " + event.data[0])) {
                    response = () -> UI.Chat.messages(DataBase.Chat.getMessages
                            (user.getUsername() + " " + event.data[0]));
                }
                else if(DataBase.Chat.isChatExists(event.data[0] + " " + user.getUsername())) {
                    response = () -> UI.Chat.messages(DataBase.Chat.getMessages
                            (event.data[0] + " " + user.getUsername()));
                }
                else if(DataBase.Signup.isUsernameExist(event.data[0])) {
                    DataBase.Chat.newChat(user.getUsername(), event.data[0]);
                    chat_name = user.getUsername() + " " + event.data[0];
                    response = () -> UI.Chat.messages(DataBase.Chat.
                            getMessages(user.getUsername() + " " +event.data[0]));
                }
                else {
                    response = () -> UI.Chat.newChat(true, event.data[0]);
                }

            } catch (SQLException e) {
                // DeBug
                e.printStackTrace();
                response = () -> UI.MyProfile.myProfile(user, Profile.ProfileSituation.NORMAL);
                UI.UI.dataBaseException();
            }
        }
    }

    private static void selectMessage() {
        if(event.data[0].equals("0")) {
            response = () -> UI.Chat.messages(DataBase.Chat.getMessages(chat_name));
        }
        else {
            response = () -> UI.Chat.selectedMessage(DataBase.Chat.getMessage(chat_name,
                    Integer.parseInt(event.data[0])));
        }
    }

    private static void selectedMessage() {
        
    }

    public static void exitProgram(int code) {
        System.exit(code);
    }

}