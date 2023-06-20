package com._vois.iotconfigurationservice.devices.DTO;


import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListResponse {
    private List<IoTDevice> content;
    private long totalElements;
}
