package com.monkeypenthouse.core.config;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class AppConfig {

    // ModelMapper 사용
   @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
