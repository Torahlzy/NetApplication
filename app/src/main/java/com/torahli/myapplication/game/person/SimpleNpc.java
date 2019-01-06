package com.torahli.myapplication.game.person;

import com.torahli.myapplication.game.person.npc.BaseNPC;

public class SimpleNpc extends BaseNPC {
    public SimpleNpc(int id) {
        super(id);
    }

    @Override
    protected int getFullRemainTime() {
        return 10;
    }
}
