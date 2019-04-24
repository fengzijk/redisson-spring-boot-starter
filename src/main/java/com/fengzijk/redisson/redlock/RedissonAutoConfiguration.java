
/*
 *   All rights Reserved, Designed By ZTE-ITS
 *   Copyright:    Copyright(C) 2019-2025
 *   Company       FENGZIJK LTD.
 *   @Author:    fengzijk
 *   @Email: guozhifengvip@163.com
 *   @Version    V1.0
 *   @Date:   2019年04月24日 15时02分
 *   Modification       History:
 *   ------------------------------------------------------------------------------------
 *   Date                  Author        Version        Discription
 *   -----------------------------------------------------------------------------------
 *  2019-04-24 15:02:07    fengzijk         1.0         Why & What is modified: <修改原因描述>
 */

package com.fengzijk.redisson.redlock;



import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * -------------------------------------------------
 *
 * @ProjectName : fengzijk-sales
 * @Descprition : redisson自动配置类
 * @Author : fengzijk
 * @email: guozhifengvip@163.com
 * @Time : 2019/3/23 11:07
 * --------------------------------------------------
 */


@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperties.class)

public class RedissonAutoConfiguration {

    @Autowired
    private RedissonProperties redssionProperties;

    /**
     * 哨兵模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.redis.redisson.Sentinel", value = "enabled", havingValue = "true")
    RedissonClient redissonSentinel() {
        String prefix = "redis://";
        Config config = new Config();
        for (int i = 0; i < redssionProperties.getSentinelAddresses().length; i++) {
            redssionProperties.getSentinelAddresses()[i] = prefix + redssionProperties.getSentinelAddresses()[i];
        }

        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(redssionProperties.getSentinelAddresses())
                .setMasterName(redssionProperties.getMasterName())
                .setTimeout(redssionProperties.getTimeout())
                .setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());

        if (StringUtil.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 单机模式自动装配
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.redisson.address")
    RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redssionProperties.getAddress() + ":" + redssionProperties.getPort())
                .setTimeout(redssionProperties.getTimeout())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());

        if (StringUtil.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }

        return Redisson.create(config);

    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    DistributedLocker distributedLocker(RedissonClient redissonSingle) {
        RedissonDistributedLocker locker = new RedissonDistributedLocker();
        locker.setRedissonClient(redissonSingle);
        RedissLockUtil.setLocker(locker);
        return locker;
    }
}

