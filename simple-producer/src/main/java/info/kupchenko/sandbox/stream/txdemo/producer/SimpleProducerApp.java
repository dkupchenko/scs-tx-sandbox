package info.kupchenko.sandbox.stream.txdemo.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SimpleProducerApp {

	public static void main(String[] args) {
		SpringApplication.run(SimpleProducerApp.class, args);
	}

}
