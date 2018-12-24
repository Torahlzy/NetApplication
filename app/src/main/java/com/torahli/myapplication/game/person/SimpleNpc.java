package com.torahli.myapplication.game.person;

import com.torahli.myapplication.game.base.BasePerson;

public class SimpleNpc extends BasePerson {
    @Override
    protected int getFullRemainTime() {
        return 10;
    }
}
