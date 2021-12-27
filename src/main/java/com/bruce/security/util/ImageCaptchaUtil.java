package com.bruce.security.util;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/12/27 13:48
 * @Author fzh
 */
public class ImageCaptchaUtil {


    private static final int codeCount = 4;
    private static final int lineCount = 20;
    private static final Random random = new Random();

    @SneakyThrows(IOException.class)
    public static byte[] toByteArray(BufferedImage image) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        }
    }

    @SneakyThrows(IOException.class)
    public static byte[] toByteArray(BufferedImage image, HttpServletResponse response) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", response.getOutputStream());
            return out.toByteArray();
        }
    }

    public static BufferedImage createImage(int width, int height, String code) {
        int fontWidth = width / codeCount;
        int fontHeight = height - 5;
        int codeY = height - 8;
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }
        float yawpRate = 0.01f;
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buffImg.setRGB(x, y, random.nextInt(255));
        }
        for (int i = 0; i < codeCount; i++) {
            String strRand = code.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            g.drawString(strRand, i * fontWidth + 3, codeY);
        }
        return buffImg;
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
