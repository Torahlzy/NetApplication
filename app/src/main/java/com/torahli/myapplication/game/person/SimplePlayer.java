package com.torahli.myapplication.game.person;

import com.torahli.myapplication.game.base.BasePlayer;
import com.torahli.myapplication.game.demo.manager.event.TimeFlyingEvent;


public class SimplePlayer extends BasePlayer {

    public SimplePlayer() {
        super(1);
        setXingMing("太吾", "春");
    }

    @Override
    protected int getFullRemainTime() {
        return 30;
    }

    @Override
    public void next(TimeFlyingEvent event) {

    }

}
