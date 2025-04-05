package TTCS.TTCS_ThayPhuong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TtcsThayPhuongApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtcsThayPhuongApplication.class, args);
	}

}
