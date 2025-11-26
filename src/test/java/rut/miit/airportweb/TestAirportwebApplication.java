package rut.miit.airportweb;

import org.springframework.boot.SpringApplication;

public class TestAirportwebApplication {

	public static void main(String[] args) {
		SpringApplication.from(AirportwebApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
