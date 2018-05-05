package it.polimi.se2018;

public class ServerController {
    private final static ServerController instance = new ServerController(Server.getInstance());

    public static ServerController getInstance() {
        return instance;
    }

    private Server server;
    private String gameName;
    private Player admin;

    private ServerController(Server server) {
        this.server = server;
    }

    public void connectPlayer (Player player) {
        server.addPlayer(player);
    }

    public void disconnectPlayer (Player player) {
        server.removePlayer(player);
    }


    public void createRoom(Room room) {
        server.addRoom(String gameName, Player admin);
    }

    public void deleteRoom(Room room) {
        server.removeRoom(room);
    }

}