package com.bruce.security.model.common;

import com.bruce.security.util.ImageCaptchaUtil;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.UUID;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 13:45
 * @Author fzh
 */
@Data
public class ImageCaptcha {


    private static final String CODE_GENERATOR = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

    private String rid;
    private String code;
    private Date createdAt;
    @Transient
    private BufferedImage bufferedImage;

    public static ImageCaptcha create(int width, int height, String rid) {
        if (rid == null) {
            rid = UUID.randomUUID().toString().replaceAll("-", "");
        }
        String code = randomStr(4);
        BufferedImage image = ImageCaptchaUtil.createImage(width, height, code);
        ImageCaptcha ret = new ImageCaptcha();
        ret.setRid(rid);
        ret.setCode(code);
        ret.setBufferedImage(image);
        ret.setCreatedAt(new Date());
        return ret;
    }

    private static String randomStr(int n) {
        StringBuilder sb = new StringBuilder();
        int len = CODE_GENERATOR.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            sb.append(CODE_GENERATOR.charAt((int) r));
        }
        return sb.toString();
    }

}
