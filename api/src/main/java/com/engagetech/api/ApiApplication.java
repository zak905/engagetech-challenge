package com.engagetech.api;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class ApiApplication {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${environment}")
	String environment;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
	
	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST");
            }
        };
    }
    
    @Bean
    @Primary
    public DataSource dataSource() throws InterruptedException {
	    	if("prod".equals(environment)) {
	    	logger.info("sleeping 10s to make sure database is up");
	    	Thread.sleep(10000);
	        return DataSourceBuilder.create() 
	        		                .url("jdbc:mysql://database:3306/expenses")
	        		                .driverClassName("com.mysql.jdbc.Driver")
	        		                .username("tester")
	        		                .password("password")
	        		                .build();
	    	} else {
	    		return DataSourceBuilder.create() 
		                .url("jdbc:h2:mem:db;DB_CLOSE_ON_EXIT=FALSE")
		                .driverClassName("org.h2.Driver")
		                .build();
	    	}
        }
    
    @CacheEvict(allEntries = true, cacheNames = { "exchangeRates"})
    @Scheduled(fixedDelay = 30000)
    public void cacheEvict() {
    	logger.info("clearing cache");		
    }
    
}
