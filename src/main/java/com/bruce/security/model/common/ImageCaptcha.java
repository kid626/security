package com.bruce.security.model.common;

import com.bruce.security.util.ImageCaptchaUtil;
import com.bruce.security.util.RandomUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 13:45
 * @Author fzh
 */
@Data
public class ImageCaptcha {

    private String rid;
    private String code;
    private Date createdAt;
    @Transient
    private BufferedImage bufferedImage;

    public static ImageCaptcha create(int width, int height, String rid) {
        if (StringUtils.isBlank(rid)) {
            rid = RandomUtil.randomUUID();
        }
        String code = RandomUtil.randomStr(4);
        BufferedImage image = ImageCaptchaUtil.createImage(width, height, code);
        ImageCaptcha ret = new ImageCaptcha();
        ret.setRid(rid);
        ret.setCode(code);
        ret.setBufferedImage(image);
        ret.setCreatedAt(new Date());
        return ret;
    }


}
