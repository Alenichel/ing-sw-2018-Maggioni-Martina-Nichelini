package it.polimi.se2018.model;

import java.io.Serializable;

abstract class Card implements Serializable {
    protected String name;
    protected String imagePath;
    protected boolean fieldState;
}
