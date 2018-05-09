package it.polimi.se2018;

public class RoomWaitingState implements RoomControllerState{

    @Override
    public void launchGame(RoomController context) {
        Game newGame = new Game(context.room.getListOfConnectedPlayers());
        context.room.setGameAssociated(newGame);
        context.room.setGameControllerAssociated(new GameController(newGame));
        context.setState(new RoomWaitingState());
    }

    @Override
    public void connectPlayer(RoomController context, Player player) {
        context.room.addPlayer(player);
    }

    @Override
    public void disconnectPlayer(RoomController context, Player player) {
        context.room.removePlayer(player);
    }
}