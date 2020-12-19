package com.github.niefy.modules.wx.handler.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MaLogHandler extends AbstractMaHandler {
    @Override
    public WxMaXmlOutMessage handle(WxMaMessage wxMaMessage, Map<String, Object> map, WxMaService wxMaService, WxSessionManager wxSessionManager) throws WxErrorException {
        logger.info("收到消息：" + wxMaMessage.toString());
        wxMaService.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMaMessage.toJson())
                .toUser(wxMaMessage.getFromUser()).build());
        return null;
    }
}
