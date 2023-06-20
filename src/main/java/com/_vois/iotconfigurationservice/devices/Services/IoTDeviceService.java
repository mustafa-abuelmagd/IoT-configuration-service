package com._vois.iotconfigurationservice.devices.Services;

import com._vois.iotconfigurationservice.devices.Errors.BadRequestException;
import com._vois.iotconfigurationservice.devices.Errors.DeviceNotFoundException;
import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class IoTDeviceService {
    private final IoTDeviceRepository repository;

    public IoTDeviceService(IoTDeviceRepository repository) {
        this.repository = repository;
    }

    public List<IoTDevice> getAll() {
        return repository.findAll();
    }

    public IoTDevice create(IoTDevice newDevice) {
        if (!isValidPinCode(newDevice.getPinCode())
                || !isValidDeviceUpdate(newDevice.getStatus(), newDevice.getTemp())) {

            throw new BadRequestException("Provided data is not correct!");
        }

        return repository.save(newDevice);
    }

    public IoTDevice getOne(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new DeviceNotFoundException(id);
        }
        return repository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    @Transactional
    public IoTDevice updateOne(IoTDevice newDevice, Long id) {
        if (!isValidDeviceUpdate(newDevice.getStatus(), newDevice.getTemp())) {
            throw new BadRequestException("Provided data update is not valid!");
        }
        return repository.findById(id).map(device -> {
            device.setStatus(newDevice.getStatus());
            device.setTemp(newDevice.getTemp());
            return device;
        }).orElseGet(() -> {
            newDevice.setId(id);
            return repository.save(newDevice);
        });
    }

    public void deleteOne(Long id) {
        repository.deleteById(id);
    }


    // Using a device pinCode, its status is set to ACTIVE and its temperature is set to a
    // random number between 0 and 10
    @Transactional
    public IoTDevice configureDevice(long id) {
        return repository.findById(id).map(device ->{
            device.setStatus("ACTIVE");
            device.setTemp(generateTemp());
            return device;
        }).orElseThrow();


    }

    public int generateTemp() {
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    // When updating a new device, the temperature must be valid to a valid device status
    public boolean isValidDeviceUpdate(String status, int temp) {
        if (status.equalsIgnoreCase("ACTIVE")) {
            return 0 <= temp && temp <= 10;
        } else if (status.equalsIgnoreCase("READY")) {
            return temp == -1;
        } else return false;
    }

    // Validate provided pinCode to be an int of 6 digits
    public boolean isValidPinCode(int pinCode) {
            return String.valueOf(pinCode).length() == 7;
    }

    public boolean isValidDeviceData(String deviceStatus,
                              int deviceTemp,
                              int devicePinCode) {
        return isValidDeviceUpdate(deviceStatus, deviceTemp) && isValidPinCode(devicePinCode);

    }

}
