package com.torahli.myapplication.game.base;

/**
 * 动作
 */
public abstract class BaseAction {
    public abstract String getDescription(BaseScean scean);

    /**
     * 动作生效
     */
    public abstract void action();
}
