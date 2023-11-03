package info.kupchenko.sandbox.stream.txdemo.function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BatchProducerApp {

	public static void main(String[] args) {
		SpringApplication.run(BatchProducerApp.class, args);
	}

}
