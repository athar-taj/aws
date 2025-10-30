package com.aws.service.sqs.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SQSService {


    @Value("${sqs.queue.end-point}")
    private String queueUrl;
    @Autowired
    AmazonSQS amazonSQSClient;


    public void publishMessage(String message) {
        try {
            com.amazonaws.services.sqs.model.SendMessageRequest sendMessageRequest =
                    new com.amazonaws.services.sqs.model.SendMessageRequest()
                            .withQueueUrl(queueUrl)
                            .withMessageBody(message);

            amazonSQSClient.sendMessage(sendMessageRequest);
            System.out.println("Message sent successfully: " + message);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            throw new RuntimeException("Error sending message to SQS", e);
        }
    }

    public List<Message> receiveMessages(int maxMessages) {
        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMaxNumberOfMessages(maxMessages)
                    .withWaitTimeSeconds(20);

            ReceiveMessageResult result = amazonSQSClient.receiveMessage(receiveMessageRequest);
            return result.getMessages();

        } catch (Exception e) {
            System.err.println("Error receiving messages: " + e.getMessage());
            throw new RuntimeException("Error receiving messages from SQS", e);
        }
    }

    public void deleteMessage(Message message) {
        try {
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withReceiptHandle(message.getReceiptHandle());

            amazonSQSClient.deleteMessage(deleteMessageRequest);
        } catch (Exception e) {
            System.err.println("Error deleting message: " + e.getMessage());
            throw new RuntimeException("Error deleting message from SQS", e);
        }
    }

}