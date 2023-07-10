package com.fhey.common.file.imagehand.rule.watermark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * @author fhey
 * @date 2022-01-23 18:00:57
 * @description: TODO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TextWatermarkRule extends BaseImageWatermarkRule {
    /**
     * 水印文字
     */
    private String text;

    /**
     * 水印文字颜色
     */
    private Color color;

    /**
     * 水印文字字体
     */
    private Font font;

    /**
     * 水印占原图宽高比
     */
    private Double proportion;

    /**
     * 和边缘的间距
     */
    private int margin;

    @Override
    public boolean check() {
        if(null == text){
            throw new RuntimeException("水印文字(水印文字)不能为空!");
        }
        if (null == font){
            throw new RuntimeException("水印文字字体(font)不能为空!");
        }
        if(null == super.getPositions()){
            throw new RuntimeException("水印位置(positions)不能为空!");
        }
        return true;
    }
}
