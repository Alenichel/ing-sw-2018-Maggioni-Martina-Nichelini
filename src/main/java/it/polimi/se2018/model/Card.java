package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * This is an abstract class which is extended by ObjectiveCard, WindowPatternCard & ToolCard classes.
 */
abstract class Card implements Serializable {
    protected String name;
}
