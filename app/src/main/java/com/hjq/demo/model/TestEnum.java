package com.hjq.demo.model;

public enum TestEnum {
    SCREEN("屏幕"),
    TOUCH("触摸"),
    WIFI("WIFI"),
    LAN("以太网"),
    FORG("4G"),
    SPEAKER("喇叭"),
    HEADPHONE("耳机"),
    KEY("按键"),
    BACK_LIGHT("背光"),
    BLUETOOTH("蓝牙"),
    CAMERA("摄像头"),
    KEYBOARD("键盘"),
    SCAN_GUN("扫描枪"),
    PRINTER("打印机"),
    ;

    private final String content;
    private TestEnum(String content)
    {
        this.content=content;
    }

    public String value() {
        return content;
    }
}
