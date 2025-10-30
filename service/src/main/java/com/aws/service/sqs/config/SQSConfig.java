package com.aws.service.sqs.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;


@Configuration
public class SQSConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonSQSAsync amazonSQSClient() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

        @Bean
        public SqsClient sqsClient() {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

            return SqsClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();
        }
}