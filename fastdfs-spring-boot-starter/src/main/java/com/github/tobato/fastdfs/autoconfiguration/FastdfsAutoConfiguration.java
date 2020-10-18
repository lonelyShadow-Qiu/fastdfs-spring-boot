package com.github.tobato.fastdfs.autoconfiguration;

import com.github.tobato.fastdfs.FdfsClientConstants;
import com.github.tobato.fastdfs.annotation.EnableFastdfsConfiguration;
import com.github.tobato.fastdfs.config.FastdfsProperties;
import com.github.tobato.fastdfs.domain.conn.*;
import com.github.tobato.fastdfs.domain.fdfs.DefaultThumbImageConfig;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FastdfsConfiguration Fastdfs自动装配类
 *                      配置自动暴露功能接口
 * @author qiuzulin
 * @date 2020-09-27 16:28
 */
@Configuration
@ConditionalOnBean(
        annotation = {EnableFastdfsConfiguration.class}
)
@EnableConfigurationProperties(FastdfsProperties.class)
public class FastdfsAutoConfiguration {


    private final FastdfsProperties properties;

    public FastdfsAutoConfiguration(FastdfsProperties properties) {
        super();
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean(PooledConnectionFactory.class)
    public PooledConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setSoTimeout(properties.getSoTimeout());
        pooledConnectionFactory.setConnectTimeout(properties.getConnectTimeout());
        return pooledConnectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionPoolConfig.class)
    @ConfigurationProperties(prefix = FdfsClientConstants.POOL_CONFIG_PREFIX)
    public ConnectionPoolConfig connectionPoolConfig() {
        return new ConnectionPoolConfig();
    }

    @Bean
    @ConditionalOnMissingBean(FdfsConnectionPool.class)
    @ConditionalOnBean({PooledConnectionFactory.class, ConnectionPoolConfig.class})
    public FdfsConnectionPool fdfsConnectionPool(PooledConnectionFactory pooledConnectionFactory, ConnectionPoolConfig connectionPoolConfig) {
        return new FdfsConnectionPool(pooledConnectionFactory, connectionPoolConfig);
    }

    /**
     *  @ConditionalOnProperty(
            prefix = FdfsClientConstants.ROOT_CONFIG_PREFIX,
            value = FdfsClientConstants.TRACKERLIST
    )*/
    @Bean
    @ConditionalOnMissingBean(TrackerConnectionManager.class)
    @ConditionalOnBean(FdfsConnectionPool.class)
    public TrackerConnectionManager trackerConnectionManager(FdfsConnectionPool fdfsConnectionPool) {
        Assert.hasText(properties.getTrackerList(), "[fdfs.trackerList] must not be null");
        List<String> trackerList = Arrays.asList(properties.getTrackerList().split(FdfsClientConstants.SEMI)).stream().map(s -> (s.trim())).collect(Collectors.toList());
        return new TrackerConnectionManager(fdfsConnectionPool, trackerList);
    }

    @Bean
    @ConditionalOnMissingBean(TrackerClient.class)
    @ConditionalOnBean(TrackerConnectionManager.class)
    public TrackerClient trackerClient(TrackerConnectionManager trackerConnectionManager) {
        return new DefaultTrackerClient(trackerConnectionManager);
    }

    @Bean
    @ConditionalOnMissingBean(FdfsConnectionManager.class)
    @ConditionalOnBean(FdfsConnectionPool.class)
    public FdfsConnectionManager fdfsConnectionManager(FdfsConnectionPool fdfsConnectionPool) {
        return new FdfsConnectionManager(fdfsConnectionPool);
    }

    @Bean
    @ConditionalOnMissingBean(ThumbImageConfig.class)
    @ConfigurationProperties(prefix = FdfsClientConstants.THUMB_IMAGE_CONFIG_PREFIX)
    public ThumbImageConfig thumbImageConfig() {
        return new DefaultThumbImageConfig();
    }

    @Bean
    @ConditionalOnMissingBean(FastFileStorageClient.class)
    @ConditionalOnBean({TrackerClient.class, FdfsConnectionManager.class, ThumbImageConfig.class})
    public FastFileStorageClient fastFileStorageClient(TrackerClient trackerClient,
                                                       FdfsConnectionManager fdfsConnectionManager,
                                                       ThumbImageConfig thumbImageConfig) {
        return new DefaultFastFileStorageClient(trackerClient, fdfsConnectionManager, thumbImageConfig);
    }

    @Bean
    @ConditionalOnMissingBean(AppendFileStorageClient.class)
    @ConditionalOnBean({TrackerClient.class, FdfsConnectionManager.class})
    public AppendFileStorageClient appendFileStorageClient(TrackerClient trackerClient,
                                                           FdfsConnectionManager fdfsConnectionManager) {
        return new DefaultAppendFileStorageClient(trackerClient, fdfsConnectionManager);
    }


    @Bean
    @ConditionalOnMissingBean(GenerateStorageClient.class)
    @ConditionalOnBean({TrackerClient.class, FdfsConnectionManager.class})
    public GenerateStorageClient generateStorageClient(TrackerClient trackerClient,
                                                       FdfsConnectionManager fdfsConnectionManager) {
        return new DefaultGenerateStorageClient(trackerClient, fdfsConnectionManager);
    }


}
