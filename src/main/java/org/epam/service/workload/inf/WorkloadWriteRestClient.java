package org.epam.service.workload.inf;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.epam.service.workload.FeignClientConfig;
import org.epam.service.workload.app.WorkloadWriteClient;
import org.epam.service.workload.dto.UpdateReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload", contextId = "workloadWriteClient", configuration = FeignClientConfig.class)
public interface WorkloadWriteRestClient extends WorkloadWriteClient {
    @Override
    @PostMapping("/trainer-summary")
    @CircuitBreaker(name = "workload", fallbackMethod = "updateFallback")
    void updateTrainerSummary(@RequestBody UpdateReport updateReport);

    default void updateFallback(UpdateReport updateReport, Throwable throwable) {
        throw new RuntimeException("workload is DOWN!!!");
    }
}
