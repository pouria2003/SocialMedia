package UI;

import BusinessLogic.Main.Main;

import java.util.Scanner;

public class UI {
    static final Scanner scanner = new Scanner(System.in);
    private static int database_exception_no = 0;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void dataBaseException() {
        if(++database_exception_no == 3) {
            System.out.println(UI.ANSI_RED + "sorry we seems to have a problem" + UI.ANSI_RESET);
            Main.exitProgram(1);
        }
        System.out.println(UI.ANSI_RED + "oops ! there is a problem with connecting to database. please try again" + UI.ANSI_RESET);
    }

}
