package com.fhey.common.file.imagehand.base;

import com.fhey.common.file.imagehand.constants.ImageHandConstants;
import com.fhey.common.file.imagehand.hand.BaseImageHand;
import com.fhey.common.file.imagehand.rule.ImageHandRule;
import lombok.Data;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.awt.image.BufferedImage;
import java.io.InputStream;

@Data
public
class ImageHandCommand implements Command {
    private ImageHandCommand(){}

    /**
     * 获取图片处理器责任链任务实例
     * @param rule 图片处理规则
     * @return 图片处理器
     */
    public static ImageHandCommand getInstance(ImageHandRule rule) {
        ImageHandCommand imageHandCommand = new ImageHandCommand();
        imageHandCommand.setRule(rule);
        BaseImageHand imageHand = ImageHandConstants.getImageHand(rule.getClass());
        if (imageHand == null) {
            throw new RuntimeException("规则:" + rule.getClass().getSimpleName() + "未找到对应的处理器");
        }
        imageHandCommand.setImageHand(imageHand);
        return imageHandCommand;
    }

    /**
     * 图片处理规则
     */
    private ImageHandRule rule;

    /**
     * 图片处理器
     */
    private BaseImageHand imageHand;


    @Override
    public boolean execute(Context context) {
        BufferedImage image =(BufferedImage) context.get(ImageHandConstants.PROCESS_IMAGE_KEY);
        Thumbnails.Builder<? extends InputStream> thumbnails = (Thumbnails.Builder<? extends InputStream>) context.get(ImageHandConstants.TARGET_KEY);
        return this.imageHand.hand(thumbnails, this.rule, image);
    }




}
