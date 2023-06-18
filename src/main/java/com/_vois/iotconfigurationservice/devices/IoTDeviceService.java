package com._vois.iotconfigurationservice.devices;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class IoTDeviceService {
    private final IoTDeviceRepository repository;

    public IoTDeviceService(IoTDeviceRepository repository) {
        this.repository = repository;
    }

    List<IoTDevice> getAll() {
        return repository.findAll();
    }

    IoTDevice create(IoTDevice newDevice) {
        if(!isValidPinCode(newDevice.getPinCode())
                || !isValidDeviceUpdate(newDevice.getStatus(), newDevice.getTemp())){

            throw new IllegalStateException("Provided data is not correct!");
        }

        return repository.save(newDevice);
    }

    IoTDevice getOne(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new IllegalStateException("No device exists with this ID");
        }
        return repository.findById(id).orElseThrow(() -> new DeviceNotFoundExceptions(id));
    }

    IoTDevice updateOne(IoTDevice newDevice, Long id) {
        if (!isValidDeviceUpdate(newDevice.getStatus(), newDevice.getTemp())) {
            throw new IllegalStateException("Provided data update is not valid!");
        }
        return repository.findById(id).map((device) -> {
            device.setStatus(newDevice.getStatus());
            device.setTemp(newDevice.getTemp());
            return device;
        }).orElseGet(() -> {
            newDevice.setId(id);
            return repository.save(newDevice);
        });
    }

    void deleteOne(Long id) {
        repository.deleteById(id);
    }


    // Using a device pinCode, its status is set to ACTIVE and its temperature is set to a
    // random number between 0 and 10
    IoTDevice configureDevice(int pinCode) {
        String pinCodeString = String.valueOf(pinCode);
        IoTDevice device = repository.findByPinCode(pinCodeString).get(0);
        device.setStatus("ACTIVE");
        device.setTemp(generateTemp());
        return device;
    }

    int generateTemp() {
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    // When updating a new device, the temperature must be valid to a valid device status
    boolean isValidDeviceUpdate(String status, int temp) {
        if (status.equalsIgnoreCase("ACTIVE")) {
            return 0 <= temp && temp <= 10;
        }
        else if (status.equalsIgnoreCase("READY")) {
            return temp == -1;
        }
        else return false;
    }

    // Validate provided pinCode to be an int of 6 digits
    boolean isValidPinCode(long pinCode) {
        return String.valueOf(pinCode).length() == 7;
    }

}
