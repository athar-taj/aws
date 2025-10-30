package com.aws.service.sns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

import java.util.Map;

@Service
public class SNSService {

    private final SnsClient snsClient;

    @Autowired
    public SNSService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    private String TOPIC_ARN = "arn:aws:sns:ap-south-1:392894084667:OTPsender";

    public String subscribeTopic(String email){
        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn(TOPIC_ARN)
                .protocol("email")
                .endpoint(email)
                .build();

        snsClient.subscribe(subscribeRequest);
        return "check Your Email " + email;
    }

    public String publishMessage(String message){
        PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(TOPIC_ARN)
                .message(message)
                .subject("Greeting From Swiggy")
                .build();

        snsClient.publish(publishRequest);
        return "Email Sent Successfully !!";
    }

    public Boolean publishOTP(String email, String message) {
        PublishRequest publishRequest = PublishRequest.builder()
                .message("Your OTP is " + message + " Don't Share with anyone")
                .subject("OTP From Swiggy")
                .messageAttributes(Map.of(
                        "email", MessageAttributeValue.builder()
                                .dataType("String")
                                .stringValue(email)
                                .build()
                ))
                .topicArn(TOPIC_ARN)
                .build();

        try {
            snsClient.publish(publishRequest);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public Boolean sendEmail(String email,String subject, String message) {
        PublishRequest publishRequest = PublishRequest.builder()
                .message(message)
                .subject(subject)
                .messageAttributes(Map.of(
                        "email", MessageAttributeValue.builder()
                                .dataType("String")
                                .stringValue(email)
                                .build()
                ))
                .topicArn(TOPIC_ARN)
                .build();

        try {
            snsClient.publish(publishRequest);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
