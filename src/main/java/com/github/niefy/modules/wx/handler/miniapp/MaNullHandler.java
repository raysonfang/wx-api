package com.github.niefy.modules.wx.handler.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang
 */
@Component
public class MaNullHandler extends AbstractMaHandler {
    @Override
    public WxMaXmlOutMessage handle(WxMaMessage wxMaMessage, Map<String, Object> map, WxMaService wxMaService, WxSessionManager wxSessionManager) throws WxErrorException {
        return null;
    }
}
