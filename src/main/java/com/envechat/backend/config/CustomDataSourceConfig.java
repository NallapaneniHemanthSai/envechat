package com.envechat.backend.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class CustomDataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String rawUrl,
            @Value("${spring.datasource.username:}") String configuredUsername,
            @Value("${spring.datasource.password:}") String configuredPassword,
            @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}") String driverClassName,
            @Value("${spring.datasource.hikari.maximum-pool-size:10}") int maximumPoolSize
    ) {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driverClassName);

        if (rawUrl.startsWith("jdbc:")) {
            ds.setJdbcUrl(rawUrl);
            if (configuredUsername != null && !configuredUsername.isBlank()) {
                ds.setUsername(configuredUsername);
                ds.setPassword(configuredPassword == null ? "" : configuredPassword);
            }
        } else {
            PostgresJdbcUrlParser.Parsed parsed = PostgresJdbcUrlParser.parse(rawUrl);
            ds.setJdbcUrl(parsed.jdbcUrl());
            ds.setUsername(parsed.username());
            ds.setPassword(parsed.password());
        }

        ds.setMaximumPoolSize(maximumPoolSize);
        return ds;
    }
}
