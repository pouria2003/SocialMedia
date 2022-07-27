package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;

public class StartPage {
    public enum StartPageSituation {
        EMPTY,
        INVALID_OPTION
    }

    public static Event startPage(StartPageSituation situation) {
        int option = 0;
        boolean invalid_option = false;
        do {
            System.out.println(UI.ANSI_BLUE + "\n--------------------StartPage--------------------\n" + UI.ANSI_RESET);
            if (situation == StartPageSituation.INVALID_OPTION)
                System.out.println("invalid option given");
            System.out.println("-Welcome to program");
            System.out.println("-press Backspace to go to previous step");
            System.out.println("-press Esc to exit program");
            System.out.println("1- sign up");
            System.out.println("2- sign in");
            System.out.println("3- exit");

            if(invalid_option)
                System.out.println(UI.ANSI_RED + "\ninvalid option given" + UI.ANSI_RESET);
            System.out.print("Enter your option : ");

            try {
                option = Integer.parseInt(UI.scanner.nextLine());
                invalid_option = option < 1 || option > 3;
            }
            catch (NumberFormatException ex) {
                invalid_option = true;
            }
        } while (invalid_option);
        return new Event(Main.UserRequest.START_PAGE, Integer.toString(option));

    }
}
