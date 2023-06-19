package com._vois.iotconfigurationservice.devices;

import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
public class IoTDeviceController {
    private final IoTDeviceService service;

    public IoTDeviceController(IoTDeviceService ioTDeviceService) {
        this.service = ioTDeviceService;
    }

    @GetMapping("/")
    List<IoTDevice> all() {

        List<IoTDevice> devices = service.getAll();
        devices.sort(Comparator.comparingInt(IoTDevice::getPinCode));
        return devices;

    }

    @PostMapping("/")
    IoTDevice create(@RequestBody IoTDevice device) {
        return service.create(device);
    }

    @GetMapping("/{id}")
    IoTDevice getOne(@PathVariable long id) {
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    IoTDevice updateOne(@RequestBody IoTDevice newDevice, @PathVariable long id) {
        return service.updateOne(newDevice, id);
    }

    @DeleteMapping("/{id}")
    void deleteOne(@PathVariable long id) {
        service.deleteOne(id);
    }

    @PostMapping("/configure_device/{pinCode}")
    IoTDevice configureDevice(@PathVariable int pinCode) {
        return service.configureDevice(pinCode);
    }


}

