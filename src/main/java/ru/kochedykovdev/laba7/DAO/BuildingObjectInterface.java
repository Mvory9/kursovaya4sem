package ru.kochedykovdev.laba7.DAO;

public class BuildingObjectInterface {
    private Long id;
    private String name;
    private String address;
    private ProrabInterface prorabInterface;        // FK на Prorab
}