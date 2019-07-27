package com.hjq.demo.model;

public enum StateEnum {
    NOT_TEST("待测试"),
    TESTING("测试中"),
    SUCCESS("成功"),
    FAILED("失败");

    private final String state;
    private StateEnum(String state)
    {
        this.state=state;
    }

    public String value() {
        return state;
    }
}
