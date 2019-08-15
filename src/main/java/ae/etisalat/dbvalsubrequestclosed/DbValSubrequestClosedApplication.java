package ae.etisalat.dbvalsubrequestclosed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DbValSubrequestClosedApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbValSubrequestClosedApplication.class, args);
	}

}
