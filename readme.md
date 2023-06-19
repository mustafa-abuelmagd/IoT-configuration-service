# IoT Software Engineering Java Interview Question

## Task Description

A shop in London has 2 million IoT tracking devices in the Warehouse Inventory for sale,
of which half need configuration to meet UK industry standards.

A configured device will have a status ACTIVE and an ideal temperature between (0’C to
10’C).

When a device is not configured, the default status is READY and temperature value is -
1’C.

Every device has a unique secret seven-digit pin code used for unlocking the device.

A given device needs to be sent to a Device Configuration Service (DCS) to set the device
status ACTIVE and random temperature value between (0 to 10).

The Device Configuration Service does not need a device pin code for the configuration
operation.

The shop can sell a device only if it meets the UK government's industry standard.

### Installation

Clone the repository:

```
git clone https://github.com/mustafa-abuelmagd/IoT-configuration-service
```

## About The App

1. **REST API for the Warehouse Inventory**:
    - Add, update, or remove a device
    - Return all devices available for sale in numerical order of their seven-digit
      pin code.

2. **Device Configuration Service**:
    - An endpoint endpoint responsible for configuring a device.

3. **Device Configuration Service**:
    - All database repositories.
    - All communicating API and endpoints.

## API Endpoints

The following API endpoints are available:

- `GET /api/v1/devices/`: Retrieve list of the available devices ordered by their numerical order of their seven-digit
  pin code.

- `POST /api/v1/devices/`: Creates a new IoTDevice (provided its pinCode) and saves it to the database.
- `PUT /api/v1/devices/{id}`: Updates a devices provided its id (pinCodes cannot be updated).
- `DELETE /api/v1/devices/{id}`: Deletes a device by its id.
- `GET /api/v1/devices/{id}`: Retrieve device by its id.

- `POST /api/v1/devices/configure_device/{id}`: Sends a device to the configuration service to be configured.

## Used tools

- **Sonarlint**: A static code analyser to check for code qualities.

- **Mockservers**: For simulating external APIs and API calls.

- **H2 in-memory database**: An in-memory database for the purpose of testing without affecting real data.

- **MySQL persistence database**: Application relational database

- **Intellij Test coverage tool**: A test coverage tool to ensure that test coverage for Lines, Methods, and Branches are not less than 80% coverage.


## Running App

Install all the gradle dependencies in the build.gradle file:

### Create IoTDevices Database

To create the application database you'll need to perform the following bash commands:
- Ensure that MySQL database is installed in the system using the command
    ```
      mysql -V
    ```
- If the previous command outputs the database version, then proceed to creating the application database using the command 
    - Start mysql service 
        ```
        sudo systemctl start mysql.service
        ```
    - Login to the database server using the username and password
        ```
            mysql -u <username> -p
        ```
    - Create database 
        ```
            CREATE DATABASE IoTDevices;
        ```



### Running Application in test mode

- The src/test directory provides an application.properties file where I've configured an in-memory database for the
  testing events.
- Test files can be found in src/test/com._vois.iotconfigurationservice/devices/IoTDeviceControllerTest.java and
  src/test/com._vois.iotconfigurationservice/services/IoTDeviceServiceTest.java
- Running the testing files from intellij is fairly easy; the application provides a clear button for running the test
  class.

### Running the Application

- #### Running the application involves running the IoTConfigurationServiceApplication class, same as before.

