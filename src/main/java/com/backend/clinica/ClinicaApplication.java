package com.backend.clinica;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClinicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicaApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT)
				.setFieldMatchingEnabled(true)
				.setSkipNullEnabled(true);
		return mapper;
	}

}
