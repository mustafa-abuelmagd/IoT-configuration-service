package com._vois.iotconfigurationservice.devices;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "IoTDevice")
@Table(
        name = "iotDevice",
        uniqueConstraints = {
                @UniqueConstraint(name="device_unique_pin", columnNames = "pinCode")
        }
)
public class IoTDevice {
    private @Id
            @SequenceGenerator(
                    name = "device_sequence",
                    sequenceName = "device_sequence",
                    allocationSize = 1
            )
            @GeneratedValue(
                    strategy = GenerationType.SEQUENCE,
                    generator = "student_sequence"
            )
    Long id;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "ENUM('ACTIVE', 'READY')"
    )
    private String status;

    @Column(
            name = "temp",
            columnDefinition = "DECIMAL",
            nullable = false
    )
    private int temp;

    @Column(
            name = "pinCode",
            nullable = false,
            columnDefinition = "VARCHAR(7)",
            updatable = false
    )
    private long pinCode;

    public IoTDevice() {}

    public IoTDevice(String status, int temp, long pinCode) {

        this.status = status;
        this.temp = temp;
        this.pinCode = pinCode;
    }

    public IoTDevice(Long id, String status, int temp, long pinCode) {

        this.id = id;
        this.status = status;
        this.temp = temp;
        this.pinCode = pinCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public long getPinCode() {
        return pinCode;
    }

    public void setPinCode(long pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return "IoTDevice{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", temp=" + temp +
                ", pinCode=" + pinCode +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IoTDevice ioTDevice = (IoTDevice) o;
        return temp == ioTDevice.temp && pinCode == ioTDevice.pinCode && Objects.equals(id, ioTDevice.id) && Objects.equals(status, ioTDevice.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, temp, pinCode);
    }





}
