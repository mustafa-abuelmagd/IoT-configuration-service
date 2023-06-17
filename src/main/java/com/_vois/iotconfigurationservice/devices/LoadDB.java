package com._vois.iotconfigurationservice.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public class LoadDB {
    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner initDatabase(IoTDeviceRepository repository){
        return args -> {
            log.info("Preloading"+ repository.save(new IoTDevice("Ready", -1, 1234567)));
            log.info("Preloading"+ repository.save(new IoTDevice("Ready", -1, 1234367)));
        };
    }

}
