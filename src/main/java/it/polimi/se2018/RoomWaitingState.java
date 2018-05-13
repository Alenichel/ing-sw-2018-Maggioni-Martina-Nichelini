package it.polimi.se2018;

/**
 * Concrete State for RooController's State Pattern.
 */
public class RoomWaitingState implements RoomControllerState{

    /**
     * This method handles the launch of a new game
     * @param context
     */
    @Override
    public void launchGame(RoomController context) {
        Game newGame = new Game(context.room.getListOfConnectedPlayers());
        context.room.setGameAssociated(newGame);
        context.room.setGameControllerAssociated(new GameController(newGame));
        context.setState(new RoomWaitingState());
    }

    /**
     * Connection handling when the room is waiting.
     * @param context
     * @param player
     */
    @Override
    public void connectPlayer(RoomController context, Player player) {
        context.room.addPlayer(player);
    }

    /**
     * Disconnection handling when the room is waiting.
     * @param context
     * @param player
     */
    @Override
    public void disconnectPlayer(RoomController context, Player player) {
        context.room.removePlayer(player);
    }
}