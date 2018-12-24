package com.torahli.myapplication.game.action;

import com.torahli.myapplication.game.base.BaseAction;
import com.torahli.myapplication.game.base.BasePerson;
import com.torahli.myapplication.game.base.BaseScean;

/**
 * 交友动作
 */
public class MakeFriendsAction extends BaseAction {

    public static final int usedTime = 10;
    public BasePerson targetPerson;

    public BasePerson actionPerson;

    @Override
    public String getDescription(BaseScean scean) {
        return actionPerson.getFullName() + "在" + scean.getSceanName() + "和" + targetPerson.getFullName() + "结为好友";
    }

    @Override
    public void action() {
        targetPerson.addFriend(actionPerson);
        actionPerson.addFriend(targetPerson);
    }

    /**
     * 判断是否能结交好友
     *
     * @param action
     * @param target
     * @return
     */
    public static boolean canMakeFriends(BasePerson action, BasePerson target) {
        return !action.isFriend(target);
    }
}
