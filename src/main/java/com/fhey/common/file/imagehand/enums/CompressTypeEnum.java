package com.fhey.common.file.imagehand.enums;

/**
 * @author fhey
 * @date 2022-07-08 14:59:15
 * @description: 
 */
public enum CompressTypeEnum {

    WIDTH_HEIGHT_COMPRESS("0","按宽高压缩"),
    SCALE_COMPRESS("1","按比例压缩")
    ;

    private String code;
    private String desc;

    CompressTypeEnum(String code, String desc) {
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
