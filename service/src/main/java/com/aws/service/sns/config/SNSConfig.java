package com.aws.service.sns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SNSConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${spring.cloud.aws.region.static}")
    private String region;


    @Bean
    public SnsClient snsClient(){

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey,secretKey);

        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(() -> awsBasicCredentials)
                .build();
    }
}
