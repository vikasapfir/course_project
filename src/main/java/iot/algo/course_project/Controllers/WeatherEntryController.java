package iot.algo.course_project.Controllers;

import iot.algo.course_project.models.WeatherStation;
import iot.algo.course_project.models.WeatherStationEntry;
import iot.algo.course_project.Services.WeatherEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/entries")
public class WeatherEntryController {

    private final WeatherEntryService weatherEntryService;

    @Autowired
    public WeatherEntryController(WeatherEntryService weatherEntryService) {
        this.weatherEntryService = weatherEntryService;
    }

    @GetMapping("/today")
    public List<WeatherStationEntry> getAllEntries() {
        return weatherEntryService.getAllEntries();
    }

    @GetMapping("/station/{weatherStationId}")
    public List<WeatherStationEntry> getEntriesByWeatherStationId(@PathVariable int weatherStationId) {
        return weatherEntryService.getEntriesByWeatherStationId(weatherStationId);
    }

    @GetMapping("/{entryId}")
    public WeatherStationEntry getEntryById(@PathVariable int entryId) {
        return weatherEntryService.getEntryById(entryId);
    }

    @GetMapping("/monthly")
    public Map<Integer, WeatherStationEntry> getMonthlyEntries() {
        return weatherEntryService.getMonthlyEntries();
    }
}