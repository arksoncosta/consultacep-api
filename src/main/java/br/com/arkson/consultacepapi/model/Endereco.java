package br.com.arkson.consultacepapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author arkson
 * @date 16/11/2020
 */
@Data
public class Endereco implements Serializable {

    private String cep;

    @JsonProperty("end")
    private String logradouro;
    private String cidade;
    private String bairro;

    @JsonProperty("complemento2")
    private String complemento;
    private String uf;

}
