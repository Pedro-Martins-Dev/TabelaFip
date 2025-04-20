package com.api.fipe.tabelaFipe.service;

import java.util.List;

public interface IConverteDados {
    <T> T converteDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}