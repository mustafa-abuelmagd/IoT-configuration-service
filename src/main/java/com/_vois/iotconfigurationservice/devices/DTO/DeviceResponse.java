package com._vois.iotconfigurationservice.devices.DTO;

import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponse {
    private IoTDevice content;

    public IoTDevice getContent() {
        return content;
    }
}


