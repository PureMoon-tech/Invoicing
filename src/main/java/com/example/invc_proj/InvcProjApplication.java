package com.example.invc_proj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableJpaRepositories(basePackages = "com.example.invc_proj.repository")
@EnableAsync
@EnableCaching
public class InvcProjApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(InvcProjApplication.class, args);
		/*SpringApplication app = new SpringApplication(InvcProjApplication.class);
		app.setAdditionalProfiles("default");
		app.run(args);*/
	}
 
}
