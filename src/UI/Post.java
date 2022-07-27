package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;
import BusinessLogic.Post.Comment;

import java.util.ArrayList;

public class Post {
    public static Event post(BusinessLogic.Post.Post post, boolean is_liked) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Post--------------------\n" + UI.ANSI_RESET);

            System.out.println(UI.ANSI_CYAN + post.getUsername() + " : " + post.getContent());
            System.out.println(UI.ANSI_RED + post.getLikes() + " like  "
                    + post.getNumberOfReplies() + " replies" + UI.ANSI_RESET);

            System.out.println("0 - back");
            System.out.println("1 - " + ((is_liked) ? "unLike" : "like"));
            System.out.println("2 - reply");
            System.out.println("3 - replies");

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.print("enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > 3;
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.POST, Integer.toString(user_option), post.getId());
    }

    public static Event posts(ArrayList<BusinessLogic.Post.Post> posts, String username) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Posts--------------------\n" + UI.ANSI_RESET);

            System.out.println(UI.ANSI_PURPLE + "                " + username + UI.ANSI_RESET);

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
        return new Event(Main.UserRequest.POSTS, (user_option == 0) ? "0" : posts.get(user_option - 1).getId());

    }

    public static Event reply(BusinessLogic.Post.Post post, boolean length_exception) {
        System.out.println(UI.ANSI_BLUE + "\n--------------------Comment--------------------\n" + UI.ANSI_RESET);

        if(length_exception)
            System.out.println(UI.ANSI_RED + "content length must contain at least 1 character " +
                    "and at most 250 character" + UI.ANSI_RESET);

        String comment;

        System.out.print("comment : ");
        comment = UI.scanner.nextLine();

        return new Event(Main.UserRequest.REPLY, comment, post.getId());
    }

    public static Event comments(ArrayList<Comment> comments, BusinessLogic.Post.Post replied_to) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Replies--------------------\n" + UI.ANSI_RESET);

            System.out.println(UI.ANSI_PURPLE + replied_to.getContent() + UI.ANSI_RESET);

            for (int i = 0; i < comments.size(); ++i) {

                System.out.println(UI.ANSI_CYAN + (i + 1) + " - "  + comments.get(i).getUsername() + " : "
                        + comments.get(i).getContent().substring(0, (Math.min(comments.get(i).getContent().length(), 30)))
                        + "..." + UI.ANSI_YELLOW + "     " + comments.get(i).getLikes() + " like   "
                        + comments.get(i).getNumberOfReplies() + " replies" + UI.ANSI_RESET);

            }
            System.out.print(UI.ANSI_RESET);

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "\ninvalid option given" + UI.ANSI_RESET);

            System.out.println("\n-enter number of post to see post");
            System.out.println("-enter 0 to go back");
            System.out.print("enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > comments.size();
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        /// return "0" if user option iz 0 otherwise return post id
        return new Event(Main.UserRequest.COMMENTS, (user_option == 0) ? "0" : comments.get(user_option - 1).getId());
    }
}
