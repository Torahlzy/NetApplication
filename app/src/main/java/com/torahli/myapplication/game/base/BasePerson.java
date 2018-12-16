package com.torahli.myapplication.game.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BasePerson {
    private List<BasePerson> friends = new ArrayList<>();
    private BaseScean mScean;
    private String ming;
    private String xing;
    /**
     * 唯一标识
     */
    private long birthTime;

    public BasePerson() {
        birthTime = getBirthTime();
    }

    protected long getBirthTime() {
        return System.currentTimeMillis();
    }

    public String getMing() {
        return ming;
    }


    public void setXingMing(String xing, String ming) {
        this.xing = xing;
        this.ming = ming;
    }

    public String getXing() {
        return xing;
    }

    public String getFullName() {
        return xing + ming;
    }


    public void setScean(BaseScean scean) {
        this.mScean = scean;
    }


    protected BaseScean getScean() {
        return mScean;
    }

    /**
     * 添加一个朋友
     *
     * @param person
     */
    public void addFriend(BasePerson person) {
        if (!friends.contains(person)) {
            friends.add(person);
        }
    }

    /**
     * 判断是否朋友
     *
     * @param person
     * @return
     */
    public boolean isFriend(BasePerson person) {
        return friends.contains(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasePerson that = (BasePerson) o;
        return birthTime == that.birthTime;
    }

    @Override
    public int hashCode() {

        return Objects.hash(birthTime);
    }

    /**
     * 执行
     */
    public void next() {

    }

    /**
     * 能触发交友
     *
     * @return
     */
    protected boolean canMakeFriendAction() {
        return false;
    }
}
