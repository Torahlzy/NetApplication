package com.torahli.myapplication.game.action;

import com.torahli.myapplication.game.base.BaseAction;
import com.torahli.myapplication.game.base.BasePerson;

/**
 * 交友动作
 */
public class MakeFriendsAction extends BaseAction {
    public BasePerson targetPerson;

    public BasePerson actionPerson;

    @Override
    public String getSceanString() {
        return "和" + targetPerson.getFullName() + "结为好友";
    }

    /**
     * 判断是否能结交好友
     *
     * @param action
     * @param target
     * @return
     */
    public static final boolean canMakeFriends(BasePerson action, BasePerson target) {
        return true;
    }
}
