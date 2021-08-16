package com.github.niefy.modules.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.github.niefy.common.annotation.SysLog;
import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.WxQrCode;
import com.github.niefy.modules.wx.form.WxQrCodeForm;
import com.github.niefy.modules.wx.service.WxQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wxQrCode")
@Api(tags = {"公众号带参二维码-API"})
public class WxQrcodeApiController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxQrCodeService wxQrCodeService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMaService wxMaService;
    /**
     * 创建带参二维码ticket
     */
    @PostMapping("/createTicket")
    @ApiOperation(value = "创建带参二维码ticket",notes = "ticket可以换取二维码图片")
    @SysLog("第三方创建带参二维码")
    public R createTicket(String appid, @RequestBody WxQrCodeForm form) throws WxErrorException {
        if (wxMaService.switchover(appid)) {
            WxQrCode wxQrCode = wxQrCodeService.createMaQrcode(appid, form);
            return R.ok().put(wxQrCode);
        }
        wxMpService.switchoverTo(appid);
        WxMpQrCodeTicket ticket = wxQrCodeService.createQrCode(appid,form);
        return R.ok().put(ticket);
    }
}
