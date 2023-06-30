package iot.algo.course_project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStationEntry {

    private Date datetime;

    private WindParameter windParameter;

    private double humidity;
    private double temperature;

    private int weatherStationId;
    private int id;

}
