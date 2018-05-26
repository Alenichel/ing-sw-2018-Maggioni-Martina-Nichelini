package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;

import java.util.List;

public class RoundHandler {

    private Game gameAssociated;

    private final List<Player> turnList;

    public RoundHandler (Game game){
        this.gameAssociated = game;

        List<Player> toAssign = game.getPlayers();
        for (int i = game.getPlayers().size() ; i >= 0; i++ ) toAssign.add(game.getPlayers().get(i));
        turnList = toAssign;
        System.out.println(turnList);
        }
}


