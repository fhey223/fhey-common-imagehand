package com.fhey.common.file.imagehand.hand.watermark;

import com.fhey.common.file.imagehand.rule.watermark.ImageWatermarkRule;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author fhey
 * @date 2022-01-23 18:31:09
 * @description: TODO
 */
public class ImageHandWatermarkHand extends AbstractWatermarkHand<ImageWatermarkRule> {
    /**
     * 获取水印图片
     * @param srcImg 原图
     * @param rule 图片水印规则
     * @return Builder
     */
    @Override
    public BufferedImage getWaterImg(BufferedImage srcImg, ImageWatermarkRule rule){
        try {
            if (rule.getProportion() == null){
                return rule.getWaterImage();
            }
            BufferedImage waterImg = rule.getWaterImage();
            int waterWidth = (int) (srcImg.getWidth() * rule.getProportion());
            int waterHeight = waterWidth * waterImg.getHeight() / waterImg.getWidth();
            return Thumbnails.of(waterImg).size(waterWidth, waterHeight).keepAspectRatio(true).asBufferedImage();
        } catch (IOException e){
           throw new RuntimeException(e);
        }
    }
}
