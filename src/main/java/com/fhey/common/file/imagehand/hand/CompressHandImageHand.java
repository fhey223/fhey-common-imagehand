package com.fhey.common.file.imagehand.hand;

import com.fhey.common.file.imagehand.enums.CompressTypeEnum;
import com.fhey.common.file.imagehand.enums.KeepAspectRatioEnum;
import com.fhey.common.file.imagehand.rule.CompressRule;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * @author fhey
 * @date 2022-07-08 17:02:11
 * @description:
 */
public class CompressHandImageHand extends BaseImageHand<CompressRule> {

    @Override
    public boolean hand(Thumbnails.Builder<? extends InputStream> thumbnails, CompressRule rule, BufferedImage image) {
        if (CompressTypeEnum.SCALE_COMPRESS.equals(rule.getCompressType())){//按比例缩放
            thumbnails.scale(rule.getScale());
        } else if (CompressTypeEnum.WIDTH_HEIGHT_COMPRESS.equals(rule.getCompressType())) {//按宽高缩放
            KeepAspectRatioEnum keepAspectRatio = rule.getKeepAspectRatio();
            if(null == keepAspectRatio){
                keepAspectRatio = KeepAspectRatioEnum.NO_KEEP;
            }
            switch (keepAspectRatio){
                case KEEP_BY_WITH:
                    thumbnails.size(rule.getWidth(), rule.getHeight()).keepAspectRatio(true);
                    break;
                case KEEP_AUTO:
                    compressKeepAspectRatio(thumbnails, rule, image);
                    break;
                default:
                    thumbnails.size(rule.getWidth(), rule.getHeight()).keepAspectRatio(false);
                    break;
            }
        }
        return false;
    }

    private void compressKeepAspectRatio(Thumbnails.Builder<? extends InputStream> thumbnails, CompressRule rule, BufferedImage image) {
        int width;
        int height;
        BigDecimal aspectRatio = BigDecimal.valueOf(image.getWidth()).divide(BigDecimal.valueOf(image.getHeight()),2,BigDecimal.ROUND_HALF_UP);
        BigDecimal aspectRatioWidth = BigDecimal.valueOf(rule.getHeight()).multiply(aspectRatio);
        BigDecimal aspectRatioHeight = BigDecimal.valueOf(rule.getWidth()).divide(aspectRatio,2,BigDecimal.ROUND_HALF_UP);

        if(aspectRatioHeight.compareTo(BigDecimal.valueOf(rule.getHeight())) < 0) {
            height = rule.getHeight();
            width = aspectRatioWidth.intValue();
        } else {
            width = rule.getWidth();
            height = aspectRatioHeight.intValue();
        }
        thumbnails.size(width, height).keepAspectRatio(true);
    }
}
