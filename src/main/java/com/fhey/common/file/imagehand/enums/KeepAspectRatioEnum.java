package com.fhey.common.file.imagehand.enums;

public enum KeepAspectRatioEnum {
    NO_KEEP("0","不保持宽高比"),
    KEEP_By_WITH("1","按宽度保持宽高比"),
    KEEP_By_AUTO("2","自动保持宽高比，会根据宽高自动选择"),
    ;

    private String code;
    private String desc;

    KeepAspectRatioEnum(String code, String desc) {
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
