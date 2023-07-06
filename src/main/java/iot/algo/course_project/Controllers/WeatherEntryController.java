package iot.algo.course_project.Controllers;

import iot.algo.course_project.models.WeatherStation;
import iot.algo.course_project.models.WeatherStationEntry;
import iot.algo.course_project.Services.WeatherEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public WeatherStationEntry saveEntry(@RequestBody WeatherStationEntry weatherStationEntry) {
        return weatherEntryService.save(weatherStationEntry);
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable int id) {
        weatherEntryService.delete(id);
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