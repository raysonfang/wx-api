package com.github.niefy.modules.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序service配置
 *
 * @author fanglei
 * @date 2020-10-23
 */
@RequiredArgsConstructor
@Configuration
public class WxMaServiceConfiguration {
    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMaService =  new WxMaServiceImpl();
        wxMaService.setMaxRetryTimes(3);
        return wxMaService;
    }
}
