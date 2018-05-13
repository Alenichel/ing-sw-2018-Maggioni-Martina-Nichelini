package it.polimi.se2018;

/**
 * Interface for the implementation of RoomController's state pattern
 */
public interface RoomControllerState {
    void launchGame(RoomController context);
    void connectPlayer(RoomController context, Player player);
    void disconnectPlayer(RoomController context, Player player);
}
