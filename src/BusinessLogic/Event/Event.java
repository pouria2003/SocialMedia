package BusinessLogic.Event;

import BusinessLogic.Main.Main;

public class Event {

    public Event(Main.UserRequest req, String... data) {
        this.user_request = req;
        this.data = data;
    }

    public String[] data;

    public Main.UserRequest user_request;
}
