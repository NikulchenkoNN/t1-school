package ru.home_work.t1_school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.home_work.t1_school"})
public class T1SchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(T1SchoolApplication.class, args);
	}

}
