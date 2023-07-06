package iot.algo.course_project;

import iot.algo.course_project.Repositories.WeatherStationCSVRepository;
import iot.algo.course_project.models.ServiceWork;
import iot.algo.course_project.models.WeatherStation;
import iot.algo.course_project.models.WeatherStationLocation;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherStationCSVRepositoryTest {

    private static final String TEST_CSV_FILE_PATH = "test_weather_stations.csv";
    private WeatherStationCSVRepository repository;

    @BeforeEach
    void setUp() {
        repository = new WeatherStationCSVRepository();
        // Set the test CSV file path
        //repository.setCsvFilePath(TEST_CSV_FILE_PATH);
    }

    @AfterEach
    void tearDown() {

        File file = repository.getFileFromResourcesByDateIfNonExistsCreate("test");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void saveWeatherStation_ShouldSaveWeatherStation() {
        // Create a test weather station
        int id = 1;
        WeatherStationLocation location = new WeatherStationLocation("123.456", "Test City", "Test Street");
        String manufacturer = "Test Manufacturer";
        Date installationDate = new Date();
        ServiceWork serviceWork = new ServiceWork("Test Work Description",new Date());
        WeatherStation weatherStation = new WeatherStation(id, location, manufacturer, installationDate, serviceWork);

        // Save the weather station
        WeatherStation savedStation = repository.saveWeatherStation(weatherStation);

        // Verify the saved station is not null
        assertNotNull(savedStation);

        // Verify the saved station has the same attributes
        assertEquals(id, savedStation.getId());
        assertEquals(location, savedStation.getLocation());
        assertEquals(manufacturer, savedStation.getManufacturer());
        assertEquals(installationDate, savedStation.getInstallationDate());
        assertEquals(serviceWork, savedStation.getServiceWorkStatus());
    }


    @Test
    void getWeatherStation_ExistingId_ShouldReturnWeatherStation() {
        // Create a test weather station
        int id = 1;
        WeatherStationLocation location = new WeatherStationLocation("123.456", "Test City", "Test Street");
        String manufacturer = "Test Manufacturer";
        Date installationDate = new Date();
        ServiceWork serviceWork = new ServiceWork("Test Work Description",new Date());
        WeatherStation weatherStation = new WeatherStation(id, location, manufacturer, installationDate, serviceWork);

        repository.saveWeatherStation(weatherStation);

        WeatherStation retrievedStation = repository.getWeatherStation(id);
        assertNotNull(retrievedStation);

        assertEquals(id, retrievedStation.getId());
        assertEquals(location, retrievedStation.getLocation());
        assertEquals(manufacturer, retrievedStation.getManufacturer());
    }

    @Test
    void getWeatherStation_NonExistingId_ShouldReturnNull() {
        WeatherStation retrievedStation = repository.getWeatherStation(100);

        assertNull(retrievedStation);
    }

    @Test
    void getAllWeatherStations_ShouldReturnAllWeatherStations() {

        WeatherStation station1 = new WeatherStation(1, new WeatherStationLocation("123.456", "City 1", "Street 1"), "Manufacturer 1", new Date(), new ServiceWork( "Work 1",new Date()));
        WeatherStation station2 = new WeatherStation(2, new WeatherStationLocation("789.012", "City 2", "Street 2"), "Manufacturer 2", new Date(), new ServiceWork("Work 2", new Date()));

        repository.saveWeatherStation(station1);
        repository.saveWeatherStation(station2);


        List<WeatherStation> weatherStations = repository.getAllWeatherStations();


        assertNotNull(weatherStations);
        System.out.println(station1);
        System.out.println(weatherStations);

        WeatherStation retrievedStation1 = weatherStations.get(0);
        WeatherStation retrievedStation2 = weatherStations.get(1);

        assertEquals(station1.getId(), retrievedStation1.getId());
        assertEquals(station1.getLocation(), retrievedStation1.getLocation());
        assertEquals(station1.getManufacturer(), retrievedStation1.getManufacturer());

        assertEquals(station2.getId(), retrievedStation2.getId());
        assertEquals(station2.getLocation(), retrievedStation2.getLocation());
        assertEquals(station2.getManufacturer(), retrievedStation2.getManufacturer());
    }


    @Test
    void deleteWeatherStation_ExistingId_ShouldDeleteWeatherStation() {

        int id = 1;
        WeatherStationLocation location = new WeatherStationLocation("123.456", "Test City", "Test Street");
        String manufacturer = "Test Manufacturer";
        Date installationDate = new Date();
        ServiceWork serviceWork = new ServiceWork( "Test Work Description",new Date());
        WeatherStation weatherStation = new WeatherStation(id, location, manufacturer, installationDate, serviceWork);

        repository.saveWeatherStation(weatherStation);

        repository.deleteWeatherStation(id);

        WeatherStation deletedStation = repository.getWeatherStation(id);

        assertNull(deletedStation);
    }

    @Test
    void deleteWeatherStation_NonExistingId_ShouldNotThrowException() {
        assertDoesNotThrow(() -> repository.deleteWeatherStation(100));
    }




}