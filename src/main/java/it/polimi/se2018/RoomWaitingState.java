package it.polimi.se2018;

public class RoomWaitingState implements RoomControllerState{

    @Override
    public void launchGame(RoomController context) {
        context.room.setGameAssociated(new Game());
        context.room.setGameControllerAssociated(new GameController());
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