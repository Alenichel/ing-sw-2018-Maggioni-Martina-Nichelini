package it.polimi.se2018;

import it.polimi.se2018.controller.ServerController;
import it.polimi.se2018.message.ConnectionMessage;
import it.polimi.se2018.message.RequestMessage;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Server;
import it.polimi.se2018.view.CliView;
import org.junit.Assert;
import org.junit.Test;

import java.util.Observable;

public class ServerControllerTest {

    @Test
    public void testServerController(){
        Server server = Server.getInstance();
        ServerController serverController = ServerController.getInstance();
    }
}
