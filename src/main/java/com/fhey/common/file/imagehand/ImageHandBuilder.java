package com.fhey.common.file.imagehand;

import cn.hutool.core.io.FileTypeUtil;
import com.fhey.common.file.imagehand.constants.ImageHandConstants;
import com.fhey.common.file.imagehand.rule.CompressRule;
import com.fhey.common.file.imagehand.rule.ImageHandRule;
import com.fhey.common.file.imagehand.base.ImageHandCommand;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fhey
 * @date 2022-07-08 17:02:11
 * @description: 图片处理器
 */
public final class ImageHandBuilder {

    /**
     * 加载图片
     * @param absolutePath 图片绝对路径
     * @return Builder
     */
    public static Builder load(String absolutePath) {
        return load(new File(absolutePath));
    }

    /**
     * 加载图片
     * @param file 图片文件
     * @return Builder
     */
    public static Builder load(File file) {
        try {
            return load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载图片
     * @param file 图片文件
     * @return Builder
     */
    public static Builder load(MultipartFile file) {
        try {
            return load(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载图片
     * @param url 图片url
     * @return Builder
     */
    public static Builder load(URL url){
        try {
            return load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载图片
     * @param inputStream 图片输入流
     * @return Builder
     */
    public static Builder load(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = cloneInputStream(inputStream);
             InputStream inputStream1 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
             InputStream inputStream2 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())){
            String fileType = FileTypeUtil.getType(inputStream1);
            BufferedImage image = ImageIO.read(inputStream2);
            inputStream.close();
            return load(image, fileType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载图片
     * @param image 图片
     * @param fileType 图片类型
     * @return Builder
     */
    public static Builder load(BufferedImage image, String fileType) {
        return new Builder(image, fileType);
    }

    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Builder {
        /**
         * 图片处理责任链
         */
        private Chain execChain;

        /**
         * 文件类型
         */
        private String fileType;

        /**
         * 需要处理的图片
         */
        private BufferedImage image;

        private  Thumbnails.Builder<? extends BufferedImage> thumbnails;

        private Set<Class<? extends ImageHandRule>> ruleSet;

        public Builder(BufferedImage image, String fileType) {
            this.image = image;
            this.fileType = fileType.toUpperCase();
            this.thumbnails = Thumbnails.of(image);
            this.execChain = new ChainBase();
            this.ruleSet = new HashSet<>();
        }

        /**
         * 添加图片处理规则
         * @param rule 图片处理规则
         * @return Builder
         */
        public Builder addRule(ImageHandRule rule) {
            //检查规则
            rule.check();
            //检查规则是否重复
            if(!rule.canRepeat()){
                if(this.ruleSet.contains(rule.getClass())){
                    throw new RuntimeException("规则:" + rule.getClass().getSimpleName() + "不能重复添加");
                }
                this.ruleSet.add(rule.getClass());
            }
            //给图片处理责任链添加任务
            ImageHandCommand instance = ImageHandCommand.getInstance(rule);
            execChain.addCommand(instance);
            return this;
        }

        /**
         * 返回Thumbnails.Builder 方便自定义处理
         * @return Thumbnails.Builder
         */
        public Thumbnails.Builder<? extends BufferedImage> getThumbnails() {
            return this.thumbnails;
        }

        /**
         * 开始处理图片
         * @return Builder
         */
        private Builder process() throws Exception {
            Context context = new ContextBase();
            context.put(ImageHandConstants.PROCESS_IMAGE_KEY, this.image);
            context.put(ImageHandConstants.TARGET_KEY, this.thumbnails);
            //开始执行图片处理责任链
            execChain.execute(context);
            thumbnails.outputQuality(1f).outputFormat(this.fileType);
            //如果没有压缩规则，则不压缩
            if(!ruleSet.contains(CompressRule.class)){
                thumbnails.scale(1f);
            }
            return this;
        }

        /**
         * 将处理后的图片导出到文件
         * @param file 图片文件
         */
        public void toFile(File file) throws Exception {
            this.process();
            this.thumbnails.toFile(file);
        }

        /**
         * 将处理后的图片导出到文件
         * @param absolutePath 图片绝对路径
         */
        public void toFile(String absolutePath) throws Exception {
            this.process();
            this.thumbnails.toFile(absolutePath);
        }

        /**
         * 将处理后的图片导出到输出流
         * @param out 输出流
         */
        public void toOutputStream(OutputStream out) throws Exception {
            this.process();
            this.thumbnails.toOutputStream(out);
        }
    }
}
