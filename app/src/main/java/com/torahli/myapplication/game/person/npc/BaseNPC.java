package com.torahli.myapplication.game.person.npc;

import com.torahli.myapplication.game.action.MakeFriendsAction;
import com.torahli.myapplication.game.action.WorkAction;
import com.torahli.myapplication.game.base.BasePerson;
import com.torahli.myapplication.game.base.BaseScean;
import com.torahli.myapplication.game.demo.manager.event.TimeFlyingEvent;

/**
 * 定义一些npc模板，子类只需要继承该类，并传递构造参数即可
 * 要与player末班区分，否则移到公共父类
 */
public abstract class BaseNPC extends BasePerson {
    public BaseNPC(int id) {
        super(id);
    }

    /**
     * 执行
     */
    @Override
    public void next(TimeFlyingEvent event) {
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
