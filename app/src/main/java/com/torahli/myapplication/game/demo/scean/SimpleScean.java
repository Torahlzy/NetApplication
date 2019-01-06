package com.torahli.myapplication.game.demo.scean;

import com.torahli.myapplication.game.action.MakeFriendsAction;
import com.torahli.myapplication.game.base.BaseAction;
import com.torahli.myapplication.game.base.BasePerson;
import com.torahli.myapplication.game.base.BaseScean;
import com.torahli.myapplication.game.demo.manager.SimpleManager;
import com.torahli.myapplication.game.person.factory.NPCFactory;

import java.util.ArrayList;

public class SimpleScean extends BaseScean {
    private int id = 0;
    private final ArrayList<BasePerson> personArrayList = new ArrayList<>();

    @Override
    public String getTime() {
        return "" + id++ + ". ";
    }

    @Override
    public String getSceanName() {
        return "武当派";
    }

    public ArrayList<BasePerson> getPeople() {
        return personArrayList;
    }

    @Override
    public void init() {
        //初始化npc
        for (int i = 0; i < 5; i++) {
            BasePerson npc = NPCFactory.getInstance().generateOne(null);
            npc.setScean(this);
            npc.init();
            personArrayList.add(npc);
        }
    }

    @Override
    public String getPeopleListForShow() {
        StringBuilder retStr = new StringBuilder();
        if (personArrayList.size() > 0) {
            for (BasePerson person : personArrayList) {
                retStr.append(person.getFullName() + "\n");
            }
            retStr.subSequence(0, retStr.length() - 1);
        }
        return retStr.toString();
    }

    @Override
    public boolean action(BasePerson person, BaseAction action) {
        //person想要执行动作，但是场景有限制
        if (action instanceof MakeFriendsAction) {
            ArrayList<BasePerson> people = getPeople();
            if (people.size() > 0) {
                for (BasePerson target : people) {
                    if (MakeFriendsAction.canMakeFriends(person, target)) {
                        ((MakeFriendsAction) action).targetPerson = target;
                        action.action();
                        String sceanRecord = getTime() + action.getDescription(this);
                        record(sceanRecord);
                        return true;
                    }
                }
            }
        } else {
            //这些是场景没有限制的动作
            action.action();
            String sceanRecord = getTime() + action.getDescription(this);
            record(sceanRecord);
            return true;
        }
        return false;
    }

    private ArrayList<String> records = new ArrayList<>();

    private void record(String msg) {
        records.add(msg);
        SimpleManager.getInstance().sceanRecord(msg);
    }

}
