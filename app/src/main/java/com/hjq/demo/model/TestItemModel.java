package com.hjq.demo.model;

public class TestItemModel {

    TestEnum title;
    StateEnum state;

    public TestItemModel(TestEnum title, StateEnum state) {
        this.title = title;
        this.state = state;
    }

    public TestEnum getTitle() {
        return title;
    }

    public void setTitle(TestEnum title) {
        this.title = title;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }
}
