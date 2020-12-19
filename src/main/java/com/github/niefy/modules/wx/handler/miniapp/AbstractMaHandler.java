package com.github.niefy.modules.wx.handler.miniapp;

import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rayson fang
 */
public abstract class AbstractMaHandler implements WxMaMessageHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

}
