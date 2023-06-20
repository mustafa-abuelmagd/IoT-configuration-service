package com._vois.iotconfigurationservice.devices.Services;

import com._vois.iotconfigurationservice.devices.Models.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {
}
