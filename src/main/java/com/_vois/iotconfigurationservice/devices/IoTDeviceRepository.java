package com._vois.iotconfigurationservice.devices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {
    @Query("SELECT d FROM IoTDevice d WHERE d.pinCode = :pinCode")
    List<IoTDevice> findByPinCode(@Param("pinCode") String pinCode);
}
