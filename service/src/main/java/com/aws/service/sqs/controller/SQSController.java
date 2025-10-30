package com.aws.service.sqs.controller;

import com.amazonaws.services.sqs.model.Message;
import com.aws.service.sqs.service.SQSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sqs")
public class SQSController {

    private final SQSService sqsService;

    public SQSController(SQSService sqsService) {
        this.sqsService = sqsService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        sqsService.publishMessage(message);
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping("/receive")
    public ResponseEntity<List<Message>> receiveMessages() {
        List<Message> messages = sqsService.receiveMessages(10);
        return ResponseEntity.ok(messages);
    }
}