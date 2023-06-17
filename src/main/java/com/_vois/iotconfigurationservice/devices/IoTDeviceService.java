package com._vois.iotconfigurationservice.devices;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IoTDeviceService {
    private final IoTDeviceRepository repository;
    public IoTDeviceService(IoTDeviceRepository repository) {
        this.repository = repository;
    }

    List<IoTDevice> getAll(){
        return repository.findAll();
    }

    IoTDevice create(IoTDevice newDevice){
        return repository.save(newDevice);
    }

    IoTDevice getOne (Long id){
        if(repository.findById(id).isEmpty()){
            throw new IllegalStateException("No device exists with this ID");
        }
        return repository.findById(id).orElseThrow(()-> new DeviceNotFoundExceptions(id));
    }

    IoTDevice updateOne(IoTDevice newDevice, Long id){
        return repository.findById(id).map((device)->{
            device.setStatus(newDevice.getStatus());
            device.setTemp(newDevice.getTemp());
            device.setPin_number(newDevice.getPin_number());
            return device;
        }).orElseGet(()->{
            newDevice.setId(id);
            return repository.save(newDevice);
        });
    }

    void deleteOne(Long id){
        repository.deleteById(id);
    }

}
