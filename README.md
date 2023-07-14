# 一、前言

最近用结合thumbnailator和Graphics2D封装了一个图片工具类，目前可以实现图片的裁剪、压缩、添加图片水印、文字水印、多行文字水印等功能，同时该工具类的实现使用了建造者模式、责任链模式、工厂模式、策略模式等多种设计模式，感觉图片处理的功能有一定的通用性，所以这次写一篇文章来分享一下这个工具类的使用方式和实现原理，代码不足之处还望大家指正，文末也会提供代码的github地址。



## 二、工具类的依赖和简单介绍

## 1、添加依赖

```xml
		<dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>0.4.20</version>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.7.15</version>
        </dependency>
        <dependency>
            <groupId>commons-chain</groupId>
            <artifactId>commons-chain</artifactId>
            <version>1.2</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
        </dependency>
        <!-- 仅仅为了支持MultipartFile类型文件的加载，不加载MultipartFile这个依赖可以去掉-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>5.3.12</version>
            <scope>compile</scope>
        </dependency>
```



## 2、简单的使用

首先这个处理类提供了一个建造简化了工具栏使用，下面一个简单的使用示例，实现了图的裁剪裁剪，从这个示例代码可以看出这个图片处理工具类的建造者主要由load（加载需要处理的图片）、addRule（添加图片处理规则，可以多次添加）、toFile（输出处理后的图片）三部分组成。

```java
ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.SCALE_REGION)
                        .positions(Positions.TOP_CENTER)
                        .scale(0.5d).build())
                .toFile("D:\\test\\test-region-scale.jpg");
```



## 3、加载需要处理的图片

加载需要处理的图片的方法是load() 方法，建造者调用的第一个方法必须是这个load()方法。load()方法提供多个重载方法，支持以下传参。

```java
	/**
     * 加载图片
     * @param absolutePath 图片绝对路径
     * @return Builder
     */
	Builder load(String absolutePath);

    /**
     * 加载图片
     * @param file 图片文件
     * @return Builder
     */
    Builder load(File file);

	/**
     * 加载图片
     * @param file 图片文件
     * @return Builder
     */
    Builder load(MultipartFile file);
    
    /**
     * 加载图片
     * @param url 图片url
     * @return Builder
     */
    Builder load(URL url);

 	/**
     * 加载图片
     * @param inputStream 图片输入流
     * @return Builder
     */
    Builder load(InputStream inputStream);

	/**
     * 加载图片
     * @param image 图片
     * @param fileType 图片类型
     * @return Builder
     */
    Builder load(BufferedImage image, String fileType);
```



## 4、添加图片处理规则

 添加图片处理规则的方法是addRule()，支持RegionRule（裁剪规则）、CompressRule（压缩规则）、ImageWatermarkRule（图片水印规则）、TextWatermarkRule（文字水印规则）、MultipleTextWatermarkRule（多行文字水印规则）。规则的添加支持Builder方式和使用规则工厂的方法

### 4.1 Builder的方式

```java
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
```



### 4.2  使用规则工厂的方式

```java
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
```





## 5、输出处理后的图片

输出处理后的图片的方法是toFile()或toOutputStream()方法，是建造者最后需要调用的一个方法。输出处理后的图片的方法如下

```java
		 /**
         * 将处理后的图片导出到文件
         * @param file 图片文件
         */
         void toFile(File file);

        /**
         * 将处理后的图片导出到文件
         * @param absolutePath 图片绝对路径
         */
         void toFile(String absolutePath);

        /**
         * 将处理后的图片导出到输出流
         * @param out 输出流
         */
         void toOutputStream(OutputStream out);
```





# 三、使用方式

下面我们使用同一张示例图片依次演示一下RegionRule（裁剪规则）、CompressRule（压缩规则）、ImageWatermarkRule（图片水印规则）、TextWatermarkRule（文字水印规则）、MultipleTextWatermarkRule（多行文字水印规则）这五种规则的设置和实现效果，下图处理前的示例原图，图片的原始比例为3840 X 2160：
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844532-2120856782.jpg)




## 1、图片裁剪

图片裁剪提供了按长宽裁剪和按比例裁剪两种裁剪方式

### 图片裁剪规则实体类

```java
import com.fhey.common.file.imagehand.enums.RegionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @author fhey
 * @date 2022-07-08 17:42:51
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
```



### 1.1 按长宽裁剪

从图片中间裁剪300 X 300的区域

#### 1.1.1 示例代码

```java
 @Test
    public void testWidthHeightRegion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
                        .positions(Positions.CENTER)
                        .width(300)
                        .height(300).build())
                .toFile("D:\\test\\test-region-widthHeight.jpg");
    }
```

#### 1.1.2 实现效果

由下图可以看出处理后的图片正好是300 X 300，但是只截取了中间一小块
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844577-1058147209.jpg)




### 1.2 按比例裁剪

从上中开始按图片0.5的比列裁剪

#### 1.2.1 示例代码

```java
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
```

#### 1.2.2 实现效果

有下图可以看出裁剪之后长宽都是原图的一半，因为从上中开始裁剪所以原图下方一些图片被裁剪掉了
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844802-1150992416.jpg)




## 2、图片压缩

图片压缩提供了按长宽压缩和按比例压缩两种压缩方式，其中按长宽压缩又提供了不保持比列、但宽度保持比列、自动保持比列。

### 图片压缩规则实体类

```java
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
```



### 2.1 按长宽压缩 不保持比例

#### 2.1.1 示例代码

将图片压缩成300 X 300

```java
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
```

#### 2.1.2 实现效果

由下图可以看出处理后的图片是300 X300，但是图片变形严重
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844339-2094565989.jpg)




### 2.2 按长宽压缩 按宽度保持长宽比例

将图片压缩成300 X 300，这里的缩 按宽度保持比例采用了thumbnailator提供的默认的保持长宽比列的方式

#### 2.2.1 示例代码

```java
@Test
    public void testWidthHeightCompressKeepAspectRatioByWidth() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
                        .width(300)
                        .height(300)
                        .keepAspectRatio(KeepAspectRatioEnum.KEEP_BY_WITH).build())
                .toFile("D:\\test\\test-compress-widthHeight-keep-byWidth.jpg");
    }
```

#### 2.2.2 实现效果

由下图可以看出thumbnailator默认的保持长宽比的做法，将宽度处理成了300，但是高度只有169，不到300了
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844797-1346277665.jpg)




### 2.3 按长宽压缩 自动保持长宽比例

将图片压缩成300 X 300，因为thumbnailator默认的保持长宽比的做法在某些场景下不符合需求，所以我有自己扩展了自定义的一个保持长宽比例的做法，即在保持长宽比的情况下让处理后的图片宽高不会低于设定值。

#### 2.3.1 示例代码

```java
@Test
     public void testWidthHeightCompressKeepAspectRatioAuto() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
                        .width(300)
                        .height(300)
                        .keepAspectRatio(KeepAspectRatioEnum.KEEP_AUTO).build())
                .toFile("D:\\test\\test-compress-widthHeight-keep-auto.jpg");
    }
```

#### 2.3.2 实现效果

由下图可以看出处理后的图片,宽度533，高度300
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844599-584345871.jpg)






### 2.4 按比例压缩

按图片0.5的比列压缩

#### 2.4.1 示例代码

```java
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
```

#### 2.4.2 实现效果

可以看出压缩之后长宽都是原图的一半
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844827-1590466645.jpg)




## 3、结合图片压缩和图片裁剪生成缩略图

在需要展示图片的列表中，比如电商中常见的商品列表，为了提升列表的性能，列表里展示的图片往往都是缩略图。但是通上面的图片压缩和图片裁剪把图片处理成300 X300可以发现处理后的图片图片变形严重要么图片细节缺失过多展示效果不好，那我们应该怎么生成一张符合尺寸且保证展示效果的缩略图呢？方法就是将图片压缩和图片裁剪结合起来使用，先根据缩略图的尺寸要求压缩图片（压缩后的长宽都不能低于缩略图的尺寸），然后再根据缩略图的尺寸裁剪图片。代码示例和展示效果如下。

### 3.1 错误的写法

#### 3.1.1 错误的代码示例：

```java
		int width = 300;
        int height = 300;
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.WIDTH_HEIGHT_COMPRESS)
                        .width(width)
                        .height(height)
                        .keepAspectRatio(KeepAspectRatioEnum.KEEP_AUTO).build())
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
                        .positions(Positions.CENTER)
                        .width(width)
                        .height(height).build())
                .toFile("D:\\test\\test-compressAndRegion.jpg");
```

因为thumbnailator的裁剪都是对原图的裁剪这样使用只会使之前的压缩规则实现，造成只会按原图裁剪的情况。执行效果等同于

```java
		int width = 300;
        int height = 300;
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
                        .positions(Positions.CENTER)
                        .width(width)
                        .height(height).build())
                .toFile("D:\\test\\test-compressAndRegion.jpg");
```

### 3.1.2  实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844343-1798468001.jpg)




### 3.2 正确的写法

#### 3.2.1 正确的代码示例：

所以正确的做法是需要压缩图片之后先输出图片，在第二次调用建造者加载图片，再进行图片裁剪。

正确的代码示例：

```java
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
                        .keepAspectRatio(KeepAspectRatioEnum.KEEP_AUTO).build())
                .toFile("D:\\test\\test-compressAndRegion.jpg");
        ImageHandBuilder.load("D:\\test\\test-compressAndRegion.jpg")
                .addRule(ImageHandRuleFactory.regionRuleBuilder()
                        .regionType(RegionTypeEnum.WIDTH_HEIGHT_REGION)
                        .positions(Positions.CENTER)
                        .width(width)
                        .height(height).build())
                .toFile("D:\\test\\test-compressAndRegion.jpg");
    }
```



#### 3.2.2  实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844495-304572173.jpg)




## 4、添加图片水印

水印规则基类：

```java
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

```



### 添加图片水印规则实体类

```java
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
```





### 4.1 普通的添加图片水印

#### 4.1.1 示例代码

```java
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
```

#### 4.1.2 实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844528-129426733.jpg)




### 4.2 添加图片水印 按占原图宽高比

按占原图0.3宽高比添加图片水印

#### 4.2.1 示例代码

```java
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
```

#### 4.2.2 实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110845060-88325249.jpg)





## 5、添加文字水印

### 添加文字水印实体类

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
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
```





### 5.1 普通的添加文字水印

#### 5.1.1 示例代码

```java
@Test
    public void testTextWatermark() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .textBuilder()
                        .text("@知北游")
                        .color(Color.WHITE)
                        .font(new Font("宋体", Font.BOLD, 100))
                        .margin(20)
                        .alpha(0.8d)
                        .positions(Positions.BOTTOM_RIGHT).build())
                .toFile("D:\\test\\test-watermark-text.jpg");
    }
```

#### 5.1.2 实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844717-103518998.jpg)




### 5.2 添加文字水印 按占原图宽高比

按占原图0.3宽高比添加文字水印

#### 5.2.1 示例代码

```java
@Test
    public void testTextWatermarkProportion() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        ImageHandBuilder.load(imageResource)
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .textBuilder()
                        .text("@知北游")
                        .color(Color.WHITE)
                        .font(new Font("宋体", Font.BOLD, 100))
                        .margin(20)
                        .proportion(0.3d)
                        .alpha(0.8d)
                        .positions(Positions.BOTTOM_RIGHT).build())
                .toFile("D:\\test\\test-watermark-text-proportion.jpg");
    }
```

#### 5.2.2 实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844966-1848440899.jpg)





## 5、添加多行文字水印

### 添加多行文字水印规则实体类

```java
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
```





#### 5.1 示例代码

```java
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
```

#### 5.2 实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110845103-2031682899.jpg)


## 6、最后再来叠个buff，把几种规则一起使用

#### 5.1 示例代码

```java
@Test
    public void testAddBuff() throws Exception {
        ClassLoader classLoader = ImageHandBuilderTest.class.getClassLoader();
        URL imageResource = classLoader.getResource("pic/example.jpg");
        URL syResource = classLoader.getResource("pic/watermark.png");
        InputStream syInput = syResource.openStream();
        BufferedImage syImage = ImageIO.read(syInput);
        ImageHandBuilder.load(imageResource)
                //按比例压缩
                .addRule(ImageHandRuleFactory.compressRuleBuilder()
                        .compressType(CompressTypeEnum.SCALE_COMPRESS)
                        .scale(0.5d).build())
                //添加图片水印
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .imageBuilder()
                        .waterImage(syImage)
                        .alpha(0.5d)
                        .positions(Positions.BOTTOM_RIGHT).build())
                //添加文字水印
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .textBuilder()
                        .text("@知北游")
                        .color(Color.WHITE)
                        .font(new Font("宋体", Font.BOLD, 70))
                        .margin(20)
                        //.proportion(0.3d)
                        .alpha(0.8d)
                        .positions(Positions.BOTTOM_LEFT).build())
                //添加多行文字水印
                .addRule(ImageHandRuleFactory.waterRuleBuilder()
                        .multipleTextBuilder()
                        .rotateDegree(-35d)
                        .ySpace(300)
                        .xSpace(250)
                        .color(Color.RED)
                        .font(new Font("宋体", Font.BOLD, 30))
                        .text("三十功名尘与土,八千里路云和月")
                        .alpha(0.7d)
                        .build())
                .toFile("D:\\test\\test-addBuff.jpg");
    }
```

#### 5.2 实现效果
![请添加图片描述](https://img2023.cnblogs.com/blog/685402/202307/685402-20230712110844521-1122475991.jpg)

# 总结
​    到此这篇图片处理工具类的使用方式和实现原理都已经介绍完了，这个工具类实现图片裁剪、图片压缩、添加图片、添加文字水印和添加多行文字水印功能。因为第一次封装这样的工具类难免有些简陋，甚至可能有些错误，还望各位大佬海涵和指正。
