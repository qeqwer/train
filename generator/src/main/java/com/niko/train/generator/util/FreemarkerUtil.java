package com.niko.train.generator.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FreemarkerUtil {

    static String ftlPath = "generator/src/main/java/com/niko/train/generator/ftl/";

    static Template temp;

    /**
     * 读模板
     */
    public static void initConfig(String ftlName) throws IOException {
        // 创建FreeMarker配置对象并设置版本
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
        // 设置模板加载目录为静态变量ftlPath指定的路径
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        // 设置对象包装器以确保Java对象能正确转换为模板中的数据模型
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_34));
        // 根据传入的模板名称获取模板对象并赋值给静态变量temp
        temp = cfg.getTemplate(ftlName);
    }

    /**
     * 根据模板，生成文件
     */
    public static void generator(String fileName, Map<String, Object> map) throws IOException, TemplateException {
        // 使用 FileWriter 打开指定文件名的文件。
        FileWriter fw = new FileWriter(fileName);
        // 使用 BufferedWriter 包装 FileWriter 以提高写入效率。
        BufferedWriter bw = new BufferedWriter(fw);
        // 调用 temp.process 方法，将数据映射到模板并写入文件。
        temp.process(map, bw);
        // 刷新缓冲区并关闭文件流
        bw.flush();
        fw.close();
    }
}
