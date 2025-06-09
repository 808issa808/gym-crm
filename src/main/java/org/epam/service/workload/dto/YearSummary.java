package org.epam.service.workload.dto;

import lombok.Data;
import java.util.List;

@Data
public class YearSummary {
    private int year;
    private List<MonthSummary> monthSummaries;
}
