package com._vois.iotconfigurationservice.devices.Errors;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(Long id){
        super("No devices exists with this id: "+ id);
    }
}