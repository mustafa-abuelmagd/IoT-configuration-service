package com._vois.iotconfigurationservice.devices.Controllers;

import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import com._vois.iotconfigurationservice.devices.Services.IoTDeviceService;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/devices")
public class IoTDeviceController {
    private final IoTDeviceService service;

    public IoTDeviceController(IoTDeviceService ioTDeviceService) {
        this.service = ioTDeviceService;
    }

    @GetMapping("/")
    public List<IoTDevice> all() {

        List<IoTDevice> devices = service.getAll();
        devices.sort(Comparator.comparingInt(IoTDevice::getPinCode));
        devices = devices.stream()
                .filter(device -> device.getStatus().equals("ACTIVE"))
                .collect(Collectors.toList());
        return devices;

    }

    @PostMapping("/")
    public IoTDevice create(@RequestBody IoTDevice device) {
        return service.create(device);
    }

    @GetMapping("/{id}")
    public IoTDevice getOne(@PathVariable long id) {
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    public IoTDevice updateOne(@RequestBody IoTDevice newDevice, @PathVariable long id) {
        return service.updateOne(newDevice, id);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable long id) {
        service.deleteOne(id);
    }

    @PostMapping("/configure_device/{id}")
    public IoTDevice configureDevice(@PathVariable Long id) {
        return service.configureDevice(id);
    }


}

