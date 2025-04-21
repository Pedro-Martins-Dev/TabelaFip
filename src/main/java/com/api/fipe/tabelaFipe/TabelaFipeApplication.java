package com.api.fipe.tabelaFipe;

import com.api.fipe.tabelaFipe.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Map;

@SpringBootApplication
public class TabelaFipeApplication implements CommandLineRunner
{

	public static void main(String[] args) {
		SpringApplication.run(TabelaFipeApplication.class, args);
	}

	@Override
	public void run(String... args)
	{
		Main main = new Main();

			Map<String, String> veiculoEscolhido = main.exibeMenu();
			Map<String, String> marcaEscolhida = main.escolherMarca(veiculoEscolhido);
		Map<String, String> modeloEscolhido = main.escolherModelo(marcaEscolhida);

	}




}
