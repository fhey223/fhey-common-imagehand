package com.fhey.common.file.imagehand.hand;

import com.fhey.common.file.imagehand.enums.RegionTypeEnum;
import com.fhey.common.file.imagehand.rule.RegionRule;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author fhey
 * @date 2022-01-23 18:14:14
 * @description: TODO
 */
public class RegionHandImageHand extends BaseImageHand<RegionRule> {
    @Override
    public boolean hand(Thumbnails.Builder<? extends InputStream> thumbnails, RegionRule rule, BufferedImage image) {
        if (RegionTypeEnum.SCALE_REGION.equals(rule.getRegionType())) {
            //按比例裁剪
            Double scale = rule.getScale();
            thumbnails.sourceRegion(rule.getPositions(), (int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
        } else if (RegionTypeEnum.WIDTH_HEIGHT_REGION.equals(rule.getRegionType())) {
            //按宽高裁剪
            thumbnails.sourceRegion(rule.getPositions(), rule.getWidth(), rule.getHeight());
        }
        return false;
    }
}
