package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;
import BusinessLogic.Post.Post;
import BusinessLogic.User.User;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class MyProfile {

    public static Event myProfile(User user, Profile.ProfileSituation situation) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------MyProfile--------------------\n" + UI.ANSI_RESET);

            System.out.print(UI.ANSI_PURPLE);
            System.out.println("                " + user.getUsername());
            System.out.println("   posts : " + user.getNumberOfPost() + "   followers : " +
                    user.getNumberOfFollowers() + "  followings : " + user.getNumberOfFollowings());
            System.out.println(UI.ANSI_RESET);

            if(situation == Profile.ProfileSituation.DATABASE_EXCEPTION) {
                System.out.println(UI.ANSI_RED + "we have some problem with connecting to database\n" +
                        "please try later" + UI.ANSI_RESET);
            }

            System.out.println("0 - back");
            System.out.println("1 - posts");
            System.out.println("2 - followers");
            System.out.println("3 - followings");
            System.out.println("4 - new post");
            System.out.println("5 - send message");

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.print("\nenter your option : ");
            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());

                invalid_option = user_option < 0 || user_option > 5;

            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.MY_PROFILE, Integer.toString(user_option));
    }

    public static Event myFollowersList(ArrayList<String> followers) {
        int user_option = 0;
        boolean invalid_option = false;

        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Followers--------------------\n" + UI.ANSI_RESET);

            for (int i = 0; i < followers.size(); ++i)
                System.out.println((i + 1) + " - " + followers.get(i));

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.println("\n-enter follower number to remove the follower");
            System.out.println("-enter 0 to go back\n");

            System.out.print("Enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > followers.size();
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.MY_FOLLOWERS_LIST, (user_option == 0) ? "0" : followers.get(user_option - 1));
    }

    public static Event myFollowingsList(ArrayList<String> followings) {
        int user_option = 0;
        boolean invalid_option = false;

        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Followings--------------------\n" + UI.ANSI_RESET);

            for (int i = 0; i < followings.size(); ++i)
                System.out.println((i + 1) + " - " + followings.get(i));

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.println("\n-enter following number to unfollow");
            System.out.println("-enter 0 to go back\n");

            System.out.print("Enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > followings.size();
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.MY_FOLLOWINGS_LIST, (user_option == 0) ? "0" : followings.get(user_option - 1));
    }

    public static Event newPost(boolean length_exception) {
        System.out.println(UI.ANSI_BLUE + "\n--------------------New Post--------------------\n" + UI.ANSI_RESET);

        if(length_exception)
            System.out.println(UI.ANSI_RED + "content length must contain at least 1 character " +
                    "and at most 250 character" + UI.ANSI_RESET);

        System.out.print("enter post content : ");
        String content = UI.scanner.nextLine();

        return new Event(Main.UserRequest.NEW_POST, content);

    }

    public static Event myPosts(ArrayList<Post> posts) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Posts--------------------\n" + UI.ANSI_RESET);

            for (int i = 0; i < posts.size(); ++i) {

                System.out.println(UI.ANSI_CYAN + (i + 1) + " - " + posts.get(i).getUsername()
                        + " : " + posts.get(i).getContent().substring(0,
                        (Math.min(posts.get(i).getContent().length(), 30))) + "..."
                        + UI.ANSI_YELLOW + "     " + posts.get(i).getLikes() + " like   "
                        + posts.get(i).getNumberOfReplies() + " replies" + UI.ANSI_RESET);

            }
            System.out.print(UI.ANSI_RESET);

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "\ninvalid option given" + UI.ANSI_RESET);

            System.out.println("\n-enter number of post to see post");
            System.out.println("-enter 0 to go back");
            System.out.print("enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > posts.size();
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        /// return "0" if user option iz 0 otherwise return post id
        return new Event(Main.UserRequest.MY_POSTS, (user_option == 0) ? "0" : posts.get(user_option - 1).getId());

    }

    public static Event myPost(Post post) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Post--------------------\n" + UI.ANSI_RESET);

            System.out.println(UI.ANSI_CYAN + post.getUsername() + " : " + post.getContent());
            System.out.println(UI.ANSI_RED + post.getLikes() + " like  "
                    + post.getNumberOfReplies() + " replies" + UI.ANSI_RESET);

            System.out.println("0 - back");
            System.out.println("1 - like");
            System.out.println("2 - delete");
            System.out.println("3 - reply");
            System.out.println("4 - replies");

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.print("enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > 4;
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.MY_POST, Integer.toString(user_option), post.getId());
    }
}
