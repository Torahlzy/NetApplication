package com.torahli.myapplication.game.base;

public abstract class BasePerson {

    private BaseScean mScean;
    private String ming;
    private String xing;

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
