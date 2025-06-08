package org.epam.service.workload;

import lombok.RequiredArgsConstructor;
import org.epam.service.workload.app.WorkloadReadClient;
import org.epam.service.workload.app.WorkloadWriteClient;
import org.epam.service.workload.dto.TrainerSummary;
import org.epam.service.workload.dto.UpdateReport;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadService {
    private final WorkloadWriteClient updateSummaryClient;
    private final WorkloadReadClient readSummaryClient;

    public void updateTrainerSummary(UpdateReport updateReport) {
        updateSummaryClient.updateTrainerSummary(updateReport);
    }
    public TrainerSummary getSummary( String username){
        return readSummaryClient.getSummary(username);
    }
}
