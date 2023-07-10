package com.fhey.common.file.imagehand.base;

import com.fhey.common.file.imagehand.rule.watermark.ImageWatermarkRule;
import com.fhey.common.file.imagehand.rule.watermark.MultipleTextWatermarkRule;
import com.fhey.common.file.imagehand.rule.watermark.TextWatermarkRule;

public class WaterRuleBuilder{
    public ImageWatermarkRule.ImageWatermarkRuleBuilder imageBuilder(){
        return ImageWatermarkRule.builder();
    }
    public TextWatermarkRule.TextWatermarkRuleBuilder textBuilder(){
        return TextWatermarkRule.builder();
    }
    public MultipleTextWatermarkRule.MultipleTextWatermarkRuleBuilder multipleTextBuilder(){
        return MultipleTextWatermarkRule.builder();
    }

}
