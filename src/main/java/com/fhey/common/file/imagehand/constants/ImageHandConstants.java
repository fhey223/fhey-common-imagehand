package com.fhey.common.file.imagehand.constants;

import com.fhey.common.file.imagehand.hand.BaseImageHand;
import com.fhey.common.file.imagehand.hand.CompressHandImageHand;
import com.fhey.common.file.imagehand.hand.RegionHandImageHand;
import com.fhey.common.file.imagehand.hand.watermark.ImageHandWatermarkHand;
import com.fhey.common.file.imagehand.hand.watermark.MultipleTextWatermarkHand;
import com.fhey.common.file.imagehand.hand.watermark.TextImageHandWatermarkHand;
import com.fhey.common.file.imagehand.rule.CompressRule;
import com.fhey.common.file.imagehand.rule.ImageHandRule;
import com.fhey.common.file.imagehand.rule.RegionRule;
import com.fhey.common.file.imagehand.rule.watermark.ImageWatermarkRule;
import com.fhey.common.file.imagehand.rule.watermark.MultipleTextWatermarkRule;
import com.fhey.common.file.imagehand.rule.watermark.TextWatermarkRule;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fhey
 * @date 2022-07-08 17:20:29
 * @description: 
 */
public class ImageHandConstants {

    public final  static  String PROCESS_IMAGE_KEY = "image";

    public final  static  String TARGET_KEY = "target";

    private final static Map<Class<? extends ImageHandRule>, BaseImageHand> ruleHandMap = new HashMap<>();

    static {
        ruleHandMap.put(CompressRule.class, new CompressHandImageHand());
        ruleHandMap.put(RegionRule.class, new RegionHandImageHand());
        ruleHandMap.put(ImageWatermarkRule.class, new ImageHandWatermarkHand());
        ruleHandMap.put(TextWatermarkRule.class, new TextImageHandWatermarkHand());
        ruleHandMap.put(MultipleTextWatermarkRule.class, new MultipleTextWatermarkHand());
    }

    public static BaseImageHand getImageHand(Class<? extends ImageHandRule> ruleClass) {
        return ruleHandMap.get(ruleClass);
    }
}
