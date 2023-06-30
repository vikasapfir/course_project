package iot.algo.course_project.Controllers;

import iot.algo.course_project.models.WeatherStation;
import iot.algo.course_project.Services.WeatherStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weather-stations")
public class WeatherStationController {

    private final WeatherStationService weatherStationService;

    @Autowired
    public WeatherStationController(WeatherStationService weatherStationService) {
        this.weatherStationService = weatherStationService;
    }

    @GetMapping("/{id}")
    public WeatherStation getWeatherStationById(@PathVariable int id) {
        return weatherStationService.getWeatherStationById(id);
    }

    @GetMapping("/today")
    public List<WeatherStation> getAllWeatherStations() {
        return weatherStationService.getAllWeatherStations();
    }

    @PostMapping("/")
    public WeatherStation saveWeatherStation(@RequestBody WeatherStation weatherStation) {
        return weatherStationService.saveWeatherStation(weatherStation);
    }

    @DeleteMapping("/{id}")
    public void deleteWeatherStation(@PathVariable int id) {
        weatherStationService.deleteWeatherStation(id);
    }

    @GetMapping("/monthly")
    public Map<Integer, WeatherStation> getMonthlyWeatherStationsMap() {
        return weatherStationService.getMonthlyWeatherStationsMap();
    }


}