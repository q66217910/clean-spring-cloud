package com.zd.core.filter.type;

import com.google.common.collect.Lists;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.List;

/**
 * 自定义配置文件过滤(过滤配置目录)
 */
public class ConfigFilter implements TypeFilter {

    private static List<String> fiter = Lists.newArrayList("redis", "mysql");

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前正在扫描类信息s
        Resource resource = metadataReader.getResource();
        return fiter.stream().anyMatch(name -> resource.getDescription().toLowerCase().contains(name));
    }
}
