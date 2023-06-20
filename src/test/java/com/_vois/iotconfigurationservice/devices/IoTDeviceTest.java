package com._vois.iotconfigurationservice.devices;

import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IoTDeviceTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link IoTDevice#IoTDevice(String, int, int)}
     *   <li>{@link IoTDevice#toString()}
     * </ul>
     */
    @Test
    void testConstructor() {
        assertEquals("IoTDevice{id=null, status='Status', temp=1, pinCode=1}", (new IoTDevice("Status", 1, 1)).toString());
    }
}

