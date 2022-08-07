package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;

import java.util.concurrent.TimeUnit;

public class SignIn {

    public enum SignInSituation {
        EMPTY,
        USERNAME_NOT_FOUND,
        WRONG_PASSWORD,
        WRONG_SECURITY_ANSWER,
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
            case WRONG_SECURITY_ANSWER:
                System.out.println("wrong answers for security questions");
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
        System.out.println("\n- enter 0 if forgot your password");
        System.out.print("password : ");
        password = UI.scanner.nextLine();

        return new Event(Main.UserRequest.SIGN_IN, "username="+username, "password="+password);
    }

    public static Event checkSecurityQuestions() {
        UI.clearScreen();
        System.out.println(UI.ANSI_BLUE + "\n--------------------SecurityCheck--------------------\n" + UI.ANSI_RESET);

        String sec_answer1, sec_answer2, sec_answer3;

        System.out.print("In what city were you born? : ");
        sec_answer1 = UI.scanner.nextLine();
        System.out.print("What is the name of your favorite pet? : ");
        sec_answer2 = UI.scanner.nextLine();
        System.out.print("What is the name of your first school? : ");
        sec_answer3 = UI.scanner.nextLine();

        return new Event(Main.UserRequest.CHECK_SECURITY_QUESTIONS, sec_answer1, sec_answer2, sec_answer3);
    }

    public static Event changePassword(boolean password_format_exception) {


        String password, repeated_password;
        boolean repeated_password_match = true;
        do {
            UI.clearScreen();
            System.out.println(UI.ANSI_BLUE + "\n--------------------ChangePassword--------------------\n" + UI.ANSI_RESET);

            if(password_format_exception)
                System.out.println(UI.ANSI_RED + "password must have at least 6 characters and " +
                        "at most 20 characters\npassword must contains only lowercase letters or" +
                        " only uppercase letters" + UI.ANSI_RESET);

            if(!repeated_password_match)
                System.out.println(UI.ANSI_RED + "repeated password does not match" + UI.ANSI_RESET);

            System.out.print("new password : ");
            password = UI.scanner.nextLine();
            System.out.print("repeat  password : ");
            repeated_password = UI.scanner.nextLine();

            repeated_password_match = password.equals(repeated_password);
        } while(!repeated_password_match);

        return new Event(Main.UserRequest.CHANGE_PASSWORD, password);
    }

}
