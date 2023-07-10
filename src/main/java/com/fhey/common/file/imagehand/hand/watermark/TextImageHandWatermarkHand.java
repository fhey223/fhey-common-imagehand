package com.fhey.common.file.imagehand.hand.watermark;

import cn.hutool.core.lang.Tuple;
import com.fhey.common.file.imagehand.rule.watermark.TextWatermarkRule;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fhey
 * @date 2022-01-23 18:31:39
 * @description: TODO
 */
public class TextImageHandWatermarkHand extends AbstractWatermarkHand<TextWatermarkRule> {
    /**
     * 获取水印图片
     * @param srcImg 原图
     * @param rule 文字水印规则
     * @return Builder
     */
    //@Override
    public BufferedImage getWaterImg(BufferedImage srcImg, TextWatermarkRule rule) {
        int srcWidth = srcImg.getWidth();
        int srcHeight = srcImg.getHeight();
        int margin = rule.getMargin();
        String text = rule.getText();
        ///确定字体
        Font font;
        if (rule.getProportion() != null) {
            double fontLength = srcWidth * rule.getProportion();
            Integer frontSize = (int) (fontLength / text.length());
            font = new Font(rule.getFont().getName(), rule.getFont().getStyle(), frontSize);
        } else {
            font = rule.getFont();
        }
        FontMetrics metrics = new FontMetrics(font) {};
        Rectangle2D bounds = metrics.getStringBounds(text, null);
        //效果一样
//        FontRenderContext frc = new FontRenderContext(null, true, true);
//        Rectangle2D bounds = font.getStringBounds(text, frc);
        // 字符宽度
        int textWidth = (int) bounds.getWidth();
        // 字符高度
        int textHeight = (int) bounds.getHeight();
        BufferedImage sysImage = new BufferedImage(textWidth + margin, textHeight + margin, BufferedImage.TYPE_INT_RGB);
        // 获取画笔对象
        Graphics2D graphics = sysImage.createGraphics();
        //设置图片透明
        sysImage = graphics.getDeviceConfiguration().createCompatibleImage(textWidth + margin, textHeight + margin, Transparency.TRANSLUCENT);
        graphics = sysImage.createGraphics();
        //设置字体
        graphics.setFont(font);
        //设置水印字体颜色
        Color color;
        if (rule.getColor() != null) {
            color = rule.getColor();
        } else {
            color = new Color(0, 0, 0, 3);
        }
        graphics.setColor(color);
        // 消除java.awt.Font字体的锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 设置水印透明度
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rule.getAlpha().floatValue()));
        Tuple marginInfo = getMarginInfo(rule.getPositions(), margin);
        graphics.drawString(text,  marginInfo.get(0) , metrics.getAscent() + (int) marginInfo.get(1));
        graphics.dispose();
        return sysImage;
    }

    public Tuple getMarginInfo(Positions positions, Integer margin) {
        int xMargin = 0;
        int yMargin = 0;
        switch (positions){
            case TOP_LEFT:
                xMargin = margin;
                yMargin = margin;
                break;
            case TOP_CENTER:
                xMargin = margin/2;
                yMargin = margin;
                break;
            case TOP_RIGHT:
                xMargin = 0;
                yMargin = margin;
                break;
            case CENTER_LEFT:
                xMargin = margin;
                yMargin = margin/2;
                break;
            case CENTER:
                xMargin = margin/2;
                yMargin = margin/2;
                break;
            case CENTER_RIGHT:
                xMargin = 0;
                yMargin = margin/2;
                break;
            case BOTTOM_LEFT:
                xMargin = margin;
                yMargin = 0;
                break;
            case BOTTOM_CENTER:
                xMargin = margin/2;
                yMargin = 0;
                break;
            case BOTTOM_RIGHT:
                xMargin = 0;
                yMargin = 0;
                break;
        }
        return new Tuple(xMargin, yMargin);
    }

}
