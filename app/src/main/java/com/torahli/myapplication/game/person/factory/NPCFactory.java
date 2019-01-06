package com.torahli.myapplication.game.person.factory;

import android.text.TextUtils;

import com.torahli.myapplication.game.base.BasePerson;
import com.torahli.myapplication.game.person.SimpleNpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class NPCFactory {
    private volatile static NPCFactory instance = new NPCFactory();

    private NPCFactory() {
    }

    private static final String[] xings = new String[]{
            "公孙", "欧阳", "上官",
            "西门", "诸葛", "轩辕",
            "令狐", "司马", "岳",
            "沙"
    };

    private static final String[] mings = new String[]{
            "昭", "春", "珊珊",
            "浩晨", "蓉", "婵",
            "子辰", "川", "强",
    };

    public static NPCFactory getInstance() {
        return instance;
    }

    private List<BasePerson> allNpc = new ArrayList<>();
    private Random random = new Random();
    private static volatile int id = 100;

    /**
     * 产生一个npc
     *
     * @param xing
     * @return
     */
    public BasePerson generateOne(@Nullable String xing) {
        //名字去重
        String ming = null;
        boolean canNext = false;
        do {
            canNext = true;
            ming = mings[random.nextInt(mings.length)];
            if (TextUtils.isEmpty(xing)) {
                xing = xings[random.nextInt(xings.length)];
            }
            for (BasePerson person : allNpc) {
                if (person.getXing().equals(xing) && person.getMing().equals(ming)) {
                    canNext = false;
                    break;
                }
            }
        } while (!canNext);

        SimpleNpc npc = new SimpleNpc(id++);
        npc.setXingMing(xing, ming);
        allNpc.add(npc);
        return npc;
    }

}
