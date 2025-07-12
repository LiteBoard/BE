package we.LiteBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LiteBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteBoardApplication.class, args);
	}

}
