package com.github.niefy.modules.wx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxMaUserInfoVo {
    private String sessionKey;
    private String openid;
    private String unionid;
    private String login;
    private String phone;
}
