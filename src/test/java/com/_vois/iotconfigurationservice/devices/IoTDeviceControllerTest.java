package com._vois.iotconfigurationservice.devices;

import com._vois.iotconfigurationservice.IoTConfigurationServiceApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    /**
     * Method under test: {@link IoTDeviceController#all()}
     */
    @Test
    void testAll() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        ArrayList<IoTDevice> ioTDeviceList = new ArrayList<>();
        when(repository.findAll()).thenReturn(ioTDeviceList);
        List<IoTDevice> actualAllResult = (new IoTDeviceController(new IoTDeviceService(repository))).all();
        assertSame(ioTDeviceList, actualAllResult);
        assertTrue(actualAllResult.isEmpty());
        verify(repository).findAll();
    }

    /**
     * Method under test: {@link IoTDeviceController#all()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAll2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com._vois.iotconfigurationservice.devices.IoTDeviceService.getAll()" because "this.service" is null
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceController.all(IoTDeviceController.java:20)
        //   See https://diff.blue/R013 to resolve this issue.

        (new IoTDeviceController(null)).all();
    }

    /**
     * Method under test: {@link IoTDeviceController#all()}
     */
    @Test
    void testAll3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

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

    /**
     * Method under test: {@link IoTDeviceController#create(IoTDevice)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreate() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: Provided data is not correct!
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceService.create(IoTDeviceService.java:25)
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceController.create(IoTDeviceController.java:28)
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceController ioTDeviceController = new IoTDeviceController(
                new IoTDeviceService(mock(IoTDeviceRepository.class)));
        ioTDeviceController.create(new IoTDevice("Status", 1, 1));
    }

    /**
     * Method under test: {@link IoTDeviceController#create(IoTDevice)}
     */
    @Test
    void testCreate2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(ioTDeviceService.create(Mockito.<IoTDevice>any())).thenReturn(ioTDevice);
        IoTDeviceController ioTDeviceController = new IoTDeviceController(ioTDeviceService);
        assertSame(ioTDevice, ioTDeviceController.create(new IoTDevice("Status", 1, 1)));
        verify(ioTDeviceService).create(Mockito.<IoTDevice>any());
    }

    /**
     * Method under test: {@link IoTDeviceController#getOne(long)}
     */
    @Test
    void testGetOne() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.of(ioTDevice));
        assertSame(ioTDevice, (new IoTDeviceController(new IoTDeviceService(repository))).getOne(1L));
        verify(repository, atLeast(1)).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IoTDeviceController#getOne(long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetOne2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: No device exists with this ID
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceService.getOne(IoTDeviceService.java:33)
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceController.getOne(IoTDeviceController.java:33)
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        when(repository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        (new IoTDeviceController(new IoTDeviceService(repository))).getOne(1L);
    }

    /**
     * Method under test: {@link IoTDeviceController#getOne(long)}
     */
    @Test
    void testGetOne3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(ioTDeviceService.getOne(Mockito.<Long>any())).thenReturn(ioTDevice);
        assertSame(ioTDevice, (new IoTDeviceController(ioTDeviceService)).getOne(1L));
        verify(ioTDeviceService).getOne(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IoTDeviceController#updateOne(IoTDevice, long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateOne() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: Provided data update is not valid!
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceService.updateOne(IoTDeviceService.java:41)
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceController.updateOne(IoTDeviceController.java:38)
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceController ioTDeviceController = new IoTDeviceController(
                new IoTDeviceService(mock(IoTDeviceRepository.class)));
        ioTDeviceController.updateOne(new IoTDevice("Status", 1, 1), 1L);
    }

    /**
     * Method under test: {@link IoTDeviceController#updateOne(IoTDevice, long)}
     */
    @Test
    void testUpdateOne2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(ioTDeviceService.updateOne(Mockito.<IoTDevice>any(), Mockito.<Long>any())).thenReturn(ioTDevice);
        IoTDeviceController ioTDeviceController = new IoTDeviceController(ioTDeviceService);
        assertSame(ioTDevice, ioTDeviceController.updateOne(new IoTDevice("Status", 1, 1), 1L));
        verify(ioTDeviceService).updateOne(Mockito.<IoTDevice>any(), Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IoTDeviceController#deleteOne(long)}
     */
    @Test
    void testDeleteOne() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        doNothing().when(repository).deleteById(Mockito.<Long>any());
        (new IoTDeviceController(new IoTDeviceService(repository))).deleteOne(1L);
        verify(repository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IoTDeviceController#deleteOne(long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDeleteOne2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com._vois.iotconfigurationservice.devices.IoTDeviceService.deleteOne(java.lang.Long)" because "this.service" is null
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceController.deleteOne(IoTDeviceController.java:43)
        //   See https://diff.blue/R013 to resolve this issue.

        (new IoTDeviceController(null)).deleteOne(1L);
    }

    /**
     * Method under test: {@link IoTDeviceController#deleteOne(long)}
     */
    @Test
    void testDeleteOne3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        doNothing().when(ioTDeviceService).deleteOne(Mockito.<Long>any());
        (new IoTDeviceController(ioTDeviceService)).deleteOne(1L);
        verify(ioTDeviceService).deleteOne(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link IoTDeviceController#configureDevice(int)}
     */
    @Test
    void testConfigureDevice() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(Mockito.<IoTDevice>any()));
        assertNull((new IoTDeviceController(new IoTDeviceService(repository))).configureDevice(1));
        verify(repository).findById(anyLong());
    }

    @Test
    void testConfigureDevice2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        ArrayList<IoTDevice> ioTDeviceList = new ArrayList<>();
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        ioTDeviceList.add(ioTDevice);
        IoTDeviceRepository repository = mock(IoTDeviceRepository.class);
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(Mockito.<IoTDevice>any()));
        IoTDevice actualConfigureDeviceResult = (new IoTDeviceController(new IoTDeviceService(repository)))
                .configureDevice(1);
        assertSame(ioTDevice, actualConfigureDeviceResult);
        org.junit.jupiter.api.Assertions.assertEquals("ACTIVE", actualConfigureDeviceResult.getStatus());
        verify(repository).findById(anyLong());
    }


    @Test
    @Disabled("TODO: Complete this test")
    void testConfigureDevice3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com._vois.iotconfigurationservice.devices.IoTDeviceService.configureDevice(int)" because "this.service" is null
        //       at com._vois.iotconfigurationservice.devices.IoTDeviceController.configureDevice(IoTDeviceController.java:48)
        //   See https://diff.blue/R013 to resolve this issue.

        (new IoTDeviceController(null)).configureDevice(1);
    }

    @Test
    void testConfigureDevice4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "bean" is null
        //   See https://diff.blue/R013 to resolve this issue.

        IoTDeviceService ioTDeviceService = mock(IoTDeviceService.class);
        IoTDevice ioTDevice = new IoTDevice("Status", 1, 1);

        when(ioTDeviceService.configureDevice(anyInt())).thenReturn(ioTDevice);
        assertSame(ioTDevice, (new IoTDeviceController(ioTDeviceService)).configureDevice(1));
        verify(ioTDeviceService).configureDevice(anyInt());
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