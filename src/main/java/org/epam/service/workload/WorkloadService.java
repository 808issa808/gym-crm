package org.epam.service.workload;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.epam.service.workload.dto.TrainerSummary;
import org.epam.service.workload.dto.UpdateReport;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadService {
    private final WorkloadClient workloadClient;

    @CircuitBreaker(name = "workload", fallbackMethod = "fallback")
    public void updateTrainerSummary(UpdateReport updateReport) {
        workloadClient.updateTrainerSummary(updateReport);
    }
    @CircuitBreaker(name = "workload", fallbackMethod = "fallback")
    public TrainerSummary getSummary( String username){
        return workloadClient.getSummary(username);
    }

    public TrainerSummary fallback(String username, Throwable throwable) throws Exception {
        throw new RuntimeException("workload is DOWN!!!");
    }
}
