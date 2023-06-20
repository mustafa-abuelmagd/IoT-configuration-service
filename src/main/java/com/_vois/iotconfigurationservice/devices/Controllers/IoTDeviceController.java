package com._vois.iotconfigurationservice.devices.Controllers;

import com._vois.iotconfigurationservice.devices.DTO.DeviceResponse;
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
    public ResponseEntity create(@RequestBody IoTDevice device) {
        try {
            DeviceResponse response = new DeviceResponse();
            response.setContent(service.create(device));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            return handleBadRequest(new BadRequestException("Provided data is not correct!"));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable long id) {
        try {
            DeviceResponse response = new DeviceResponse();
            response.setContent(service.getOne(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            return handleBadRequest(new BadRequestException("Provided data is not correct!"));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity updateOne(@RequestBody IoTDevice newDevice, @PathVariable long id) {
        try {
            DeviceResponse response = new DeviceResponse();
            response.setContent(service.updateOne(newDevice, id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            return handleBadRequest(new BadRequestException("Provided data is not correct!"));
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable long id) {
        service.deleteOne(id);
    }

    @PostMapping("/configure_device/{id}")
    public ResponseEntity configureDevice(@PathVariable Long id) {
        try {
            DeviceResponse response = new DeviceResponse();
            response.setContent(service.configureDevice(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            return handleBadRequest(new BadRequestException("Provided data is not correct!"));
        }
    }


}

