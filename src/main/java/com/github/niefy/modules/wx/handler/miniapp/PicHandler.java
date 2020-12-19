package com.github.niefy.modules.wx.handler.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PicHandler extends AbstractMaHandler {
    @Override
    public WxMaXmlOutMessage handle(WxMaMessage wxMaMessage, Map<String, Object> map, WxMaService wxMaService, WxSessionManager wxSessionManager) throws WxErrorException {
        try {
            WxMediaUploadResult uploadResult = wxMaService.getMediaService()
                    .uploadMedia("image", "png",
                            ClassLoader.getSystemResourceAsStream("tmp.png"));
            wxMaService.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .newImageBuilder()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMaMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}
