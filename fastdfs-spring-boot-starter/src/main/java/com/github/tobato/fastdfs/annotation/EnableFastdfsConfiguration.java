package com.github.tobato.fastdfs.annotation;

import java.lang.annotation.*;

/**
  *  使用fastdfs-spring-boot-starter组件时
  *  配合EnableFastdfsConfiguration才能启动fastdfs服务
  *
  *@author qiuzulin
  *@date  2020/9/28
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableFastdfsConfiguration {
}
