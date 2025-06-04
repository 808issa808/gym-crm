package org.epam.service.workload;

import org.epam.service.workload.dto.TrainerSummary;
import org.epam.service.workload.dto.UpdateReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload", configuration = FeignClientConfig.class)
@Component
public interface WorkloadClient {
    @PostMapping("/trainer-summary")
    void updateTrainerSummary(@RequestBody UpdateReport updateReport);

    @GetMapping("/trainer-summary/{username}")
    TrainerSummary getSummary(@PathVariable("username") String username);
}