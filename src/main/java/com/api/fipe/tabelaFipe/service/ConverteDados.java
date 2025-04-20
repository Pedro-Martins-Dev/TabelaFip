package com.api.fipe.tabelaFipe.service;

import com.api.fipe.tabelaFipe.model.Dados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class ConverteDados implements IConverteDados
{
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> T converteDados(String json, Class<T> classe)
    {
        try
        {
            return objectMapper.readValue(json, classe);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Erro ao converter dados: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> List<T> obterLista(String json, Class<T> classe)
    {
        CollectionType lista = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, classe);

        try
        {
            return objectMapper.readValue(json, lista);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Erro ao converter lista de dados: " + e.getMessage(), e);
        }
    }
}
