package com._vois.iotconfigurationservice.devices;

import com._vois.iotconfigurationservice.IoTConfigurationServiceApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

import static org.junit.Assert.assertEquals;
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
        String url =  "http://localhost:8080/api/v1/devices/1";
        RestTemplate restTemplate = new RestTemplate();

        IoTDevice request = new IoTDevice(1, "READY", -1 , 1234567);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( org.springframework.http.MediaType.APPLICATION_JSON);
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
        headers.setContentType( org.springframework.http.MediaType.APPLICATION_JSON);
        headers.set("method", "DELETE");

        HttpEntity<IoTDevice> entity = new HttpEntity<>(null, headers);
        String url = "http://localhost:8080/api/v1/devices/1";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url,  entity, String.class);

        assertEquals(null, response.getStatusCode());
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