package com.example.invc_proj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.example.invc_proj.repository")
public class InvcProjApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(InvcProjApplication.class, args);
		/*SpringApplication app = new SpringApplication(InvcProjApplication.class);
		app.setAdditionalProfiles("default");
		app.run(args);*/
	}

}
