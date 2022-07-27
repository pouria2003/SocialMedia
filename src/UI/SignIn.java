package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;

import java.util.concurrent.TimeUnit;

public class SignIn {
    public enum SignInSituation {
        EMPTY,
        USERNAME_NOT_FOUND,
        WRONG_PASSWORD,
        DATA_BASE
    }

    public static Event signIn(SignInSituation situation) {
        UI.clearScreen();
        System.out.println(UI.ANSI_BLUE + "\n--------------------SignIn--------------------\n" + UI.ANSI_RESET);

        System.out.print(UI.ANSI_RED);
        switch (situation) {
            case USERNAME_NOT_FOUND:
                System.out.println("username does not exists\n");
                break;
            case WRONG_PASSWORD:
                System.out.println("wrong password\n");
                break;
            case DATA_BASE:
                System.out.println("oops! we have some problem in connection :(\n" +
                        "please try again\n");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    System.out.print("something went wrong\nplease try later\n");
                    System.exit(0);
                }
                UI.clearScreen();
                break;
        }
        System.out.println(UI.ANSI_RESET);

        return getInfo();
    }

    private static Event getInfo() {
        String username;
        String password;
        System.out.print("username : ");
        username = UI.scanner.nextLine();
        System.out.print("password : ");
        password = UI.scanner.nextLine();

        return new Event(Main.UserRequest.SIGN_IN, "username="+username, "password="+password);
    }

}
