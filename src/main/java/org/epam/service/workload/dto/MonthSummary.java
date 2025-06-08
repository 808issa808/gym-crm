package org.epam.service.workload.dto;

import lombok.Data;
import java.time.Month;

@Data
public class MonthSummary {
    private Month month;
    private int duration;
}
