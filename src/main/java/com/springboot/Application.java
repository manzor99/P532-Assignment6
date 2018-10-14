package com.springboot;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.gamemaker.GameMaker;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// ApplicationContext ctx = SpringApplication.run(Application.class, args);

		ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).headless(false)
				.run(args);
		GameMaker appFrame = context.getBean(GameMaker.class);

		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

}
