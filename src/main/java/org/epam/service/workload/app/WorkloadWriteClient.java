package org.epam.service.workload.app;

import org.epam.service.workload.dto.UpdateReport;

public interface WorkloadWriteClient {
    void updateTrainerSummary(UpdateReport updateReport);
}