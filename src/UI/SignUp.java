package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;

import java.util.concurrent.TimeUnit;

public class SignUp {

    public enum SignUpSituation {
        EMPTY,
        REPEATED_PASSWORD_NOT_MATCH,
        PASSWORD_LENGTH,
        WEAK_PASSWORD,
        PASSWORD_FORMAT,
        USERNAME_LENGTH,
        USERNAME_FORMAT,
        USERNAME_EXISTS,
        SECURITY_ANSWERS_LENGTH,
        DATA_BASE
    }

    public static Event signUp(SignUpSituation situation) {
        UI.clearScreen();
        System.out.println(UI.ANSI_BLUE + "\n--------------------SignUp--------------------\n" + UI.ANSI_RESET);

        System.out.print(UI.ANSI_RED);
        switch (situation) {
            case REPEATED_PASSWORD_NOT_MATCH -> System.out.print("repeated password does not match\nplease enter again\n");
            case PASSWORD_LENGTH -> System.out.print("password must be at least 6 characters and at most 20 characters\n" +
                    "please enter again\n");
            case WEAK_PASSWORD -> System.out.print("password is weak\npassword must not have only lowercase or only uppercase\n" +
                    "please enter again\n");
            case PASSWORD_FORMAT -> System.out.print("password must include only lowercase letter, uppercase letter, digits " +
                    " and @-#-.-* signs\nplease enter again\n");
            case USERNAME_LENGTH -> System.out.print("username must have at least 3 characters and at most 20 characters\n" +
                    "please enter again\n");
            case USERNAME_FORMAT -> System.out.print("username must include only lowercase letter, uppercase letter, " +
                    "digits and under line sign\nusername must starts with a letter\n" +
                    "please enter again\n");
            case USERNAME_EXISTS -> System.out.print("this username is already taken\nchoose another username\n");
            case DATA_BASE -> {
                System.out.print("oops! we have some problem in connection :(\n" +
                        "please try again\n");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    System.out.print("something went wrong\nplease try later");
                    System.exit(0);
                }
                UI.clearScreen();
            }
            case SECURITY_ANSWERS_LENGTH -> System.out.println
                    ("answer of security questions most be at least one character and at most twenty");
        }
        System.out.print(UI.ANSI_RESET);

        return getInfo();
    }

    private static Event getInfo() {
        String user_name;
        String password;
        String repeated_password;
        String sec_answer1;
        String sec_answer2;
        String sec_answer3;

        System.out.print("username : ");
        user_name = UI.scanner.nextLine();
        System.out.print("password : ");
        password = UI.scanner.nextLine();
        System.out.print("repeat password : ");
        repeated_password = UI.scanner.nextLine();

        UI.clearScreen();
        System.out.println("\nhere is three security questions will help you to recover your password later\n");
        System.out.print("In what city were you born? : ");
        sec_answer1 = UI.scanner.nextLine();
        System.out.print("What is the name of your favorite pet? : ");
        sec_answer2 = UI.scanner.nextLine();
        System.out.print("What is the name of your first school? : ");
        sec_answer3 = UI.scanner.nextLine();

        return new Event(Main.UserRequest.SIGN_UP, "username="+user_name,
                "password="+password, "repeatedpassword="+repeated_password,
                "sec_ans1=" + sec_answer1, "sec_ans2=" + sec_answer2, "sec_ans3=" + sec_answer3);
    }

}
