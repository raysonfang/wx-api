package com.github.niefy.modules.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.github.niefy.modules.wx.handler.miniapp.MaLogHandler;
import com.github.niefy.modules.wx.handler.miniapp.MaNullHandler;
import com.github.niefy.modules.wx.handler.miniapp.PicHandler;
import com.github.niefy.modules.wx.handler.miniapp.QrcodeHandler;
import com.github.niefy.modules.wx.handler.miniapp.SubscribeMsgHandler;
import com.github.niefy.modules.wx.handler.miniapp.TextHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序消息路由配置
 * @author fanglei
 * @date 2020-10-23
 */
@RequiredArgsConstructor
@Configuration
public class WxMaMessageRoutorConfiguration {
    private final MaNullHandler maNullHandler;
    private final MaLogHandler maLogHandler;
    private final PicHandler picHandler;
    private final QrcodeHandler qrcodeHandler;
    private final SubscribeMsgHandler subscribeMsgHandler;
    private final TextHandler textHandler;
    
    @Bean
    WxMaMessageRouter maMessageRouter(WxMaService wxMaService) {
        final WxMaMessageRouter router = new WxMaMessageRouter(wxMaService);
        router
                .rule().handler(maLogHandler).next()
                .rule().async(false).content("订阅消息").handler(subscribeMsgHandler).end()
                .rule().async(false).content("文本").handler(textHandler).end()
                .rule().async(false).content("图片").handler(picHandler).end()
                .rule().async(false).content("二维码").handler(qrcodeHandler).end()
                .rule().async(false).handler(maNullHandler).end();
        return router;
    }
    
}
