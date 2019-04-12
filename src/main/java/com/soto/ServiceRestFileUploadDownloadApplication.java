package com.soto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.soto.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class ServiceRestFileUploadDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRestFileUploadDownloadApplication.class, args);
	}

}
