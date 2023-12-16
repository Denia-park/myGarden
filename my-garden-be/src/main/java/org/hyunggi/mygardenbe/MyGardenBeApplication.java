package org.hyunggi.mygardenbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyGardenBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyGardenBeApplication.class, args);
    }

}
