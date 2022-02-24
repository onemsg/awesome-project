package dm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * DataMiningOnlineApplication
 */
@SpringBootApplication
@EnableJdbcRepositories
public class DataMiningOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataMiningOnlineApplication.class, args);
    }
}