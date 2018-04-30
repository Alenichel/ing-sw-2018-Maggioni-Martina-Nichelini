package it.polimi.se2018;

public class RoomStartedState implements RoomControllerState{

    @Override
    public void launchGame(RoomController context) {
        throw new RuntimeException("Game already started");
    }

    @Override
    public void connectPlayer(RoomController context, Player player) {
        if (context.room.isADisconnectedClient(player)){
            context.room.addPlayer(player);
        }
        else{
            throw new RuntimeException("Game already started: only previously disconnected players can join the game.");
        }
    }

    @Override
    public void disconnectPlayer(RoomController context, Player player) {
        context.room.addDisconnectedClient(player);
        context.room.removePlayer(player);
    }
}
