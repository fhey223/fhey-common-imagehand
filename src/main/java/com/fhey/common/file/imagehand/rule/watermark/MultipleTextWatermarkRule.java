package com.fhey.common.file.imagehand.rule.watermark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.*;

/**
 * @author fhey
 * @date 2022-07-08 18:00:57
 * @description: 
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MultipleTextWatermarkRule extends BaseImageWatermarkRule {
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
     * 旋转角度
     */
    private Double rotateDegree;

    /**
     * x轴间距
     */
    private Integer xSpace;

    /**
     * y轴间距
     */
    private Integer ySpace;

    @Override
    public boolean check() {
        super.setPositions(Positions.CENTER);
        if(null == text){
            throw new RuntimeException("水印文字(水印文字)不能为空!");
        }
        if (null == font){
            throw new RuntimeException("水印文字字体(font)不能为空!");
        }
        if (null == rotateDegree){
            throw new RuntimeException("水印透明度(rotateDegree)不能为空!");
        }
        if (null == rotateDegree){
            throw new RuntimeException("旋转角度(rotateDegree)不能为空!");
        }
        if (null == xSpace){
            throw new RuntimeException("x轴间距(xSpace)不能为空!");
        }
        if (null == ySpace){
            throw new RuntimeException("y轴间距(ySpace)不能为空!");
        }
        return true;
    }
}

