package com.fhey.common.file.imagehand.rule.watermark;

import com.fhey.common.file.imagehand.rule.ImageHandRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.coobird.thumbnailator.geometry.Positions;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseImageWatermarkRule implements ImageHandRule {

    /**
     * 水印位置
     */
    private Positions positions;

    /**
     * 水印透明度
     */
    private Double alpha;

    @Override
    //设置水印可以重复设置
    public boolean canRepeat() {
        return true;
    }
}
