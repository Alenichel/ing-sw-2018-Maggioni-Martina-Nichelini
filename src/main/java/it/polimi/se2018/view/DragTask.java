package it.polimi.se2018.view;

import it.polimi.se2018.enumeration.LoggerPriority;
import it.polimi.se2018.enumeration.LoggerType;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.*;
import javafx.animation.Timeline;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javafx.scene.image.Image;


public class DragTask extends Task<Void> {

    private final GameWindowController gwc;

    private final Pane target;
    private final Pane responeInsert;
    private final DragEvent event;
    private final GuiView gw;
    private final Semaphore controllerCallbackSemaphore;
    private final Timeline timer;
    private final int die;
    private final List<Die> dice;
    private final List<Pane> dicePanes;

    public DragTask(GameWindowController gwc, Pane target,  DragEvent event, Timeline timer, int die){
        this.gwc = gwc;
        this.target = target;
        this.responeInsert = gwc.responseInsert;
        this.event = event;
        this.gw = gwc.gw;
        this.controllerCallbackSemaphore = gwc.controllerCallbackSemaphore;
        this.timer = timer;
        this.die = die;
        this.dice = gwc.getDraftPoolDice();
        this.dicePanes = gwc.getDraftedDice();
    }

    @Override
    protected Void call() throws Exception {

        Dragboard db = event.getDragboard();
        boolean success = false;



        gw.placeDice(die, Integer.parseInt(target.getId().split("-")[1]), Integer.parseInt(target.getId().split("-")[2]));

        try {
            controllerCallbackSemaphore.tryAcquire(5, TimeUnit.SECONDS);
            if (controllerCallbackSemaphore.availablePermits() == 0) {
                gwc.printNack("INCORRECT INSERTION");
                success = false;
                timer.setCycleCount(Timeline.INDEFINITE);
                timer.play();

                Die d = dice.get(die-1);
                String path = "/dice/"+d.getColor()+"/"+d.getNumber()+".png";
                Pane p = dicePanes.get(die-1);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        BackgroundImage myBI= new BackgroundImage(new Image(path,55,55,false,true),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                        p.setBackground(new Background(myBI));
                    }
                });
            }
            else if (controllerCallbackSemaphore.availablePermits() == 1){
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        BackgroundImage myBI= new BackgroundImage(db.getImage(),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                        target.setBackground(new Background(myBI));
                    }
                });
                //gwc.draggable = false;
                gwc.printAck("CORRECT INSERTION");
                success = true;
                timer.setCycleCount(Timeline.INDEFINITE);
                timer.play();
            }

            controllerCallbackSemaphore.release();
        } catch (Exception e){
            success = false;
            Logger.log(LoggerType.CLIENT_SIDE, LoggerPriority.ERROR, "Network Error");
        }

        event.setDropCompleted(success);
        event.consume();

        return null;
    }
}
