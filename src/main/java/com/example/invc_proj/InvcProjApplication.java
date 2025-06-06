package com.example.invc_proj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class InvcProjApplication {

	public static void main(String[] args)
	{
		//SpringApplication.run(InvcProjApplication.class, args);
		SpringApplication app = new SpringApplication(InvcProjApplication.class);
		app.setAdditionalProfiles("default");
		app.run(args);
	}

}
