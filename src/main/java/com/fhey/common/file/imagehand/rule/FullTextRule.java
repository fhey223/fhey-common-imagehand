package com.fhey.common.file.imagehand.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullTextRule{

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
}