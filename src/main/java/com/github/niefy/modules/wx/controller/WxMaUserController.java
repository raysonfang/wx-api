package com.github.niefy.modules.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.niefy.common.utils.JsonUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.WxUser;
import com.github.niefy.modules.wx.service.WxUserService;
import com.github.niefy.modules.wx.vo.WxMaUserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/wx/user/{appid}")
@Api(tags = {"微信小程序 - 用户信息"})
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMaService wxMaService;
    
    private final WxUserService wxUserService;
    /**
     * 登陆接口
     */
    @GetMapping("/login")
    @ApiOperation(value = "微信小程序用户-微信用户登录")
    public R login(@PathVariable String appid, String code) {
        if (StringUtils.isBlank(code)) {
            return R.error("empty jscode");
        }
        wxMaService.switchoverTo(appid);
        
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            WxUser wxUser = wxUserService.getById(session.getOpenid());
            WxMaUserInfoVo wxMaUserInfoVo = new WxMaUserInfoVo();
            wxMaUserInfoVo.setOpenid(session.getOpenid());
            wxMaUserInfoVo.setSessionKey(session.getSessionKey());
            wxMaUserInfoVo.setUnionid(session.getUnionid());
            wxMaUserInfoVo.setLogin("0");
            wxMaUserInfoVo.setPhone("0");
            if(null == wxUser) {
                wxUser = new WxUser();
                wxUser.setAppid(appid);
                wxUser.setOpenid(session.getOpenid());
                wxUser.setUnionid(session.getUnionid());
                wxUser.setSubscribeScene("miniapp");
                wxUserService.save(wxUser);
                return R.ok().put(wxMaUserInfoVo);
            }
            // 需要登录授权
            if(!wxUser.isSubscribe()) {
                return R.ok().put(wxMaUserInfoVo);
            }
            // 需要手机号授权
            if(StringUtils.isEmpty(wxUser.getPhone())) {
                wxMaUserInfoVo.setLogin("1");
                return R.ok().put(wxMaUserInfoVo);
            }
            wxMaUserInfoVo.setLogin("1");
            wxMaUserInfoVo.setPhone("1");
            return R.ok().put(wxMaUserInfoVo);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    @ApiOperation(value = "微信小程序用户-获取微信用户信息")
    public R info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        wxMaService.switchoverTo(appid);
        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return R.error("user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        // 判断是否需要存储
        WxUser wxUser = wxUserService.getById(userInfo.getOpenId());
        if (null == wxUser) {
            return R.error("请重新登录！");
        }
        if(!wxUser.isSubscribe()) {
            logger.info("用户信息：" + JSON.toJSONString(userInfo));
            wxUser = new WxUser(userInfo);
            wxUserService.saveOrUpdate(wxUser);
        }
        return R.ok().put(wxUser);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    @ApiOperation(value = "微信小程序用户-获取微信用户绑定手机号")
    public R phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv,
                   String openid) {
        wxMaService.switchoverTo(appid);

        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return R.error("user check failed");
        }
        
        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
    
        // 判断是否需要存储
        WxUser wxUser = wxUserService.getById(openid);
        if (null == wxUser) {
            return R.error("请先登录");
        }
        if(StringUtils.isEmpty(wxUser.getPhone())) {
            WxUser wxUser1 = new WxUser();
            wxUser1.setOpenid(openid);
            wxUser1.setPhone(phoneNoInfo.getPhoneNumber());
            wxUserService.updateById(wxUser1);
        }
        return R.ok().put(phoneNoInfo);
    }

}
