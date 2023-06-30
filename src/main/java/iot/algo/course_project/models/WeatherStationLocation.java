package iot.algo.course_project.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStationLocation {
    private String GPS;
    private String city;
    private String street;

}
