package com.studyyoun.androidbaselibrary.model;

import java.io.Serializable;

public class BaseBean implements Serializable {
    private Integer errorCode;

    private String errorMsg;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
