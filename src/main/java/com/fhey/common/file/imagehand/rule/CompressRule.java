package com.fhey.common.file.imagehand.rule;

import com.fhey.common.file.imagehand.enums.CompressTypeEnum;
import com.fhey.common.file.imagehand.enums.KeepAspectRatioEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fhey
 * @date 2022-07-08 17:28:28
 * @description: 压缩规则
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompressRule implements ImageHandRule {

    /**
     * 压缩宽度
     */
    private Integer width;

    /**
     * 压缩高度
     */
    private Integer height;

    /**
     * 压缩比例
     */
    private Double scale;

    /**
     * 压缩类型
     */
    private CompressTypeEnum compressType;

    /**
     * 是否保持宽高比
     */
    private KeepAspectRatioEnum keepAspectRatio;

    @Override
    public boolean check() {
        if (null == compressType){
            throw new RuntimeException("压缩类型(compressType)不能为空!");
        }
        if (CompressTypeEnum.SCALE_COMPRESS.equals(compressType) && null == scale){
            throw new RuntimeException("按比列压缩模式压缩比例(scale)不能为空!");
        }
        if (CompressTypeEnum.WIDTH_HEIGHT_COMPRESS.equals(compressType) && (null == width || null == height)){
            throw new RuntimeException("按宽高压缩模式宽高不能为空!");
        }
        return true;
    }
}
