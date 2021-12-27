package com.bruce.security.component;

import com.bruce.security.config.SecurityProperty;
import com.bruce.security.exceptions.SecurityException;
import com.bruce.security.model.common.ImageCaptcha;
import com.bruce.security.model.constant.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 13:42
 * @Author fzh
 */
@Component
public class CaptchaComponent {

    @Autowired
    private RedissonComponent redissonComponent;
    @Autowired
    private SecurityProperty property;

    public ImageCaptcha createCaptcha(Integer width, Integer height, String rid) {
        ImageCaptcha imageCaptcha = ImageCaptcha.create(width, height, rid);
        String key = MessageFormat.format(RedisConstant.IMAGE_CAPTCHA_RID, imageCaptcha.getRid());
        redissonComponent.getRBucket(key).set(imageCaptcha.getCode(), property.getCaptcha().getExpire(), TimeUnit.SECONDS);
        return imageCaptcha;
    }

    public void checkCaptchaAndDelete(String rid, String code) {
        String key = MessageFormat.format(RedisConstant.IMAGE_CAPTCHA_RID, rid);
        RBucket<String> captchaBucket = redissonComponent.getRBucket(key);
        if (captchaBucket == null) {
            throw new SecurityException("验证码错误!");
        }
        String captcha = captchaBucket.get();
        if (StringUtils.isBlank(captcha)) {
            throw new SecurityException("验证码错误!");
        }
        captchaBucket.delete();
        if (!StringUtils.equalsIgnoreCase(captcha, code)) {
            throw new SecurityException("验证码错误!");
        }

    }

}
