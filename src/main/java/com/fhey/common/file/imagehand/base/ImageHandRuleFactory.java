package com.fhey.common.file.imagehand.base;

import com.fhey.common.file.imagehand.rule.CompressRule;
import com.fhey.common.file.imagehand.rule.RegionRule;

public class ImageHandRuleFactory {
    public static RegionRule.RegionRuleBuilder regionRuleBuilder(){
        RegionRule.RegionRuleBuilder builder = RegionRule.builder();
        return builder;
    }

    public static CompressRule.CompressRuleBuilder compressRuleBuilder(){
        CompressRule.CompressRuleBuilder builder = CompressRule.builder();
        return builder;
    }

    public static WaterRuleBuilder waterRuleBuilder (){
        return new WaterRuleBuilder();
    }



}
