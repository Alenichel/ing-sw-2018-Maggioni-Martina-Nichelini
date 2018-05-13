package it.polimi.se2018;

/**
 * Concrete State for RooController's State Pattern.
 */
public class RoomStartedState implements RoomControllerState{

    /**
     * This method  has to fail when the game is already started.
     * @param context
     */
    @Override
    public void launchGame(RoomController context) {
        throw new RuntimeException("Game already started");
    }

    /**
     * This method let only previously disconnected client to be reconnected to the game.
     * @param context
     * @param player The player to be added
     */
    @Override
    public void connectPlayer(RoomController context, Player player) {
        if (context.room.isADisconnectedClient(player)){
            context.room.addPlayer(player);
        }
        else{
            throw new RuntimeException("Game already started: only previously disconnected players can join the game.");
        }
    }

    /**
     * This method handles the disconnection process when the game associated with this room is started
     * @param context
     * @param player
     */
    @Override
    public void disconnectPlayer(RoomController context, Player player) {
        context.room.addDisconnectedClient(player);
        context.room.removePlayer(player);
    }
}
