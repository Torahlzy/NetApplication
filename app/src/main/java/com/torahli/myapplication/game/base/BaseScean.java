package com.torahli.myapplication.game.base;

public abstract class BaseScean {
    public abstract String getTime();

    public abstract String getSceanName();

    public abstract boolean action(BasePerson person, BaseAction action);

    public abstract void init();
}
