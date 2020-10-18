package com.github.tobato.fastdfs.config;

import com.github.tobato.fastdfs.FdfsClientConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FastdfsProperties Fastdfs属性配置类
 *
 * @author qiuzulin
 * @date 2020-09-27 16:12
 */
@ConfigurationProperties(prefix = FdfsClientConstants.ROOT_CONFIG_PREFIX)
public class FastdfsProperties {

    /**
     * 连接地址, formats: `host:port;host:port`
     */
    private String trackerList;

    /**
     * 读取时间
     */
    private int soTimeout = 1000;
    /**
     * 连接超时时间
     */
    private int connectTimeout = 1000;

    public String getTrackerList() {
        return trackerList;
    }

    public void setTrackerList(String trackerList) {
        this.trackerList = trackerList;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
