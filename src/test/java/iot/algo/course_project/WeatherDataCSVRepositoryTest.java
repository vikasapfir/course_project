package iot.algo.course_project;

import iot.algo.course_project.Repositories.WeatherDataCSVRepository;
import iot.algo.course_project.models.WeatherStationEntry;
import iot.algo.course_project.models.WindParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class WeatherDataCSVRepositoryTest {
    private WeatherDataCSVRepository weatherDataRepository;

    @BeforeEach
    public void setup() {
        weatherDataRepository = new WeatherDataCSVRepository();
    }

    @Test
    public void testAddOrUpdateWeatherData() {
        // Create a new weather data entry
        WeatherStationEntry entry = new WeatherStationEntry();
        entry.setId(1);
        entry.setDatetime(new Date());
        entry.setTemperature(25.5);
        entry.setHumidity(50.2);
        WindParameter windParameter = new WindParameter();
        windParameter.setSpeed(10.0);
        windParameter.setAtmospherePressure(1000.0);
        windParameter.setDirection("N");
        entry.setWindParameter(windParameter);
        entry.setWeatherStationId(123);

        // Add the weather data entry
        weatherDataRepository.saveWeatherEntry(entry);

        // Retrieve the weather data entry
        WeatherStationEntry retrievedEntry = weatherDataRepository.getWeatherDataById(1);

        // Check if the retrieved entry is equal to the original entry
        Assertions.assertEquals(entry, retrievedEntry);
    }

    @Test
    public void testGetWeatherDataById() {
        // Create a new weather data entry
        WeatherStationEntry entry = new WeatherStationEntry();
        entry.setId(2);
        entry.setDatetime(new Date());
        entry.setTemperature(28.0);
        entry.setHumidity(60.0);
        WindParameter windParameter = new WindParameter();
        windParameter.setSpeed(15.0);
        windParameter.setAtmospherePressure(1010.0);
        windParameter.setDirection("E");
        entry.setWindParameter(windParameter);
        entry.setWeatherStationId(456);

        // Add the weather data entry
        weatherDataRepository.saveWeatherEntry(entry);
        Assertions.assertTrue(weatherDataRepository.getWeatherDataMap().size() > 0);
        // Retrieve the weather data entry by ID
        WeatherStationEntry retrievedEntry = weatherDataRepository.getWeatherDataById(2);

        // Check if the retrieved entry is equal to the original entry
        Assertions.assertEquals(entry, retrievedEntry);
    }
}