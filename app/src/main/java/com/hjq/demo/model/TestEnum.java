package com.hjq.demo.model;

public enum TestEnum {
    PRINTER("打印机", true),
    SCAN_GUN("扫描枪", true),
    CAMERA("摄像头", true),
    WIFI("WIFI", true),
    SCREEN("屏幕", true),
    TOUCH1("触摸一", true),
    TOUCH2("触摸二", true),
    BACK_LIGHT("屏幕亮度", true),
    SPEAKER("音频测试", true),
    LAN("有线网卡", true),
    VIDEO("视频播放", true),
    DEVICE("设备信息测试", true),
    SERIAL("串口", true),
    KEY("按键", true),
    KEYBOARD("键盘", true),

    BLUETOOTH("蓝牙", false),
    RECORD("录音", false),
    FORG("4G", false),
    HEADPHONE("耳机", false),
    ;
    private final String content;
    private final boolean enabled;
    private TestEnum(String content, boolean enable)
    {
        this.content = content;
        this.enabled = enable;
    }

    public String value() {
        return content;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
