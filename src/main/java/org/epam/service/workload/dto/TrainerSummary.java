package org.epam.service.workload.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainerSummary {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private boolean isActive;
    private List<YearSummary> yearSummaries;
}
