package com._vois.iotconfigurationservice.services;

import com._vois.iotconfigurationservice.IoTConfigurationServiceApplication;
import com._vois.iotconfigurationservice.devices.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//@SpringBootTest(classes = IoTConfigurationServiceApplication.class)
@DataJpaTest
@ComponentScan(basePackages = "com._vois.iotconfigurationservice.devices;")
@ContextConfiguration(classes = IoTConfigurationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
class IoTDeviceServiceTests {
    @MockBean
    private IoTDeviceRepository repository;

    @Autowired
    private IoTDeviceService deviceService;


    @BeforeEach
    void setup() {
//        MockitoAnnotations.openMocks(this);
        this.deviceService = new IoTDeviceService(this.repository);
    }

    // Test multiple variations of device parameters at once instead of creating multiple tests
    @ParameterizedTest
    @CsvSource({
            "ACTIVE,8,1234567,true",
            "ACTIVE,-1,1234567,false",
            "ACTIVE,1,12345678,false",
            "ACTIVE,1,123456,false",
            "READY,1,1234567,false",
            "READY,1,12345678,false",
            "READY,1,1234567,false",
            "Device Status,1,1,false",
            "ACTIVE,1,1,false",
            "READY,1,1,false",
            "ACTIVE,-1,1,false",
            "READY,-1,1,false"
    })
    void testDeviceDataValidation(String deviceStatus,
                                  int deviceTemp,
                                  int devicePinCode,
                                  boolean expected) {
        boolean validationStatus = this.deviceService.isValidDeviceData(deviceStatus,
                deviceTemp, devicePinCode);

        Assertions.assertEquals(validationStatus, expected);
    }


    @ParameterizedTest
    @CsvSource({
            "ACTIVE,-1,1234567",
            "ACTIVE,1,12345678",
            "ACTIVE,1,123456",
            "READY,1,1234567",
            "READY,1,12345678",
            "READY,1,1234567",
            "Device Status,1,1",
            "ACTIVE,1,1",
            "READY,1,1",
            "ACTIVE,-1,1",
            "READY,-1,1"
    })
//    @Disabled
    void testCreateInvalidDeviceData(String deviceStatus,
                                     int deviceTemp,
                                     int devicePinCode) {
        IoTDevice newDevice = new IoTDevice(deviceStatus, deviceTemp, devicePinCode);
        Assertions.assertThrows(BadRequestException.class, () -> this.deviceService.create(newDevice));

    }

    @Test
//    @Disabled
    void testCreateInvalidDevice() {
        IoTDevice invalidDevice = new IoTDevice();
        invalidDevice.setPinCode(12345678);
        Assertions.assertThrows(BadRequestException.class, () -> this.deviceService.create(invalidDevice));

    }

    @Test
//    @Disabled
    void testCreateValidDevice() {
        IoTDevice newDevice = new IoTDevice("ACTIVE", 8, 1234567);
        Mockito.when(repository.save(Mockito.any(IoTDevice.class))).thenReturn(newDevice);

        IoTDevice result = this.deviceService.create(newDevice);
        Assertions.assertEquals(newDevice, result);
    }


    @Test
//    @Disabled
    void testGetAll() {
        ArrayList<IoTDevice> devices = new ArrayList<>();
        devices.add(new IoTDevice("READY", 5, 1234567));

        Mockito.when(repository.findAll()).thenReturn(devices);
        List<IoTDevice> result = this.deviceService.getAll();
        Assertions.assertEquals(devices, result);
    }

    @Test
//    @Disabled
    void testGetAll2() {
        ArrayList<IoTDevice> deviceList = new ArrayList<>();
        when(repository.findAll()).thenReturn(deviceList);
        List<IoTDevice> actualAll = this.deviceService.getAll();
        Assertions.assertSame(deviceList, actualAll);
        Assertions.assertTrue(actualAll.isEmpty());
        verify(repository).findAll();
    }

    @Test
//    @Disabled
    void testGetAll3() {
        List<IoTDevice> devices = new ArrayList<>();
        IoTDevice device = mock(IoTDevice.class);
        devices.add(device);
        when(repository.findAll()).thenReturn(devices);
        Assertions.assertNotNull(this.deviceService.getAll());
        verify(repository).findAll();
    }

    @Test
//    @Disabled
    void testCreate() {
        IoTDevice newDevice = new IoTDevice("Status", 1, 1);
        Assertions.assertThrows(BadRequestException.class, () -> this.deviceService.create(newDevice));
    }


    @Test
//    @Disabled
    void testGetOne() {
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.of(ioTDevice));
        Assertions.assertSame(ioTDevice, this.deviceService.getOne(1L));
        verify(repository, atLeast(1)).findById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testGetOne2() {
        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        Assertions.assertThrows(DeviceNotFoundException.class, () -> this.deviceService.getOne(1L));
        verify(repository).findById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testGetOne3() {
        when(repository.findById(Mockito.<Long>any()))
                .thenThrow(new IllegalStateException("No device exists with this ID"));
        Assertions.assertThrows(IllegalStateException.class, () -> this.deviceService.getOne(1L));
        verify(repository).findById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testUpdateOne() {
        IoTDevice newDevice = new IoTDevice("Status", 1, 1);
        Assertions.assertThrows(BadRequestException.class, () -> this.deviceService.updateOne(newDevice, 1L));
    }

    @Test
//    @Disabled
    void testUpdateOne2() {
        when(repository.save(Mockito.<IoTDevice>any())).thenReturn(new IoTDevice("Status", 1, 1));
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.of(ioTDevice));
        IoTDeviceService ioTDeviceService = new IoTDeviceService(repository);
        IoTDevice actualUpdateOneResult = ioTDeviceService.updateOne(new IoTDevice("ACTIVE", 1, 1), 1L);
        assertSame(ioTDevice, actualUpdateOneResult);
        assertEquals(1, actualUpdateOneResult.getTemp());
        assertEquals("ACTIVE", actualUpdateOneResult.getStatus());
        verify(repository).findById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testUpdateOne3() {
        IoTDevice ioTDevice = mock(IoTDevice.class);

//        doThrow(new DeviceNotFoundException(1L)).when(ioTDevice).setStatus(Mockito.<String>any());
//        doThrow(new DeviceNotFoundException(1L)).when(ioTDevice).setTemp(anyInt());

        IoTDevice ofResult = new IoTDevice("Status", 1, 1);

        when(repository.save(Mockito.<IoTDevice>any())).thenReturn(ofResult);
        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.of(new IoTDevice("Status", 1, 1)));

        IoTDeviceService ioTDeviceService = new IoTDeviceService(repository);
        IoTDevice newDevice = new IoTDevice("Status", 1, 1);

        Assertions.assertNotNull(ioTDeviceService.updateOne(newDevice, 1L));
        verify(repository).findById(Mockito.<Long>any());
//        verify(ioTDevice).setStatus(Mockito.<String>any());
//        verify(ioTDevice).setTemp(Mockito.<Integer>any());
    }

    @Test
//    @Disabled
    void testUpdateOne4() {
        new DeviceNotFoundException(1L);
        new DeviceNotFoundException(1L);
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(repository.save(Mockito.<IoTDevice>any())).thenReturn(ioTDevice);
        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        IoTDeviceService ioTDeviceService = new IoTDeviceService(repository);
        IoTDevice newDevice = new IoTDevice("ACTIVE", 1, 1);

        assertSame(ioTDevice, ioTDeviceService.updateOne(newDevice, 1L));
        verify(repository).save(Mockito.<IoTDevice>any());
        verify(repository).findById(Mockito.<Long>any());
        assertEquals(1L, newDevice.getId());
    }



    @Test
//    @Disabled
    void testDeleteOne() {
        doNothing().when(repository).deleteById(Mockito.<Long>any());
        this.deviceService.deleteOne(1L);
        verify(repository).deleteById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testDeleteOne2() {
        doThrow(new IllegalStateException("foo")).when(repository).deleteById(Mockito.<Long>any());
        Assertions.assertThrows(IllegalStateException.class, () -> this.deviceService.deleteOne(1L));
        verify(repository).deleteById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testConfigureDevice1() {
        when(repository.findById(Mockito.<Long>any())).thenThrow(new IllegalStateException("No device with this id"));
        Assertions.assertThrows(IllegalStateException.class, () -> this.deviceService.configureDevice(15000));
        verify(repository).findById(Mockito.<Long>any());
    }

    @Test
//    @Disabled
    void testConfigureDevice2() {
        when(repository.findById(Mockito.<Long>any())).thenThrow(new IllegalStateException("foo"));
        Assertions.assertThrows(IllegalStateException.class, () -> this.deviceService.configureDevice(1));
        verify(repository).findById(Mockito.<Long>any());
    }


    @Test
//    @Disabled
    void testSetDeviceStateAndTemp() {
        IoTDevice newDevice = new IoTDevice("READY", -1, 1357924);

        newDevice.setStatus("ACTIVE");
        newDevice.setTemp(this.deviceService.generateTemp());

        boolean isWithinConditions = 0 <= newDevice.getTemp() && newDevice.getTemp() <= 10;

        Assertions.assertEquals("ACTIVE", newDevice.getStatus());
        Assertions.assertTrue(isWithinConditions);
        Assertions.assertEquals(1357924, newDevice.getPinCode());

    }

    @Test
//    @Disabled
    void testGenerateTemp() {
        int generatedTemp = deviceService.generateTemp();
        boolean isWithinConditions = 0 <= generatedTemp && generatedTemp <= 10;
        Assertions.assertTrue(isWithinConditions);
    }


    @ParameterizedTest
    @CsvSource({
            "1234567,true",
            "12345678,false",
            "123456789,false",
            "12345,false",
    })
//    @Disabled
    void testIsValidPinCode(int pinCode, boolean expected) {
        Assertions.assertEquals(expected, this.deviceService.isValidPinCode(pinCode));
    }


}
