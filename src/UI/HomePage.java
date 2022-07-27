package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;

import java.util.InputMismatchException;
import java.util.concurrent.TimeUnit;

public class HomePage {
    public static Event homePage() {
        int user_option = 0;
        boolean invalid_option = false;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------HomePage--------------------\n" + UI.ANSI_RESET);
            System.out.println("0 - exit");
            System.out.println("1 - my profile");
            System.out.println("2 - search");
            System.out.println("3 - logout\n");

            try {
                if(invalid_option)
                    System.out.println(UI.ANSI_RED + "invalid option given" + UI.ANSI_RESET);

                System.out.print("choose your option : ");

                user_option = Integer.parseInt(UI.scanner.nextLine());

                invalid_option = user_option < 0 || user_option > 3;
            } catch (InputMismatchException | NumberFormatException ex) {
                invalid_option = true;
            }
        } while (invalid_option);

        return new Event(Main.UserRequest.HOME_PAGE, Integer.toString(user_option));
    }
}
