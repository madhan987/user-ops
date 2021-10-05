package com.benz.user.ops.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaProducer {

	@Value("${user.data.topic}")
	private String dataProcessorTopic;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(String data) {
		log.info("sending data='{}'", data);
		kafkaTemplate.send(dataProcessorTopic, data);
	}
}
