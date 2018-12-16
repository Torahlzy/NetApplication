package com.torahli.myapplication.game.demo.manager;

import com.torahli.myapplication.game.base.BasePlayer;
import com.torahli.myapplication.game.base.BaseScean;
import com.torahli.myapplication.game.person.SimplePlayer;
import com.torahli.myapplication.game.demo.scean.SimpleScean;

/**
 * 时间管理器
 */
public class SimpleManager {

    public static interface IView {
        void addsceanRecord(String msg);
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
        player = new SimplePlayer();
        scean = new SimpleScean();
        scean.init();
        player.setScean(scean);
    }

    public void setViewImpl(IView viewImpl) {
        this.viewImpl = viewImpl;
    }

    public void next() {
        //玩家开始
        player.next();
    }

    public void sceanRecord(String str) {
        if (viewImpl != null) {
            viewImpl.addsceanRecord(str);
        }
    }

}
