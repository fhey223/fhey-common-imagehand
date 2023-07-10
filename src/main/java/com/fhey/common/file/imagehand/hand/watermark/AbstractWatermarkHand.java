package com.fhey.common.file.imagehand.hand.watermark;

import com.fhey.common.file.imagehand.rule.watermark.BaseImageWatermarkRule;
import com.fhey.common.file.imagehand.hand.BaseImageHand;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public abstract class AbstractWatermarkHand<T extends BaseImageWatermarkRule> extends BaseImageHand<T> {
    @Override
    public boolean hand(Thumbnails.Builder<? extends InputStream> thumbnails, T rule, BufferedImage image) {
        if(rule.getAlpha() == null){
            rule.setAlpha(1d);
        }
        BufferedImage waterImg = getWaterImg(image, rule);
        thumbnails.watermark(rule.getPositions(), waterImg, rule.getAlpha().floatValue());
        return false;
    }

    /**
     * 获取水印图片
     * @param srcImg 原图
     * @param rule 水印规则
     * @return Builder
     */
     abstract BufferedImage getWaterImg(BufferedImage srcImg, T rule);
}
