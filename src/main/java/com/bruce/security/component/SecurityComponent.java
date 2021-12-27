package com.bruce.security.component;

import com.bruce.security.config.SecurityProperty;
import com.bruce.security.exceptions.SecurityException;
import com.bruce.security.model.common.ImageCaptcha;
import com.bruce.security.model.constant.RedisConstant;
import com.bruce.security.util.AesEcbUtil;
import com.bruce.security.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RAtomicLong;
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
@Slf4j
public class SecurityComponent {

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
        String captcha = captchaBucket.getAndDelete();
        if (StringUtils.isBlank(captcha)) {
            throw new SecurityException("验证码错误!");
        }
        if (!StringUtils.equalsIgnoreCase(captcha, code)) {
            throw new SecurityException("验证码错误!");
        }
    }

    public String getRealPassword(String username, String password) {
        SecurityProperty.RetryManager retryManager = property.getRetry();
        RAtomicLong retryNumAtomicLong = redissonComponent.getRAtomicLong(MessageFormat.format(RedisConstant.LOGIN_RETRY_NUM, username));
        try {
            String key = MessageFormat.format(RedisConstant.SECRET_KEY, username);
            RBucket<String> secretKeyBucket = redissonComponent.getRBucket(key);
            // 加密密钥不存在表示密码不合法
            if (!secretKeyBucket.isExists()) {
                throw new SecurityException("密钥不存在!");
            }
            //获取之后要立马删除
            String secretKey = secretKeyBucket.getAndDelete();
            return AesEcbUtil.aesDecrypt(password, secretKey);
        } catch (Exception e) {
            log.error("解密失败:{}", e.getMessage(), e);
            incrExpireAndThrow(retryNumAtomicLong, retryManager);
            return "";
        }
    }

    public String getLoginSecretKey(String username) {
        String token = TokenUtil.getToken(username, 16);
        //设置1分钟有效期
        String key = MessageFormat.format(RedisConstant.SECRET_KEY, username);
        redissonComponent.getRBucket(key).set(token, 1L, TimeUnit.MINUTES);
        return token;
    }

    public void incrExpireAndThrow(RAtomicLong retryNumAtomicLong, SecurityProperty.RetryManager retryManager) throws SecurityException {
        long retryNum = retryNumAtomicLong.incrementAndGet();
        if (retryNum >= retryManager.getNums()) {
            throw new SecurityException("您的账号已被锁定，请稍后尝试");
        }
        retryNumAtomicLong.expire(retryManager.getExpire(), TimeUnit.SECONDS);
        throw new SecurityException(MessageFormat.format("账号密码错误,若连错{0}次，账号将被锁定,您还有{1}次机会",
                retryManager.getNums(), retryManager.getNums() - retryNum));
    }

}
