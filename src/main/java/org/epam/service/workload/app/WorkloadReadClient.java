package org.epam.service.workload.app;

import org.epam.service.workload.dto.TrainerSummary;

public interface WorkloadReadClient {
    TrainerSummary getSummary(String username);
}