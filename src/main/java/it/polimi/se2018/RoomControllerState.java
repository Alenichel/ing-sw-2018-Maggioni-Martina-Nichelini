package it.polimi.se2018;

public interface RoomControllerState {
    void launchGame(RoomController context);
    void connectPlayer(RoomController context, Player player);
    void disconnectPlayer(RoomController context, Player player);
}
