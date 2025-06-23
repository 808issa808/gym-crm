package org.epam.service.workload.inf;

import lombok.RequiredArgsConstructor;
import org.epam.service.workload.app.WorkloadWriteClient;
import org.epam.service.workload.dto.UpdateReport;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUpdateReportProducer implements WorkloadWriteClient {
    private final KafkaTemplate<String, UpdateReport> kafkaTemplate;
    private static final String TOPIC = "trainer-summary-update";
    @Override
    public void updateTrainerSummary(UpdateReport updateReport) {
        kafkaTemplate.send(TOPIC, updateReport.getUsername(), updateReport);
    }
}