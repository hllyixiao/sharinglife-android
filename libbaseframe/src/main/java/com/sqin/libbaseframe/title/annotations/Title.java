package com.sqin.libbaseframe.title.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：sqin
 * 日期：2016/3/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {
    /**
     * 标题文字
     * @return
     */
    String titleText();

    /**
     * 显示标题左键图片
     * @return
     */
    boolean showLeftImage() default true;
    /**
     * 标题左键图片资源
     * @return
     */
    int leftImageRes() default 0;

    /**
     * 显示标题右键图片
     * @return
     */
    boolean showRightImage() default false;

    /**
     * 显示标题右键文字
     * @return
     */
    boolean showRightText() default false;

    /**
     * 标题右键文字
     * @return
     */
    String rightText() default "";

    /**
     * 标题右键文字资源
     * @return
     */
    int rightTextRes() default 0;

    /**
     * 标题右键图片资源
     */
    int rightImageRes() default 0;

    /**
     * 内容布局资源文件id
     * @return
     */
    int layoutRes() default 0;

    /**
     * 标题栏背景res
     * @return
     */
    int bgRes() default 0;

//    /**
//     * 文字颜色
//     * @return
//     */
//    int textColor() default Color.WHITE;
}
