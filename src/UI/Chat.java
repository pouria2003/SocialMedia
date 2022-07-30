package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;
import BusinessLogic.Message.Message;

import java.util.ArrayList;

public class Chat {
    public static Event chats(ArrayList<String> chats, String username) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Chats--------------------\n" + UI.ANSI_RESET);

            System.out.println("0 - exit");
            System.out.println("1 - search");
            System.out.println("2 - new chat\n");

            String[] usernames;

            for(int i = 0; i < chats.size(); ++i) {
                usernames = chats.get(i).split(" ");
                System.out.println(i + 3 + " - "
                        + ((usernames[0].equals(username)) ? usernames[1] : usernames[0]));
            }

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.print("enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > chats.size() + 2;
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.CHATS, ((user_option < 3) ? Integer.toString(user_option) : chats.get(user_option - 3)));
    }

    // searched user can be null if searched_chat_not_exists is false
    public static Event searchChat(boolean searched_chat_not_exists, String searched_user) {
        UI.clearScreen();
        System.out.println(UI.ANSI_BLUE + "\n--------------------SearchChat--------------------\n" + UI.ANSI_RESET);

        if(searched_chat_not_exists) {
            System.out.println(UI.ANSI_RED + "you have not started chat with "
                    + searched_user + " yet\n" + UI.ANSI_RESET);
        }

        System.out.println("-enter 0 to back");
        System.out.print("username : ");
        String username = UI.scanner.nextLine();
        return new Event(Main.UserRequest.SEARCH_CHAT, username);
    }

    public static Event messages(ArrayList<Message> messages) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------Messages--------------------\n" + UI.ANSI_RESET);

            for (int i = 0; i < messages.size(); ++i) {
                if(messages.get(i).getRepliedTo() == -1) {
                    System.out.println(messages.get(i).getUsername() + " : "
                            + messages.get(i).getMessage());
                }
                else {
                    try {
                        System.out.println(messages.get(i).getUsername() + " : "
                                + messages.get(i).getMessage() +
                                "  ( replying to : " + Main.getMessage(messages.get(i).getRepliedTo()).getMessage() + " )");
                    } catch (NullPointerException ex) {
                        System.out.println(messages.get(i).getUsername() + " : "
                                + messages.get(i).getMessage());
                    }
                }
            }

            System.out.println("\n0 - back");
            System.out.println("1 - new message");
            System.out.println("2 - select\n");

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.print("enter your option : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > 2;
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);
        return  new Event(Main.UserRequest.MESSAGES, Integer.toString(user_option));
    }

    public static Event newMessage() {
        System.out.println(UI.ANSI_BLUE + "\n--------------------NewMessage--------------------\n" + UI.ANSI_RESET);

        System.out.print("message : ");
        String message = UI.scanner.nextLine();
        return new Event(Main.UserRequest.NEW_MESSAGE, message);
    }

    // searched_username can be null if user_not_exists is false
    public static Event newChat(boolean user_not_exists, String searched_username) {
        UI.clearScreen();
        System.out.println(UI.ANSI_BLUE + "\n--------------------NewChat--------------------\n" + UI.ANSI_RESET);

        if(user_not_exists)
            System.out.println(UI.ANSI_RED + searched_username + " does not exist" + UI.ANSI_RESET);

        System.out.println("-enter 0 to back");
        System.out.print("enter username you want to start chat with : ");
        String username = UI.scanner.nextLine();

        return new Event(Main.UserRequest.NEW_CHAT, username);
    }

    public static Event selectMessage(ArrayList<Message> messages) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------SelectMessage--------------------\n" + UI.ANSI_RESET);

            for(int i = 0; i < messages.size(); ++i) {
                System.out.println((i + 1) + " ) " + messages.get(i).getUsername()
                        + " : " + messages.get(i).getMessage());
            }

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.println("- enter 0 to back");
            System.out.print("enter number of message : ");

            try {
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 || user_option > messages.size();
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.SELECT_MESSAGE, ((user_option == 0) ? "0"
                : Integer.toString(messages.get(user_option - 1).getId())));
    }

    public static Event selectedMessage(Message message) {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------SelectMessage--------------------\n" + UI.ANSI_RESET);

            System.out.println(message.getUsername() + " : " + message.getMessage());

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

            System.out.println("0 - back");
            System.out.println("1 - reply");

            try{
                user_option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = user_option < 0 | user_option > 1;
            } catch (NumberFormatException ex) {
                invalid_option = true;
            }

        } while(invalid_option);

        return new Event(Main.UserRequest.SELECTED_MESSAGE, Integer.toString(user_option)
                , Integer.toString(message.getId()), message.getMessage());

    }

    public static Event replyMessage(String message_replying_to, String id_replying_to) {
        System.out.println(UI.ANSI_BLUE + "\n--------------------SelectMessage--------------------\n" + UI.ANSI_RESET);
        System.out.println("replying to " + message_replying_to + "\n");

        System.out.print("message : ");
        String message = UI.scanner.nextLine();
        return new Event(Main.UserRequest.REPLY_MESSAGE, message, id_replying_to);
    }
}
