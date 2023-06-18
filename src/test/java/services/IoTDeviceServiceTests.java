package services;

import com._vois.iotconfigurationservice.devices.IoTDevice;
import com._vois.iotconfigurationservice.devices.IoTDeviceRepository;
import com._vois.iotconfigurationservice.devices.IoTDeviceService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class IoTDeviceServiceTests {
    @Mock
    private IoTDeviceRepository repository;
    private IoTDeviceService deviceService;


    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        deviceService = new IoTDeviceService(repository);
    }

    @Test
    void testGetAll(){
        ArrayList<IoTDevice> devices = new ArrayList<>();
        devices.add(new IoTDevice("READY", 5, 1234567));

        Mockito.when(repository.findAll()).thenReturn(devices);
        List<IoTDevice> result = deviceService.getAll();


    }

}
