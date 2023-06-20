package com._vois.iotconfigurationservice.devices;

import com._vois.iotconfigurationservice.IoTConfigurationServiceApplication;
import com._vois.iotconfigurationservice.devices.Controllers.IoTDeviceController;
import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import com._vois.iotconfigurationservice.devices.Services.IoTDeviceRepository;
import com._vois.iotconfigurationservice.devices.Services.IoTDeviceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;


@DataJpaTest
@ComponentScan(basePackages = "com._vois.iotconfigurationservice.devices;")
@ContextConfiguration(classes = IoTConfigurationServiceApplication.class)
@RunWith(SpringRunner.class)
class IoTDeviceControllerTest {
    @MockBean
    private IoTDeviceRepository repository;

    @Autowired
    private IoTDeviceService deviceService;


    private ClientAndServer mockServer;

    @Test
    void testAll() {

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        ArrayList<IoTDevice> ioTDeviceList = new ArrayList<>();
        when(repository.findAll()).thenReturn(ioTDeviceList);
        List<IoTDevice> actualAllResult = (new IoTDeviceController(new IoTDeviceService(repository))).all();
        assertSame(ioTDeviceList, actualAllResult);
        assertTrue(actualAllResult.isEmpty());
        verify(repository).findAll();
    }

    @Test
    void testAll2() {


        (new IoTDeviceController(null)).all();
    }

    @Test
    void testAll3() {

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        ArrayList<IoTDevice> ioTDeviceList = new ArrayList<>();
        when(ioTDeviceService.getAll()).thenReturn(ioTDeviceList);
        List<IoTDevice> actualAllResult = (new IoTDeviceController(ioTDeviceService)).all();
        assertSame(ioTDeviceList, actualAllResult);
        assertTrue(actualAllResult.isEmpty());
        verify(ioTDeviceService).getAll();
    }

    @BeforeEach
    public void setup() {
        this.deviceService = new IoTDeviceService(this.repository);

        mockServer = startClientAndServer(8080);
    }


    @AfterEach
    void tearDown() {
        mockServer.stop();
    }


    @Test
    void testGetAllEndpoint() {
        new MockServerClient("localhost", 8080)
                .when(request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/api/v1/devices/")
                )
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(String.valueOf(json("[]")), MediaType.APPLICATION_JSON)
                );

        String url = "http://localhost:8080/api/v1/devices/";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals("[]", response.getBody());
    }

    @Test
    void create() {
        new MockServerClient("localhost", 8080)
                .when(request()
                        .withMethod(HttpMethod.POST.name())
                        .withPath("/api/v1/devices/")
                        .withBody("{\"id\":1,\"status\":\"READY\",\"temp\":-1,\"pinCode\":1234567}")
                ).respond(
                        response()
                                .withStatusCode(HttpStatus.CREATED.value())
                                .withBody("{\"id\":1,\"status\":\"READY\",\"temp\":-1,\"pinCode\":1234567}")
                );

        String url = "http://localhost:8080/api/v1/devices/";
        RestTemplate restTemplate = new RestTemplate();
        IoTDevice newDevice = new IoTDevice(1, "READY", -1, 1234567);
        ResponseEntity<String> response = restTemplate.postForEntity(url, newDevice, String.class);

        assertEquals("{\"id\":1,\"status\":\"READY\",\"temp\":-1,\"pinCode\":1234567}", response.getBody());


    }

    @Test
    void getOne() {
        new MockServerClient("localhost", 8080)
                .when(request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/api/v1/devices/1")

                ).respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody("{\"id\":1,\"status\":\"READY\",\"temp\":-1,\"pinCode\":1234567}")
                );
        String url = "http://localhost:8080/api/v1/devices/1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals("{\"id\":1,\"status\":\"READY\",\"temp\":-1,\"pinCode\":1234567}", response.getBody());
    }

    @Test
    void updateOne() {
        new MockServerClient("localhost", 8080)
                .when(request()
                        .withMethod(HttpMethod.PUT.name())
                        .withPath("/api/v1/devices/1")
                        .withBody("{\"status\":\"ACTIVE\",\"temp\":10}")
                ).respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody("{\"id\":1,\"status\":\"ACTIVE\",\"temp\":10,\"pinCode\":1234567}")
                );
        String url = "http://localhost:8080/api/v1/devices/1";
        RestTemplate restTemplate = new RestTemplate();

        IoTDevice request = new IoTDevice(1, "READY", -1, 1234567);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.set("method", "PUT");

        HttpEntity<IoTDevice> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        assertEquals("{\"id\":1,\"status\":\"ACTIVE\",\"temp\":10,\"pinCode\":1234567}", response.getBody());

    }

    @Test
    void deleteOne() {
        new MockServerClient("localhost", 8080)
                .when(request()
                        .withMethod(HttpMethod.DELETE.name())
                        .withPath("/api/v1/devices/1")

                ).respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.set("method", "DELETE");

        HttpEntity<IoTDevice> entity = new HttpEntity<>(null, headers);
        String url = "http://localhost:8080/api/v1/devices/1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        assertEquals(null, response.getStatusCode());
    }


    @Test
    void testCreate2() {

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice("ACTIVE", 1, 1234567);

        when(ioTDeviceService.create(Mockito.<IoTDevice>any())).thenReturn(ioTDevice);
        IoTDeviceController ioTDeviceController = new IoTDeviceController(ioTDeviceService);
        assertSame(ioTDevice, ioTDeviceController.create(new IoTDevice("ACTIVE", 1, 1234567)));
        verify(ioTDeviceService).create(Mockito.<IoTDevice>any());
    }

    @Test
    void testGetOne() {

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        IoTDevice ioTDevice = new IoTDevice("ACTIVE", 1, 1234567);

        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.of(ioTDevice));
        assertSame(ioTDevice, (new IoTDeviceController(new IoTDeviceService(repository))).getOne(1L));
        verify(repository, atLeast(1)).findById(Mockito.<Long>any());
    }


    @Test
    void testGetOne3() {

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice(1L, "ACTIVE", 1, 1234567);

        when(ioTDeviceService.getOne(Mockito.<Long>any())).thenReturn(ioTDevice);
        assertSame(ioTDevice, (new IoTDeviceController(ioTDeviceService)).getOne(1L));
        verify(ioTDeviceService).getOne(Mockito.<Long>any());
    }

    @Test
    void testUpdateOne() {

        IoTDeviceController ioTDeviceController = new IoTDeviceController(
                new IoTDeviceService(mock(IoTDeviceRepository.class)));
        IoTDevice deviceBefore = new IoTDevice(1l, "READY", -1, 1234567);

        when(ioTDeviceController.create(deviceBefore)).thenReturn(deviceBefore);
        IoTDevice deviceAfter = new IoTDevice(1l, "ACTIVE", 1, 1234567);

        when(ioTDeviceController.updateOne(deviceAfter, deviceBefore.getId())).thenReturn(deviceAfter);

        ioTDeviceController.updateOne(deviceAfter, 1L);
        assertEquals(deviceAfter.getId(), ioTDeviceController.updateOne(deviceAfter, 1L).getId());
        assertEquals(deviceAfter.getStatus(), ioTDeviceController.updateOne(deviceAfter, 1L).getStatus());
        assertEquals(deviceAfter.getTemp(), ioTDeviceController.updateOne(deviceAfter, 1L).getTemp());
    }

    @Test
    void testUpdateOne2() {

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice("ACTIVE", 1, 1234567);

        when(ioTDeviceService.updateOne(Mockito.<IoTDevice>any(), Mockito.<Long>any())).thenReturn(ioTDevice);
        IoTDeviceController ioTDeviceController = new IoTDeviceController(ioTDeviceService);
        assertSame(ioTDevice, ioTDeviceController.updateOne(new IoTDevice("ACTIVE", 1, 1234567), 1L));
        verify(ioTDeviceService).updateOne(Mockito.<IoTDevice>any(), Mockito.<Long>any());
    }


    @Test
    void testDeleteOne3() {

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        doNothing().when(ioTDeviceService).deleteOne(Mockito.<Long>any());

        (new IoTDeviceController(ioTDeviceService)).deleteOne(1L);
        verify(ioTDeviceService).deleteOne(Mockito.<Long>any());
    }


    @Test
    void testConfigureDevice2() {

        ArrayList<IoTDevice> ioTDeviceList = new ArrayList<>();

        IoTDevice ioTDevice = new IoTDevice(1L, "ACTIVE", 1, 1234567);
        ioTDeviceList.add(ioTDevice);

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(ioTDevice));

        IoTDevice actualConfigureDeviceResult = (new IoTDeviceController(new IoTDeviceService(repository)))
                .configureDevice(1L);

        Assertions.assertSame(ioTDevice, actualConfigureDeviceResult);
        Assertions.assertEquals("ACTIVE", actualConfigureDeviceResult.getStatus());
        verify(repository).findById(anyLong());
    }


    @Test
    void testConfigureDevice4() {

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);

        IoTDevice ioTDevice = new IoTDevice(1L, "READY", -1, 1234567);
        IoTDevice resultDevice = new IoTDevice(1L, "ACTIVE", 1, 1234567);

        when(ioTDeviceService.configureDevice(anyLong())).thenReturn(resultDevice);

        Assertions.assertEquals("ACTIVE", (new IoTDeviceController(ioTDeviceService)).configureDevice(ioTDevice.getId()).getStatus());
        verify(ioTDeviceService).configureDevice(anyLong());
    }

    @Test
    void configureDevice() {
        new MockServerClient("localhost", 8080)
                .when(request()
                        .withMethod(HttpMethod.POST.name())
                        .withPath("/api/v1/devices/configure_device/1")
                ).respond(
                        response()
                                .withStatusCode(HttpStatus.CREATED.value())
                                .withBody("{\"id\":1,\"status\":\"ACTIVE\",\"temp\":5,\"pinCode\":1234567}")
                );

        String url = "http://localhost:8080/api/v1/devices/configure_device/1";
        RestTemplate restTemplate = new RestTemplate();
        IoTDevice newDevice = new IoTDevice(1, "READY", -1, 1234567);
        ResponseEntity<String> response = restTemplate.postForEntity(url, newDevice, String.class);

        assertEquals("{\"id\":1,\"status\":\"ACTIVE\",\"temp\":5,\"pinCode\":1234567}", response.getBody());


    }
}