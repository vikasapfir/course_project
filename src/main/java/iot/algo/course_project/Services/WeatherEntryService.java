package iot.algo.course_project.Services;

import iot.algo.course_project.models.WeatherStationEntry;
import iot.algo.course_project.Repositories.WeatherDataCSVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WeatherEntryService {

    private final WeatherDataCSVRepository weatherDataCSVRepository;

    @Autowired
    public WeatherEntryService(WeatherDataCSVRepository weatherDataCSVRepository) {
        this.weatherDataCSVRepository = weatherDataCSVRepository;
    }

    public List<WeatherStationEntry> getAllEntries() {
        return weatherDataCSVRepository.getAllWeatherData();
    }

    public List<WeatherStationEntry> getEntriesByWeatherStationId(int weatherStationId) {
        return weatherDataCSVRepository.getWeatherDataByWeatherStationId(weatherStationId);
    }

    public WeatherStationEntry getEntryById(int entryId) {
        return weatherDataCSVRepository.getWeatherDataById(entryId);
    }

    public Map<Integer, WeatherStationEntry> getMonthlyEntries() {
        return weatherDataCSVRepository.getWeatherDataMap();
    }
}