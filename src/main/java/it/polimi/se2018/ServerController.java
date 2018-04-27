package it.polimi.se2018;

public class ServerController {
    private static ServerController instance = new ServerController();

    public static ServerController getInstance() {
        return instance;
    }

    private ServerController() { }

    public void connectPlayer (Player player) {}
    public void disconnectPlayer (Player player) {}
    public void createRoom (Room room) {}
    public void deleteRoom (Room room) {}
}