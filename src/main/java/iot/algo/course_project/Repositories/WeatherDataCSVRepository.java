package iot.algo.course_project.Repositories;

import iot.algo.course_project.models.WeatherStationEntry;
import iot.algo.course_project.models.WindParameter;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Repository
public class WeatherDataCSVRepository {
    private static final String CSV_SEPARATOR = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Map<Integer, WeatherStationEntry> weatherDataMap;

    public WeatherDataCSVRepository() {

        this.weatherDataMap = getAllEntriesFromCurrentMonthInMap();
        System.out.println(weatherDataMap);
    }

    public void addOrUpdateWeatherData(WeatherStationEntry weatherStationEntry) {
        weatherDataMap.put(weatherStationEntry.getId(), weatherStationEntry);
        saveAllWeatherData();
    }

    public List<WeatherStationEntry> getWeatherDataByWeatherStationId(int weatherStationId) {

        return weatherDataMap.values()
                .stream()
                .filter(entry -> entry.getWeatherStationId() == weatherStationId)
                .collect(Collectors.toList());

    }
    public WeatherStationEntry getWeatherDataById(int id) {
        return weatherDataMap.get(id);
    }

    public List<WeatherStationEntry> getAllWeatherData() {
        File file = getFileIfExists();
        if (file != null) {
            return readWeatherDataFromFile(file);

        }
        return null;
    }

    public List<File> getAllFilesFromCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        String currentMonthYear = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        File currentMonthDirectory = new File(currentMonthYear);

        if (currentMonthDirectory.exists() && currentMonthDirectory.isDirectory()) {
            File[] files = currentMonthDirectory.listFiles();
            if (files != null) {
                return Arrays.asList(files);
            }
        }

        return Collections.emptyList();
    }
    public Map<Integer, WeatherStationEntry> getAllEntriesFromCurrentMonthInMap() {
        Map<Integer, WeatherStationEntry> entriesMap = new HashMap<>();
        List<File> files = getAllFilesFromCurrentMonth();

        for (File file : files) {
            List<WeatherStationEntry> entries = readWeatherDataFromFile(file);
            for (WeatherStationEntry entry : entries) {
                entriesMap.put(entry.getId(), entry);
            }
        }

        return entriesMap;
    }
    public List<WeatherStationEntry> readWeatherDataFromFile(File file) {
        List<WeatherStationEntry> weatherData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                if (data.length == 8) {
                    int id = Integer.parseInt(data[0]);
                    Date datetime = parseDate(data[1]);
                    double temperature = Double.parseDouble(data[2]);
                    double humidity = Double.parseDouble(data[3]);
                    double windSpeed = Double.parseDouble(data[4]);
                    double atmospherePressure = Double.parseDouble(data[5]);
                    String windDirection = data[6];
                    int weatherStationId = Integer.parseInt(data[7]);

                    WindParameter windParameter = new WindParameter();
                    windParameter.setSpeed(windSpeed);
                    windParameter.setAtmospherePressure(atmospherePressure);
                    windParameter.setDirection(windDirection);

                    WeatherStationEntry weatherStationEntry = new WeatherStationEntry();
                    weatherStationEntry.setId(id);
                    weatherStationEntry.setDatetime(datetime);
                    weatherStationEntry.setTemperature(temperature);
                    weatherStationEntry.setHumidity(humidity);
                    weatherStationEntry.setWindParameter(windParameter);
                    weatherStationEntry.setWeatherStationId(weatherStationId);

                    weatherData.add(weatherStationEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherData;
    }

    public void saveAllWeatherData() {
        File file = getFileIfNonExistsCreate();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,datetime,temperature,humidity,wind_speed,atmosphere_pressure,wind_direction,weather_station_id\n");
            for (WeatherStationEntry weatherStationEntry : weatherDataMap.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(weatherStationEntry.getId()).append(CSV_SEPARATOR);
                sb.append(formatDate(weatherStationEntry.getDatetime())).append(CSV_SEPARATOR);
                sb.append(weatherStationEntry.getTemperature()).append(CSV_SEPARATOR);
                sb.append(weatherStationEntry.getHumidity()).append(CSV_SEPARATOR);
                sb.append(weatherStationEntry.getWindParameter().getSpeed()).append(CSV_SEPARATOR);
                sb.append(weatherStationEntry.getWindParameter().getAtmospherePressure()).append(CSV_SEPARATOR);
                sb.append(weatherStationEntry.getWindParameter().getDirection()).append(CSV_SEPARATOR);
                sb.append(weatherStationEntry.getWeatherStationId()).append("\n");
                writer.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFileIfExists() {
        LocalDate currentDate = LocalDate.now();
        String currentMonthYear = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String fileName = "weatherdata-" + currentMonthYear + ".csv";
        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    private File getFileIfNonExistsCreate() {
        LocalDate currentDate = LocalDate.now();
        String currentMonthYear = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String fileName = "weatherdata-" + currentMonthYear + ".csv";
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("File " + fileName + " created.");
            } catch (IOException e) {
                System.out.println("Error creating file " + fileName + ": " + e.getMessage());
            }
        } else {
            System.out.println("File " + fileName + " already exists.");
        }
        return file;
    }

    private Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

}