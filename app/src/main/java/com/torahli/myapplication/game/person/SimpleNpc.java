package com.torahli.myapplication.game.person;

import com.torahli.myapplication.game.base.BasePerson;

public class SimpleNpc extends BasePerson {
    public SimpleNpc(int id) {
        super(id);
    }

    @Override
    protected int getFullRemainTime() {
        return 10;
    }
}
