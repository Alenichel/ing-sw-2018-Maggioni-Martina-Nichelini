package it.polimi.se2018.model;

/**
 * This class implements ObjectiveCard which is extended by PrivateObjectiveCard &
 * PublicObjectiveCard classes
 */
public abstract class ObjectiveCard extends Card {
    protected String description;

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
    public abstract int scorePoint(WindowPatternCard windowPatternCard);

    /**
     * To string method
     * @return string
     */
    @Override
    public String toString() {
        return this.getClass().toString();
    }
}