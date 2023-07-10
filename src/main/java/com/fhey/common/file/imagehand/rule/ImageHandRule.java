package com.fhey.common.file.imagehand.rule;

/**
 * @author fhey
 * @date 2022-01-23 16:51:25
 * @description: TODO
 */
public interface ImageHandRule {

    boolean check();

    //是否可以重复设置，默认不能，重复将会报错
    default boolean canRepeat() {
        return false;
    }
}
