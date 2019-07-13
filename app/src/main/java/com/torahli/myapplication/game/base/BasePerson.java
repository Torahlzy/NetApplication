package com.torahli.myapplication.game.base;

import com.torahli.myapplication.game.demo.manager.event.MonthFinishEvent;
import com.torahli.myapplication.game.demo.manager.event.TimeFlyingEvent;
import com.torahli.myapplication.game.person.bag.BaseBag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BasePerson {
    private final int id;
    /**
     * 朋友
     */
    private List<BasePerson> friends = new ArrayList<>();
    /**
     * 背包
     */
    private BaseBag bag;
    private BaseScean mScean;
    private String ming;
    private String xing;
    /**
     * 唯一标识
     */
    private long birthTime;
    /**
     * 人物每个行动区间所拥有的时间
     */
    protected int remainTime;

    protected int hp;
    /**
     * 生命值上限
     */
    protected int hpLimit;
    protected int sp;
    protected int spLimit;
    protected long currentExp;

    protected int currentLevel;


    /**
     * 物理攻击力
     */
    protected int hpDamage;


    public BasePerson(int id) {
        this.id = id;
        birthTime = getBirthTime();
        bag = new BaseBag();
        remainTime = getFullRemainTime();
    }

    protected abstract int getFullRemainTime();

    protected long getBirthTime() {
        return System.currentTimeMillis();
    }

    public String getMing() {
        return ming;
    }

    private void resetRemainTime() {
        remainTime = getFullRemainTime();
    }

    /**
     * 过月事件
     */
    @Subscribe
    public void onMonthFinishEvent(MonthFinishEvent event) {
        resetRemainTime();
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

    protected void setBag(BaseBag bag) {
        this.bag = bag;
    }

    public BaseBag getBag() {
        return bag;
    }

    public void setScean(BaseScean scean) {
        this.mScean = scean;
    }


    protected BaseScean getScean() {
        return mScean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasePerson that = (BasePerson) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
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

    /**
     * 执行
     */
    @Subscribe
    public void next(TimeFlyingEvent event) {

    }

    /**
     * 能触发交友
     *
     * @return
     */
    protected boolean canMakeFriendAction() {
        return false;
    }

    /**
     * 能触发工作
     *
     * @return
     */
    protected boolean canWorkAction() {
        return false;
    }

    /**
     * 初始化该对象
     */
    public void init() {
        EventBus.getDefault().register(this);
    }

    /**
     * 回收该对象
     */
    public void recycle() {
        EventBus.getDefault().unregister(this);
    }
}
