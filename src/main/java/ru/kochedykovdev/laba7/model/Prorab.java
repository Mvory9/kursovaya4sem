package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prorab {

    private Long id;
    private String name;
    private String phone;

    @Override
    public String toString() {
        return name;
    }
}
