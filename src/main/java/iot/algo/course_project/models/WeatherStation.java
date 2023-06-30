package iot.algo.course_project.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStation {

    private int id;
    private WeatherStationLocation location;
    private String manufacturer;
    private Date installationDate;
    private ServiceWork serviceWorkStatus;

}
