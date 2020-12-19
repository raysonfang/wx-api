package com.github.niefy.modules.wx.enums;

/**
 * 微信账号类型
 */
public enum WxAccountTypeEnum {
    SUBSCRIBE_NUMBER(1),
    SERVICE_NUMBER(2),
    MINIAPP(3);
    
    
    private int value;
    
    WxAccountTypeEnum(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public static WxAccountTypeEnum of(String name) {
        try {
            return WxAccountTypeEnum.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }
}
