package iot.algo.course_project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WindParameter {
    private double speed;
    private double atmospherePressure;
    private String direction;
}
