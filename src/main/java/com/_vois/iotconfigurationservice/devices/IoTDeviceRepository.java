package com._vois.iotconfigurationservice.devices;

import org.springframework.data.jpa.repository.JpaRepository;

interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {
}
