package it.polimi.se2018;

import it.polimi.se2018.controller.RoomController;
import it.polimi.se2018.model.Player;

/**
 * Interface for the implementation of RoomController's state pattern
 */
public interface RoomControllerState {
    void launchGame(RoomController context);
    void connectPlayer(RoomController context, Player player);
    void disconnectPlayer(RoomController context, Player player);
}
