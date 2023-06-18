package com._vois.iotconfigurationservice.devices;

import org.springframework.web.bind.annotation.*;

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
        return service.getAll();
    }

    @PostMapping("/")
    IoTDevice create(@RequestBody IoTDevice device) {
        return service.create(device);
    }

    @GetMapping("/{id}")
    IoTDevice getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    IoTDevice updateOne(@RequestBody IoTDevice newDevice, @PathVariable Long id) {
        return service.updateOne(newDevice, id);
    }

    @DeleteMapping("/{id}")
    void deleteOne(@PathVariable Long id) {
        service.deleteOne(id);
    }

    @PostMapping("/configure_device")
    IoTDevice configureDevice(@RequestBody int pinCode) {
        return service.configureDevice(pinCode);
    }


}

