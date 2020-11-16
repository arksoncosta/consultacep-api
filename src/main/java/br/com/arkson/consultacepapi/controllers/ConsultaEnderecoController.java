package br.com.arkson.consultacepapi.controllers;

import br.com.arkson.consultacepapi.model.Endereco;
import br.com.arkson.consultacepapi.services.ConsultaEnderecoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arkson
 * @date 16/11/2020
 */
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ConsultaEnderecoController {

    private final ConsultaEnderecoService consultaEnderecoService;

    @GetMapping("{cep}")
    public ResponseEntity<Endereco> consultaEnderecoByCep(@PathVariable String cep) throws Exception {
        try {
            Endereco endereco = consultaEnderecoService.getEnderecoCorreios(cep);
            return ResponseEntity.ok(endereco);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
