package com.fhey.common.file.imagehand;


import com.fhey.common.file.imagehand.base.ImageHandRuleFactory;
import com.fhey.common.file.imagehand.enums.CompressTypeEnum;
import com.fhey.common.file.imagehand.enums.KeepAspectRatioEnum;
import com.fhey.common.file.imagehand.enums.RegionTypeEnum;
import com.fhey.common.file.imagehand.rule.RegionRule;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

public class ImageHandBuilderTest {

    //Builder方式
    @Test
    public void testBuilder() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(RegionRule.builder()
                        .regionType(RegionTypeEnum.SCALE_REGION)
                        .positions(Positions.TOP_CENTER)
                        .scale(0.5d).build())
                .toFile("D:\\test\\test-region-scale.jpg");
    }

    //Factory方式
    @Test
    public void testFactory() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.SCALE_REGION)
                        .positions(Positions.TOP_CENTER)
                        .scale(0.5d).build())
                .toFile("D:\\test\\test-region-scale.jpg");
    }

    //按长宽裁剪
    @Test
    public void testWidthHeightRegion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
                        .positions(Positions.TOP_CENTER)
                        .width(500)
                        .height(300).build())
                .toFile("D:\\test\\test-region-widthHeight.jpg");
    }

    //按比例裁剪
    @Test
    public void testScaleRegion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.SCALE_REGION)
                        .positions(Positions.TOP_CENTER)
                        .scale(0.5d).build())
                .toFile("D:\\test\\test-region-scale.jpg");
    }

    //按长宽压缩 不保持比例
    @Test
    public void testWidthHeightCompressNoKeepAspectRatio() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
                        .width(300)
                        .height(300).build())
                .toFile("D:\\test\\test-compress-widthHeight-noKeep.jpg");
    }

    //按长宽压缩 保持比例
    @Test
    public void testWidthHeightCompressKeepAspectRatio() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
                        .width(300)
                        .height(300)
                        .keepAspectRatio(KeepAspectRatioEnum.KEEP_By_WITH).build())
                .toFile("D:\\test\\test-compress-widthHeight-keep.jpg");
    }

    //按比例压缩
    @Test
    public void testScaleCompress() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.SCALE_COMPRESS)
                        .scale(0.5d).build())
                .toFile("D:\\test\\test-compress-scale.jpg");
    }

    //先压缩在再裁剪
    @Test
    public void testCompressAndRegion() throws Exception {
        int width = 300;
        int height = 300;
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
                        .width(width)
                        .height(height)
                        .keepAspectRatio(KeepAspectRatioEnum.KEEP_By_AUTO).build())
//                .addRule(ImageHandRuleFactory.regionRuleBuilder()
//                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
//                        .positions(Positions.CENTER)
//                        .width(width)
//                        .height(height).build())
                .toFile("D:\\test\\test-compressAndRegion.jpg");
        ImageHandBuilder.load("D:\\test\\test-compressAndRegion.jpg")
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
                        .positions(Positions.CENTER)
                        .width(width)
                        .height(height).build())
                .toFile("D:\\test\\test-compressAndRegion.jpg");
    }

    @Test
    public void testImageWatermark() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL syResource = classLoader.getResource("pic/watermark.png");
        InputStream syInput = syResource.openStream();
        BufferedImage syImage = ImageIO.read(syInput);
        ;
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .imageBuilder()
                        .waterImage(syImage)
                        .alpha(0.5d)
                        .positions(Positions.BOTTOM_RIGHT).build())
                .toFile("D:\\test\\test-watermark-image.jpg");
    }

    @Test
    public void testImageWatermarkProportion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL syResource = classLoader.getResource("pic/watermark.png");
        InputStream syInput = syResource.openStream();
        BufferedImage syImage = ImageIO.read(syInput);
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .imageBuilder()
                        .waterImage(syImage)
                        .proportion(0.3d)
                        .alpha(0.5d)
                        .positions(Positions.BOTTOM_RIGHT).build())
                .toFile("D:\\test\\test-watermark-image-proportion.jpg");
    }

    @Test
    public void testTextWatermarkProportion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .textBuilder()
                        .text("@知北游")
                        .color(Color.WHITE)
                        .font(new Font("宋体", Font.BOLD, 80))
                        .margin(20)
                        //.proportion(0.3d)
                        .alpha(0.8d)
                        .positions(Positions.TOP_RIGHT).build())
                .toFile("D:\\test\\test-watermark-text.jpg");
    }

    @Test
    public void testMultipleTextWatermarkProportion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .multipleTextBuilder()
                        .rotateDegree(-35d)
                        .ySpace(500)
                        .xSpace(350)
                        .color(Color.RED)
                        .font(new Font("宋体", Font.BOLD, 30))
                        .text("三十功名尘与土,八千里路云和月")
                        .alpha(0.7d)
                        .build())
                .toFile("D:\\test\\test-watermark-multipleText.jpg");
    }
}