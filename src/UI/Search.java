package UI;

import BusinessLogic.Event.Event;
import BusinessLogic.Main.Main;

public class Search {

    public enum SearchSituation {
        Normal, UsernameNotExist, DataBaseProblem
    }

    public static Event search(SearchSituation situation , String _username) {
        UI.clearScreen();
        System.out.println(UI.ANSI_BLUE + "\n--------------------Search--------------------\n" + UI.ANSI_RESET);
        System.out.println("-enter 0 to go back");

        if(situation == SearchSituation.UsernameNotExist)
            System.out.println(UI.ANSI_RED + _username + " does not exist" + UI.ANSI_RESET);
        else if(situation == SearchSituation.DataBaseProblem)
            System.out.println(UI.ANSI_RED + "oops! we have some problem in connection :(\n" +
                    "please try again\n" + UI.ANSI_RESET);

        System.out.print("username : ");
        String username = UI.scanner.nextLine();
        return new Event(Main.UserRequest.SEARCH, username);
    }
}
