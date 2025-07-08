package org.epam.service.workload.inf;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.epam.service.workload.FeignClientConfig;
import org.epam.service.workload.app.WorkloadReadClient;
import org.epam.service.workload.dto.TrainerSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.NoSuchElementException;

@FeignClient(name = "workload", contextId = "workloadReadClient", configuration = FeignClientConfig.class)
public interface WorkloadReadRestClient extends WorkloadReadClient {

    @Override
    @GetMapping("/trainer-summary/{username}")
    @CircuitBreaker(name = "workload", fallbackMethod = "getFallback")
    TrainerSummary getSummary(@PathVariable("username") String username);
    default TrainerSummary getFallback(String username, Throwable throwable) {
        if (throwable instanceof FeignException.NotFound) {
            throw new NoSuchElementException("no such trainer with username "+username );
        }

        throw new RuntimeException("workload is DOWN!!!", throwable);
    }
}