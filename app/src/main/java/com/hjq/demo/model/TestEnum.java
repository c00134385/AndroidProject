package com.hjq.demo.model;

public enum TestEnum {
    SCREEN("屏幕"),
    TOUCH1("触摸1"),
    TOUCH2("触摸2"),
    BACK_LIGHT("背光"),
    SPEAKER("喇叭"),
    VIDEO("视频播放"),
    WIFI("WIFI"),
    BLUETOOTH("蓝牙"),
    CAMERA("摄像头"),
    SCAN_GUN("扫描枪"),
    PRINTER("打印机"),
    SERIAL("串口"),
    KEYBOARD("键盘"),
    RECORD("录音"),
    LAN("以太网"),
    FORG("4G"),
    HEADPHONE("耳机"),
    KEY("按键"),
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
