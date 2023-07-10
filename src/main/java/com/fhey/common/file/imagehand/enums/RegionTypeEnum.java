package com.fhey.common.file.imagehand.enums;


/**
 * @author fhey
 * @date 2022-01-23 14:59:15
 * @description: TODO
 */
public enum RegionTypeEnum {

    WIDTH_HEIGHT_REGION("0","按宽高裁剪"),
    SCALE_REGION("1","按比例裁剪")
    ;

    private String code;
    private String desc;

    RegionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }
}
