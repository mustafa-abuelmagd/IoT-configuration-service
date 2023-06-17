package com._vois.iotconfigurationservice.devices;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "IoTDevice")
@Table(
        name = "iotDevice",
        uniqueConstraints = {
                @UniqueConstraint(name="device_unique_pin", columnNames = "pin_number")
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
            name = "pin_number",
            nullable = false,
//            unique = true,
            columnDefinition = "VARCHAR(7)"
    )
    private long pin_number;

    public IoTDevice() {}

    public IoTDevice(String status, int temp, long pin_number) {

        this.status = status;
        this.temp = temp;
        this.pin_number = pin_number;
    }

    public IoTDevice(Long id, String status, int temp, long pin_number) {

        this.id = id;
        this.status = status;
        this.temp = temp;
        this.pin_number = pin_number;
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
        temp = temp;
    }

    public long getPin_number() {
        return pin_number;
    }

    public void setPin_number(long pin_number) {
        this.pin_number = pin_number;
    }

    @Override
    public String toString() {
        return "IoTDevice{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", temp=" + temp +
                ", pin_number=" + pin_number +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IoTDevice ioTDevice = (IoTDevice) o;
        return temp == ioTDevice.temp && pin_number == ioTDevice.pin_number && Objects.equals(id, ioTDevice.id) && Objects.equals(status, ioTDevice.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, temp, pin_number);
    }





}
