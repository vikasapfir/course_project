package iot.algo.course_project.Services;

import iot.algo.course_project.Repositories.WeatherStationCSVRepository;
import iot.algo.course_project.models.WeatherStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WeatherStationService {

    private final WeatherStationCSVRepository weatherStationRepository;

    @Autowired
    public WeatherStationService(WeatherStationCSVRepository weatherStationRepository) {
        this.weatherStationRepository = weatherStationRepository;
    }

    public WeatherStation getWeatherStationById(int id) {
        return weatherStationRepository.getWeatherStation(id);
    }

    public List<WeatherStation> getAllWeatherStations() {
        return weatherStationRepository.getAllWeatherStations();
    }

    public WeatherStation saveWeatherStation(WeatherStation weatherStation) {
        return weatherStationRepository.saveWeatherStation(weatherStation);
    }

    public void deleteWeatherStation(int id) {
        weatherStationRepository.deleteWeatherStation(id);
    }

    public Map<Integer, WeatherStation> getMonthlyWeatherStationsMap() {
        return weatherStationRepository.getMonthlyWeatherStationsMap();
    }

}