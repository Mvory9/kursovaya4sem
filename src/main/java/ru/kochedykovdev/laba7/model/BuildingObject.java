package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingObject {

    private Long id;
    private String name;
    private String address;
    private Long prorabId;

    @Override
    public String toString() {
        return name;
    }
}
