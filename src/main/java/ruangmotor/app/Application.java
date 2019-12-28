/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author Randy
 */
@SpringBootApplication(
        scanBasePackages = {
            "ruangmotor.app", "ruangmotor.app.service", "ruangmotor.app.web.controller",
            "ruangmotor.imi", "ruangmotor.imi.service", "ruangmotor.imi.web.controller",
            "ruangmotor.imi.web.rest", "ruangmotor.imi.scheduler"
        })
@EnableScheduling
@EnableWebMvc
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
