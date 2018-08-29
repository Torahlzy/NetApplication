package com.torahli.myapplication.framwork.bean;

public abstract class BaseLiveData {
    protected void setNoError() {
        this.error = 0;
        this.errorMsg = null;
    }

    protected void setErrorAndMsg(int error, String msg) {
        this.error = error;
        this.errorMsg = msg;
    }

    public boolean isError() {
        return error != 0;
    }

    /**
     * {@link NetErrorType}
     */
    protected int error = 0;
    protected String errorMsg = null;

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "BaseLiveData{" +
                "error=" + error +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
