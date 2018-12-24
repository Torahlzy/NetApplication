package com.torahli.myapplication.game.action;

import com.torahli.myapplication.game.base.BaseAction;
import com.torahli.myapplication.game.base.BasePerson;
import com.torahli.myapplication.game.base.BaseScean;
import com.torahli.myapplication.game.person.bag.BaseBag;

import java.util.Random;

public class WorkAction extends BaseAction {
    public static final int usedTime = 5;
    public BasePerson actionPerson;
    public static final Random random = new Random();
    /**
     * 工作成功
     */
    private String achievementStr;

    @Override
    public String getDescription(BaseScean scean) {
        return actionPerson.getFullName() + "在" + scean.getSceanName() + "工作，获得了" + achievementStr;
    }


    @Override
    public void action() {
        BaseBag bag = actionPerson.getBag();
        int moneyAchievement = getMoneyAchievement();
        bag.money += moneyAchievement;
        achievementStr = "金钱" + moneyAchievement;
    }

    protected int getMoneyAchievement() {
        return 100 + random.nextInt(100);
    }
}
