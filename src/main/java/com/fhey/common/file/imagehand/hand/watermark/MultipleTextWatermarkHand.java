package com.fhey.common.file.imagehand.hand.watermark;

import com.fhey.common.file.imagehand.rule.watermark.MultipleTextWatermarkRule;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class MultipleTextWatermarkHand extends AbstractWatermarkHand<MultipleTextWatermarkRule> {

    /**
     * 获取全屏水印图片
     * @param srcImg 原图
     * @param rule 文字水印规则
     * @return Builder
     */
    @Override
    public BufferedImage getWaterImg(BufferedImage srcImg, MultipleTextWatermarkRule rule) {
        rule.setPositions(Positions.CENTER);
        int srcWidth = srcImg.getWidth();
        int srcHeight = srcImg.getHeight();
        String text = rule.getText();
        Optional<MultipleTextWatermarkRule> multipleTextRuleOptional = Optional.ofNullable(rule);
        // 将画布大小绘制成原图大小
        BufferedImage sysImage= new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
        // 获取画笔对象
        Graphics2D graphics = sysImage.createGraphics();
        //清除矩形区域
        graphics.clearRect(0, 0, srcWidth, srcHeight);
        // 设置绘图区域透明
        sysImage = graphics.getDeviceConfiguration().createCompatibleImage(srcWidth, srcHeight, Transparency.TRANSLUCENT);
        graphics = sysImage.createGraphics();
        FontMetrics metrics = new FontMetrics(rule.getFont()) {};
        Rectangle2D bounds = metrics.getStringBounds(text, null);
        // 字符宽度
        int textWidth = (int) bounds.getWidth();
        // 字符高度
        int textHeight = (int) bounds.getHeight();
        //设置字体
        graphics.setFont( rule.getFont());
        //设置水印字体颜色
        Color color;
        if (rule.getColor() != null) {
            color = rule.getColor();
        } else {
            color = new Color(0, 0, 0);
        }
        graphics.setColor(color);
        // 消除java.awt.Font字体的锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 设置水印透明度
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rule.getAlpha().floatValue()));
        // 设置倾斜角度
        graphics.rotate(Math.toRadians(multipleTextRuleOptional.map(MultipleTextWatermarkRule::getRotateDegree).orElse(0d))
                , (double) srcImg.getWidth() / 2
                , (double) srcImg.getHeight() / 2);
        int xCoordinate = -srcWidth / 2, yCoordinate;
        // 循环添加水印
        while (xCoordinate < srcWidth * 1.5) {
            yCoordinate = -srcHeight / 2;
            while (yCoordinate < srcHeight * 1.5) {
                graphics.drawString(rule.getText(), xCoordinate, yCoordinate);
                yCoordinate += textHeight + multipleTextRuleOptional.map(MultipleTextWatermarkRule::getYSpace).orElse(textHeight);
            }
            xCoordinate += textWidth + multipleTextRuleOptional.map(MultipleTextWatermarkRule::getXSpace).orElse(textWidth / text.length() * 2);
        }
        graphics.dispose();
        return sysImage;
    }
}
