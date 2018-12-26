package com.torahli.myapplication.game.person;

import com.torahli.myapplication.game.action.MakeFriendsAction;
import com.torahli.myapplication.game.action.WorkAction;
import com.torahli.myapplication.game.base.BasePlayer;
import com.torahli.myapplication.game.base.BaseScean;


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
    public void next() {
        super.next();
        //不可胜在我，可胜在敌
        if (canMakeFriendAction()) {
            BaseScean scean = getScean();
            MakeFriendsAction makeFriendsAction = new MakeFriendsAction();
            makeFriendsAction.actionPerson = this;
            if (scean.action(this, makeFriendsAction)) {
                remainTime -= MakeFriendsAction.usedTime;
            }
        }
        if (canWorkAction()) {
            WorkAction action = new WorkAction();
            action.actionPerson = this;
            if (getScean().action(this, action)) {
                remainTime -= WorkAction.usedTime;
            }
        }
    }

    @Override
    protected boolean canMakeFriendAction() {
        if (remainTime >= MakeFriendsAction.usedTime) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean canWorkAction() {
        if (remainTime >= WorkAction.usedTime) {
            return true;
        }
        return false;
    }
}
