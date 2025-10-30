package com.aws.service.sqs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;

@Component
@Slf4j
public class AmazonSQSListener {

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    SQSService amazonSQS;


    private static final String QUEUE_URL = "https://sqs.ap-south-1.amazonaws.com/392894084667/testingQueue";

    @Scheduled(fixedDelay = 1000)
    public void pollMessages() {
        try {
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(20)
                    .build();

            ReceiveMessageResponse response = sqsClient.receiveMessage(receiveRequest);
            List<Message> messages = response.messages();

            for (Message message : messages) {
                try {
                    processMessage(message.body());

                    DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                            .queueUrl(QUEUE_URL)
                            .receiptHandle(message.receiptHandle())
                            .build();
                    sqsClient.deleteMessage(deleteRequest);

                } catch (Exception e) {
                    log.error("Error processing message: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error polling SQS: {}", e.getMessage(), e);
        }
    }

    private void processMessage(String message) {
        log.info("Message received from SQS: {}", message);
    }
}
