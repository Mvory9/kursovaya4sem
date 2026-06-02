package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCard {

    private Long id;
    private String name;
    private Integer article;
    private String unit;
    private Integer writeOffRate;

    @Override
    public String toString() {
        return name;
    }
}
