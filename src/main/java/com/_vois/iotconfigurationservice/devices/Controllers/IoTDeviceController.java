package com._vois.iotconfigurationservice.devices.Controllers;

import com._vois.iotconfigurationservice.devices.DTO.ListResponse;
import com._vois.iotconfigurationservice.devices.Errors.BadRequestException;
import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import com._vois.iotconfigurationservice.devices.Services.IoTDeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com._vois.iotconfigurationservice.devices.Errors.ErrorHandler.handleBadRequest;

@RestController
@RequestMapping("/api/v1/devices")
public class IoTDeviceController {
    private final IoTDeviceService service;

    public IoTDeviceController(IoTDeviceService ioTDeviceService) {
        this.service = ioTDeviceService;
    }

    @GetMapping("/")
    public ResponseEntity all() {
        try {
            List<IoTDevice> devices = service.getAll();
            devices.sort(Comparator.comparingInt(IoTDevice::getPinCode));
            devices = devices.stream()
                    .filter(device -> device.getStatus().equals("ACTIVE"))
                    .collect(Collectors.toList());
            ListResponse listResponse = new ListResponse();
            listResponse.setContent(devices);
            listResponse.setTotalElements(devices.size());
            return new ResponseEntity<>(listResponse, HttpStatus.OK);


        } catch (Exception exception) {
            return handleBadRequest(new BadRequestException("Provided data is not correct!"));
        }

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

