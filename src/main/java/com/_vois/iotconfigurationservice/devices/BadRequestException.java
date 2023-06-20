package com._vois.iotconfigurationservice.devices;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
