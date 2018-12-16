package com.torahli.myapplication.game.person;

import com.torahli.myapplication.game.action.MakeFriendsAction;
import com.torahli.myapplication.game.base.BasePlayer;
import com.torahli.myapplication.game.base.BaseScean;


public class SimplePlayer extends BasePlayer {

    public SimplePlayer() {
        setXingMing("太吾", "春");
    }

    @Override
    public void next() {
        super.next();
        //不可胜在我，可胜在敌
        if (canMakeFriendAction()) {
            BaseScean scean = getScean();
            MakeFriendsAction makeFriendsAction = new MakeFriendsAction();
            makeFriendsAction.actionPerson = this;
            scean.action(this, makeFriendsAction);
        }

    }

    @Override
    protected boolean canMakeFriendAction() {
        return true;
    }
}
