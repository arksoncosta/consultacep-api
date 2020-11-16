package br.com.arkson.consultacepapi.controllers;

import br.com.arkson.consultacepapi.model.Endereco;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class ConsultaEnderecoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ConsultaEnderecoController consultaEnderecoController;

    @Test
    void consultaEnderecoByCep() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setCidade("Juazeiro do Norte");
        endereco.setLogradouro("Logradouro");
        Mockito.when(consultaEnderecoController.consultaEnderecoByCep(any()))
                .thenReturn(ResponseEntity.ok(endereco));

        mockMvc.perform(get("/63031760").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade", is("Juazeiro do Norte")))
                .andExpect(jsonPath("$.end", is("Logradouro")));
    }
}