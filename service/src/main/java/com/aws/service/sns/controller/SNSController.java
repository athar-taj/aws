package com.aws.service.sns.controller;

import com.aws.service.sns.service.SNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SNSController {

    @Autowired
    private SNSService snsService;

    @GetMapping("/subscribe/{email}")
    public String subscribeTopic(@PathVariable String email){
        return snsService.subscribeTopic(email);
    }

    @GetMapping("/publish/{message}")
    public String publishMessage(@PathVariable String message){
        return snsService.publishMessage(message);
    }

    @GetMapping("/publish-otp")
    public Boolean publishOTP(@RequestParam("email") String email, @RequestParam("message") String message){
        return snsService.publishOTP(email,message);
    }
}
