package iot.algo.course_project.Repositories;

import iot.algo.course_project.models.WeatherStation;
import iot.algo.course_project.models.WeatherStationLocation;
import iot.algo.course_project.models.ServiceWork;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Setter
@Getter
@Repository
public class WeatherStationCSVRepository {
    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter FILENAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Map<Integer, WeatherStation> monthlyWeatherStationsMap;

    public WeatherStationCSVRepository() {
        monthlyWeatherStationsMap = getAllStationsDataFromCurrentMonthInMap();
    }

    public Map<Integer, WeatherStation> getAllStationsDataFromCurrentMonthInMap() {
        Map<Integer, WeatherStation> stationsData = new HashMap<>();
        List<File> files = getAllFilesFromCurrentMonth();

        for (File file : files) {
            List<WeatherStation> stations = getAllWeatherStations(file);
            for (WeatherStation weatherStation : stations) {
                stationsData.put(weatherStation.getId(), weatherStation);
            }
        }

        return stationsData;
    }

    public WeatherStation getWeatherStation(int id) {
        List<WeatherStation> weatherStations = getAllWeatherStations();
        for (WeatherStation weatherStation : weatherStations) {
            if (weatherStation.getId() == id) {
                return weatherStation;
            }
        }

        return monthlyWeatherStationsMap.get(id);
    }


    public List<File> getAllFilesFromCurrentMonth() {
        List<File> files = new ArrayList<>();

        // Get the current month
        LocalDate currentDate = LocalDate.now();
        String currentMonth = currentDate.format(FILENAME_DATE_FORMATTER);

        // Get all files in the current directory
        File currentDirectory = new File(".");
        File[] allFiles = currentDirectory.listFiles();

        // Filter files based on the current month
        for (File file : allFiles) {
            if (file.isFile() && file.getName().startsWith("stations-") && file.getName().contains(currentMonth)) {
                files.add(file);
            }
        }

        return files;
    }

    public List<WeatherStation> getAllStationsDataFromCurrentMonthInList() {
        List<WeatherStation> stationsData = new ArrayList<>();
        List<File> files = getAllFilesFromCurrentMonth();

        for (File file : files) {
            // Read and process data from each file
            List<WeatherStation> stations = getAllWeatherStations(file);
            stationsData.addAll(stations);
        }

        return stationsData;
    }

    public List<WeatherStation> getAllWeatherStations(File file) {
        List<WeatherStation> weatherStations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); //skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                if (data.length == 8) {
                    int id = Integer.parseInt(data[0]);
                    WeatherStationLocation location = new WeatherStationLocation(data[1], data[2], data[3]);
                    String manufacturer = data[4];
                    Date installationDate = parseDate(data[5]);
                    ServiceWork serviceWork = new ServiceWork(data[7], parseDate(data[6]));
                    WeatherStation weatherStation = new WeatherStation(id, location, manufacturer, installationDate, serviceWork);
                    weatherStations.add(weatherStation);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return weatherStations;
    }
    public List<WeatherStation> getAllWeatherStations(String filePath) {
        return getAllWeatherStations(getFileFromResourcesByDateIfNonExistsCreate(filePath));
    }

    public List<WeatherStation> getAllWeatherStations() {
        return getAllWeatherStations("");
    }

    public WeatherStation saveWeatherStation(WeatherStation weatherStation, String filePath) {
        int id = weatherStation.getId();

        // Check if the weather station with the given ID exists
        WeatherStation existingStation = getWeatherStation(id);
        if (existingStation != null) {
            // Weather station with the given ID exists, update it
            existingStation.setLocation(weatherStation.getLocation());
            existingStation.setManufacturer(weatherStation.getManufacturer());
            existingStation.setInstallationDate(weatherStation.getInstallationDate());
            existingStation.setServiceWorkStatus(weatherStation.getServiceWorkStatus());

            saveAllWeatherStations(getAllWeatherStations(filePath), filePath); // Save all weather stations
            monthlyWeatherStationsMap.put(weatherStation.getId(), weatherStation);
            return existingStation;
        } else {
            // Weather station with the given ID does not exist, save it as a new weather station
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileFromResourcesByDateIfNonExistsCreate(filePath), true))) {
                StringBuilder sb = new StringBuilder();
                sb.append(weatherStation.getId()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getLocation().getGPS()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getLocation().getCity()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getLocation().getStreet()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getManufacturer()).append(CSV_SEPARATOR);
                sb.append(formatDate(weatherStation.getInstallationDate())).append(CSV_SEPARATOR);
                sb.append(formatDate(weatherStation.getServiceWorkStatus().getServiceWorkDate())).append(CSV_SEPARATOR);
                sb.append(weatherStation.getServiceWorkStatus().getDescription());
                sb.append("\n");

                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            monthlyWeatherStationsMap.put(weatherStation.getId(), weatherStation);
            return weatherStation;
        }
    }
    public WeatherStation saveWeatherStation(WeatherStation weatherStation) {
        return saveWeatherStation(weatherStation, "");
    }

    public void deleteWeatherStation(int id) {
        deleteWeatherStation(id,"");
    }

    public void deleteWeatherStation(int id, String filePath) {
        List<WeatherStation> weatherStations = getAllWeatherStations(filePath);
        for (WeatherStation weatherStation : weatherStations) {
            if (weatherStation.getId() == id) {
                monthlyWeatherStationsMap.remove(weatherStation.getId());
                weatherStations.remove(weatherStation);
                break;
            }
        }
        saveAllWeatherStations(weatherStations);

    }

    private void saveAllWeatherStations(List<WeatherStation> weatherStations) {
        saveAllWeatherStations(weatherStations, "");
    }

    private void saveAllWeatherStations(List<WeatherStation> weatherStations, String filePath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Objects.requireNonNull(getFileFromResourcesByDateIfNonExistsCreate(filePath))))) {
            //writer.write("id,GPS,city,street,manufacturer,installation_date,date_of_work,work_description\n");
            for (WeatherStation weatherStation : weatherStations) {

                StringBuilder sb = new StringBuilder();
                sb.append(weatherStation.getId()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getLocation().getGPS()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getLocation().getCity()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getLocation().getStreet()).append(CSV_SEPARATOR);
                sb.append(weatherStation.getManufacturer()).append(CSV_SEPARATOR);
                sb.append(formatDate(weatherStation.getInstallationDate())).append(CSV_SEPARATOR);
                sb.append(formatDate(weatherStation.getServiceWorkStatus().getServiceWorkDate())).append(CSV_SEPARATOR);
                sb.append(weatherStation.getServiceWorkStatus().getDescription());
                sb.append("\n");

                writer.write(sb.toString());
                monthlyWeatherStationsMap.put(weatherStation.getId(), weatherStation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public Date parseDate(String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }

    public File getFileFromResourcesByDateIfNonExistsCreate(String fileName) {

        //first search for file existence
        File file = new File(fileName);

        if (file.exists()) {
            return file;
        }

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        fileName = "stations-" + today.format(formatter) + ".csv";

        file = new File(fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                String header = "id,GPS,city,street,manufacturer,installation_date,date_of_work,work_description\n";
                Files.write(Paths.get(file.getPath()), header.getBytes());
                System.out.println("Файл " + fileName + " створено.");
            } catch (IOException e) {
                System.out.println("Помилка при створенні файлу " + fileName + ": " + e.getMessage());
            }
        } else {
            System.out.println("Файл " + fileName + " вже був згенерований за датою і існує");
        }

        return file;
    }

}