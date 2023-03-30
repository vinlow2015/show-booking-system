package com.booking.system.showbookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
public class ShowBookingSystemApplication {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Asia/Singapore"));
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ShowBookingSystemApplication.class, args);
        ctx.close();
    }

}
