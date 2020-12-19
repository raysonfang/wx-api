package com.github.niefy.modules.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wx/portal/{appid}")
@Api(tags = {"微信小程序消息 - 腾讯会调用"})
public class WxMaPortalController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMaService wxMaService;
    private final WxMaMessageRouter maMessageRouter;
    
    @GetMapping(produces = "text/plain;charset=utf-8")
    @ApiOperation(value = "微信小程序消息-认证")
    public String authGet(@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        this.logger.info("\n接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
                signature, timestamp, nonce, echostr);
        
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        wxMaService.switchoverTo(appid);
        if (wxMaService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        
        return "非法请求";
    }
    
    @PostMapping(produces = "application/xml; charset=UTF-8")
    @ApiOperation(value = "微信小程序消息-接受消息，进行转发")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "signature", required = false) String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        this.logger.info("\n接收微信请求：[msg_signature=[{}], encrypt_type=[{}], signature=[{}]," +
                        " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                msgSignature, encryptType, signature, timestamp, nonce, requestBody);
    
        this.wxMaService.switchoverTo(appid);
        
        final boolean isJson = Objects.equals(wxMaService.getWxMaConfig().getMsgDataFormat(),
                WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) {
            // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {//xml
                inMessage = WxMaMessage.fromXml(requestBody);
            }
            
            this.route(inMessage, appid);
            return "success";
        }
        
        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody, wxMaService.getWxMaConfig());
            } else {//xml
                inMessage = WxMaMessage.fromEncryptedXml(requestBody, wxMaService.getWxMaConfig(),
                        timestamp, nonce, msgSignature);
            }
            
            this.route(inMessage, appid);
            return "success";
        }
        
        throw new RuntimeException("不可识别的加密类型：" + encryptType);
    }
    
    private void route(WxMaMessage message, String appid) {
        try {
            this.maMessageRouter.getWxMaService().switchoverTo(appid);
            this.maMessageRouter.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }
}
