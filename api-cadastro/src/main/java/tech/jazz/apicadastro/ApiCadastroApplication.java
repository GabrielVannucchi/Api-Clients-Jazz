package tech.jazz.apicadastro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tech.jazz.apicadastro.presentation.controller.ClientsController;

@SpringBootApplication
@EnableFeignClients
public class ApiCadastroApplication {
	private static final Logger logger = LoggerFactory.getLogger(ApiCadastroApplication.class);
	public static void main(String[] args) {
		logger.info("Inicio da aplicação");
		SpringApplication.run(ApiCadastroApplication.class, args);
	}

}
