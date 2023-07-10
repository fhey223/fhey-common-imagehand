package com.fhey.common.file.imagehand;

import com.fhey.common.file.imagehand.rule.watermark.ImageWatermarkRule;
import com.fhey.common.file.imagehand.rule.watermark.MultipleTextWatermarkRule;
import com.fhey.common.file.imagehand.rule.watermark.TextWatermarkRule;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class ImageHandTest {
    public static void main(String[] args) throws Exception {
//        Thumbnails.of("D:\\picture\\美女\\ac18d1b0d3e034e48c99f52bb3ae44be.jpg")
//                .size(300, 300).keepAspectRatio(true)
//                //.sourceRegion(Positions.CENTER, 300, 300)
//                .outputQuality(1f).outputFormat("jpg")
//                .toFile("D:\\test\\test1.jpg");
//        Thumbnails.of("D:\\test\\test1.jpg")
//                //.size(500, 300).keepAspectRatio(true)
//                .sourceRegion(Positions.CENTER, 300, 300)
//                .outputQuality(1f).outputFormat("jpg")
//                .scale(1)
//                .toFile("D:\\test\\test1.jpg");


        FileInputStream syInput = new FileInputStream("D:\\picture\\其他\\知北游印章1.png");
        BufferedImage syImage = ImageIO.read(syInput);
        ImageHandBuilder.load("D:\\picture\\美女\\ac18d1b0d3e034e48c99f52bb3ae44be.jpg")
//                .addRule(RegionRule.builder()
//                        .regionType(RegionTypeEnum.SCALE_REGION)
//                        .positions(Positions.TOP_CENTER)
//                        .scale(0.75d).build())
//                .addRule(CompressRule.builder()
//                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
//                        .width(300)
//                        .height(500)
//                        .keepAspectRatio(false).build())
                .addRule(ImageWatermarkRule.builder()
                        .alpha(0.5d)
                        .positions(Positions.BOTTOM_RIGHT)
                        .proportion(0.2d)
                        .waterImage(syImage).build())
                .addRule(TextWatermarkRule.builder()
                        .color(Color.WHITE)
                        .alpha(0.8d)
                        .positions(Positions.BOTTOM_LEFT)
                        .font(new Font("宋体", Font.BOLD, 80))
                        .margin(20)
                        //.proportion(0.3d)
                        .text("@知北游").build())
                .addRule(MultipleTextWatermarkRule.builder()
                        .rotateDegree(-35d)
                        .ySpace(500)
                        .xSpace(350)
                        .color(Color.RED)
                        .alpha(0.7d)
                        .font(new Font("宋体", Font.BOLD, 30))
                        .text("三十功名尘与土,八千里路云和月").build())
                .toFile("D:\\test\\test.jpg");
    }


//        add(WatermarkRule.builder()
//                .watermarkType(WatermarkTypeEnum.PIC_WATER_MARK)
//                .alpha(Double.valueOf(alpha))
//                .position(Positions.BOTTOM_RIGHT)
//                .proportion(Double.valueOf(proportion))
//                .waterImage(syImg)
//                .build());
//    }};
}
