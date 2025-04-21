package com.api.fipe.tabelaFipe.main;

import com.api.fipe.tabelaFipe.model.Dados;
import com.api.fipe.tabelaFipe.model.InformacoesVaiculos;
import com.api.fipe.tabelaFipe.model.Modelos;
import com.api.fipe.tabelaFipe.service.ConsumoApi;
import com.api.fipe.tabelaFipe.service.ConverteDados;

import java.util.*;

public class Main
{

    private final ConverteDados CONVERSOR = new ConverteDados();

    Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    String endereco;

    public Map<String, String> exibeMenu()
    {
        String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
        Map<String, String> enderecoEjson = new HashMap<>();

        String opcao;
            System.out.println("""
                Quer comprar um carro? Venha ver os preços no FipeL!!
                
                Qual tipo de veículo você procura?
                1 - carro
                2 - moto
                3 - caminhão
                
                Digite uma das opções acima:
                """);

                opcao = scanner.nextLine();

            if (opcao.toUpperCase().contains("car".toUpperCase()))
            {
                endereco = URL_BASE + "carros/marcas";
            }
            else if(opcao.toUpperCase().contains("mot".toUpperCase()))
            {
                endereco = URL_BASE + "motos/marcas";
            }
            else
            {
                endereco = URL_BASE + "caminhoes/marcas";
            }

            String json = consumoApi.obterDados(endereco);

            enderecoEjson.put(endereco, json);

            return enderecoEjson;

    }

    public Map<String, String> escolherMarca(Map<String, String> enderecoEjson)
    {
        int marcaEscolhida;

        String json = enderecoEjson.values().iterator().next();
        var marcas = CONVERSOR.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(m -> System.out.println(m.nome() + ": " + m.codigo()));

        try
        {
            System.out.println("\nDigite o código da marca que você deseja: ");
            marcaEscolhida = scanner.nextInt();
            scanner.nextLine();

            String novoEndereco = enderecoEjson.keySet().iterator().next() + "/" + marcaEscolhida + "/modelos";
            var jsonModelo = consumoApi.obterDados(novoEndereco);
            enderecoEjson.clear();
            enderecoEjson.put(novoEndereco, jsonModelo);
        }
        catch (Exception e)
        {
            System.out.println("Erro ao ler a entrada: " + e.getMessage());
            return Main.this.escolherMarca(enderecoEjson);
        }

        return enderecoEjson;
    }

    public Map<String, String> escolherModelo(Map<String, String> enderecoEjson)
    {
        int modeloEscolhido;

        String json = enderecoEjson.values().iterator().next();

        var modeloLista = CONVERSOR.converteDados(json, Modelos.class);

        try
        {
            System.out.println("Insira o nome do modelo que você deseja pesquisar: ");
            String nomeModelo = scanner.nextLine();

            List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                    .filter(m -> m.nome().toUpperCase().contains(nomeModelo.toUpperCase()))
                    .toList();

            System.out.println("Modelos filtrados: ");
            modelosFiltrados.forEach(m -> System.out.println("Nome do modelo: " + m.nome() + " Código: " + m.codigo()));

            System.out.println("\nDigite o código do modelo que você deseja visualizar os anos existentes: ");
            var codigoModelo = scanner.nextLine();

            String novoEndereco = enderecoEjson.keySet().iterator().next() + "/" + codigoModelo + "/anos";

            json = consumoApi.obterDados(novoEndereco);
            enderecoEjson.clear();
            enderecoEjson.put(novoEndereco, json);
            List<Dados> listagemAnos= CONVERSOR.obterLista(json, Dados.class);

            List<InformacoesVaiculos> informacoesVeiculos = new ArrayList<>();

            for(int i = 0; i < listagemAnos.size(); i++)
            {
                var enderecoAnos = novoEndereco + "/" + listagemAnos.get(i).codigo();

                json = consumoApi.obterDados(enderecoAnos);
                var informacoes = CONVERSOR.converteDados(json, InformacoesVaiculos.class);

                informacoesVeiculos.add(informacoes);
            }

            System.out.println("\nInformações dos veículos: ");
            informacoesVeiculos.forEach(i -> System.out.println("\nValor: " + i.valor() +
                    "\nMarca: " + i.marca() +
                    "\nModelo: " + i.modelo() +
                    "\nAno: " + i.ano() +
                    "\nCombustível: " + i.combustivel()));
        }
        catch (Exception e)
        {
            System.out.println("Erro ao ler a entrada. você deve inserir um número: " + e.getMessage());
            return Main.this.escolherModelo(enderecoEjson);
        }

        return enderecoEjson;
    }
}
