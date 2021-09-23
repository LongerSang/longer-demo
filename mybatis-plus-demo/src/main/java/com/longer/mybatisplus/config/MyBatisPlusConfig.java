package com.longer.mybatisplus.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@MapperScan("com.longer.mybatisplus.mapper")// 扫描映射接口
@Configuration// 配置类
public class MyBatisPlusConfig {

    // 注册乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }

    // 分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor();
    }

    // 逻辑删除插件
    @Bean
    public ISqlInjector iSqlInjector(){
        return new LogicSqlInjector();
    }

    // 性能分析插件
    @Bean
    @Profile({"dev","test"})// 开发环境和测试环境才生效
    public PerformanceInterceptor performanceInterceptor(){
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(1000);// 单位 ms 设置 sql 执行的最大时间，如果超过则不执行
        // 设置sql格式化
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
}
