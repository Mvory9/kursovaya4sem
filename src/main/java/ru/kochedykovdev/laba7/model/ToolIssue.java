package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolIssue {

    private Long id;
    private Long toolId;
    private Long objectId;
    private String issuedTo;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
}
