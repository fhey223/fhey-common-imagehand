package com.fhey.common.file.imagehand.rule.watermark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.awt.image.BufferedImage;

/**
 * @author fhey
 * @date 2022-07-08 18:00:57
 * @description: 
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ImageWatermarkRule extends BaseImageWatermarkRule {
    /**
     * 水印图片
     */
    private BufferedImage waterImage;

    /**
     * 水印占原图宽高比
     */
    private Double proportion;


    @Override
    public boolean check() {
        if(null == waterImage){
            throw new RuntimeException("水印图片(waterImage)不能为空!");
        }
        if(null == super.getPositions()){
            throw new RuntimeException("水印位置(positions)不能为空!");
        }
        return true;
    }
}
