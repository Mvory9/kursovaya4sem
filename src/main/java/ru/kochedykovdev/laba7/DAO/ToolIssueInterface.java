package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

public class ToolIssueInterface {
    private Long id;
    private ToolInterface toolInterface;                // FK на Tool
    private BuildingObjectInterface object;    // FK на BuildingObject
    private String issuedTo;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
}