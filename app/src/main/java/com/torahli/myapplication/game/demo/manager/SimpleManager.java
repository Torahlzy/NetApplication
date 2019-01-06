package com.torahli.myapplication.game.demo.manager;

import com.torahli.myapplication.game.base.BasePlayer;
import com.torahli.myapplication.game.base.BaseScean;
import com.torahli.myapplication.game.demo.manager.event.MonthFinishEvent;
import com.torahli.myapplication.game.demo.manager.event.TimeFlyingEvent;
import com.torahli.myapplication.game.demo.scean.SimpleScean;
import com.torahli.myapplication.game.person.SimplePlayer;
import com.torahli.myapplication.game.person.bag.BaseBag;

import org.greenrobot.eventbus.EventBus;

/**
 * 时间管理器
 */
public class SimpleManager {

    public static interface IView {
        void addsceanRecord(CharSequence msg);
    }

    private static SimpleManager instance = new SimpleManager();
    private BasePlayer player;
    private BaseScean scean;
    private IView viewImpl;

    public static SimpleManager getInstance() {
        return instance;
    }

    private SimpleManager() {
        init();
    }

    private void init() {
        scean = new SimpleScean();
        scean.init();

        player = new SimplePlayer();
        player.setScean(scean);
        player.init();
    }

    public void setViewImpl(IView viewImpl) {
        this.viewImpl = viewImpl;
    }

    public void next() {
        //处理过月事件
        EventBus.getDefault().post(new TimeFlyingEvent());
        //过月完成
        EventBus.getDefault().post(new MonthFinishEvent());
    }

    public void sceanRecord(String str) {
        if (viewImpl != null) {
            viewImpl.addsceanRecord(str);
        }
    }

    /**
     * 展示人物背包
     */
    public void showPlayerBag() {
        BaseBag bag = player.getBag();
        String bagStr;
        if (bag == null) {
            bagStr = player.getFullName() + "没有背包";
        } else {
            bagStr = player.getFullName() + "的背包：\n" + bag.getDiscription();
        }
        if (viewImpl != null) {
            viewImpl.addsceanRecord(bagStr);
        }
    }

    /**
     * 展示当前场景中的人物
     */
    public void showCurrentSceanPeople() {
        String peopleStr = scean.getSceanName() + " 场景有：\n" + scean.getPeopleListForShow();
        if (viewImpl != null) {
            viewImpl.addsceanRecord(peopleStr);
        }
    }
}
