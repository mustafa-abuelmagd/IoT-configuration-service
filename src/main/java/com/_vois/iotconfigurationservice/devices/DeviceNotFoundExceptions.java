package com._vois.iotconfigurationservice.devices;

public class DeviceNotFoundExceptions extends RuntimeException{
    DeviceNotFoundExceptions(Long id){
        super("No devices exists with this id: "+ id);
    }
}