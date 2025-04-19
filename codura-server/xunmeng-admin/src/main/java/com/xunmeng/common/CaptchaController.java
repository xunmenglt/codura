package com.xunmeng.common;

import com.google.code.kaptcha.Producer;
import com.xunmeng.common.config.XunMengConfig;
import com.xunmeng.common.constant.CacheConstants;
import com.xunmeng.common.constant.Constants;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.core.redis.RedisCache;
import com.xunmeng.common.utils.uuid.IdUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制类
 */
@CrossOrigin
@Api(tags = "验证码相关接口")
@RestController
public class CaptchaController {
    
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    
    @Autowired
    private RedisCache redisCache;

    /**
     * 生成验证码
     */
    @ApiOperation(value = "验证码生成")
    @GetMapping("/captchaImage")
    public AjaxResult getCode(){
        AjaxResult ajx=AjaxResult.success();
        String uuid= IdUtils.simpleUUID();
        String verifyKey= CacheConstants.CAPTCHA_CODE_KEY+uuid;
        
        String capStr=null;
        String code=null;
        BufferedImage img=null;
        
        // 生成验证码
        String captchaType= XunMengConfig.getCaptchaType();
        if ("math".equalsIgnoreCase(captchaType)){
            // 生成数字验证码
            String capText=captchaProducerMath.createText();
            capStr=capText.substring(0,capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            img=captchaProducerMath.createImage(capStr);
        }else {
            capStr = code = captchaProducer.createText();
            img = captchaProducer.createImage(capStr);
        }
        
        redisCache.setCacheObject(verifyKey,code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        // 转换流信息写出
        FastByteArrayOutputStream os=new FastByteArrayOutputStream();
        try{
            ImageIO.write(img,"jpg",os);
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        ajx.put("uuid",uuid);
        ajx.put("img", Base64Utils.encodeToString(os.toByteArray()));
        return ajx;
    }
}
