package it.polimi.se2018.model;

/**
 * This class implements ObjectiveCard which is extended by PrivateObjectiveCard &
 * PublicObjectiveCard classes
 */
public class ObjectiveCard extends Card {
    protected String description;
    private Game gameReference;

    /**
     * Objective card constructor
     */
    public ObjectiveCard (){
        this.gameReference = null;
    }

    /**
     * Description getter
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Score point's method
     */
    public int scorePoint(){
        return 0;
    }

    /**
     * To string method
     * @return string
     */
    @Override
    public String toString() {
        return this.getClass().toString();
    }
}
