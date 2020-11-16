package br.com.arkson.consultacepapi.services;

import br.com.arkson.consultacepapi.model.Endereco;

/**
 * @author arkson
 * @date 16/11/2020
 */
public interface ConsultaEnerecoService {

    Endereco getEnderecoCorreios(String cep) throws Exception;

}
