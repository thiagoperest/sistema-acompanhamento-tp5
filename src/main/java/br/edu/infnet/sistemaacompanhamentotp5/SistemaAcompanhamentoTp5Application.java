package br.edu.infnet.sistemaacompanhamentotp5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SistemaAcompanhamentoTp5Application {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAcompanhamentoTp5Application.class, args);
    }

}
