package com.fhey.common.file.imagehand.hand;

import com.fhey.common.file.imagehand.rule.ImageHandRule;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author fhey
 * @date 2022-07-08 17:07:36
 * @description: 
 */
public abstract class BaseImageHand<T extends ImageHandRule> {
    public abstract boolean hand(Thumbnails.Builder<? extends InputStream> thumbnails, T rule, BufferedImage image);
}
