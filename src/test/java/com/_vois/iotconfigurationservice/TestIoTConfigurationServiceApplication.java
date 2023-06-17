package com._vois.iotconfigurationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
class TestIoTConfigurationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(IoTConfigurationServiceApplication::main).with(TestIoTConfigurationServiceApplication.class).run(args);
    }

}
