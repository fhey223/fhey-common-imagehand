package com.fhey.common.file.imagehand.rule;

import com.fhey.common.file.imagehand.enums.RegionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @author fhey
 * @date 2022-01-23 17:42:51
 * @description: 裁剪规则
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionRule implements ImageHandRule {

    /**
     * 位置裁剪
     */
    private Positions positions;

    /**
     * 裁剪宽度
     */
    private Integer width;

    /**
     * 裁剪高度
     */
    private Integer height;

    /**
     * 裁剪比例
     */
    private Double scale;

    /**
     * 裁剪类型
     */
    private RegionTypeEnum regionType;
    
    @Override
    public boolean check() {
        if (null == regionType){
            throw new RuntimeException("裁剪类型(compressType)不能为空!");
        }
        if (RegionTypeEnum.SCALE_REGION.equals(regionType) && null == scale){
            throw new RuntimeException("按比列裁剪模式裁剪比例(scale)不能为空!");
        }
        if (RegionTypeEnum.WIDTH_HEIGHT_REGION.equals(regionType) && (null == width || null == height)){
            throw new RuntimeException("按宽高裁剪模式宽高不能为空!");
        }
        return true;
    }
}
