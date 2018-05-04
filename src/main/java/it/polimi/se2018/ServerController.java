package it.polimi.se2018;

public class ServerController {
    private final static ServerController instance = new ServerController();

    public static ServerController getInstance() {
        return instance;
    }

    private ServerController() {
    }

    public void connectPlayer (Player player) {
        instance.connectPlayer(player);
    }

    public void disconnectPlayer (Player player) {
        instance.disconnectPlayer(player);
    }


    public void createRoom(Room room) {
        instance.createRoom(room);
    }

    public void deleteRoom(Room room) {
        instance.deleteRoom(room);
    }

}