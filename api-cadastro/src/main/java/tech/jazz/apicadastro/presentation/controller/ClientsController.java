package tech.jazz.apicadastro.presentation.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jazz.apicadastro.applicationservice.clientsservice.CreateClientsService;
import tech.jazz.apicadastro.applicationservice.clientsservice.SearchClientsService;
import tech.jazz.apicadastro.infrastructure.model.Client;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientRequestDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.DuplicateCpfException;

import java.util.List;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientsController {
    private final CreateClientsService createClientsService;
    private final SearchClientsService searchClientsService;


    @GetMapping
    @Tag(name = "Listar todos os clientes", description = "Devolve uma lista de clientes")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados")
    @ApiResponse(responseCode = "204", description = "Não há clientes cadastradao", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<List<ClientEntity>> listAll(){
        return ResponseEntity.status(200).body(searchClientsService.listAll());
    }

    @GetMapping("{id}")
    @Tag(name = "Retorna cliente referente ao id", description = "Devolve um objeto cliente com o id solicitado")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ClientEntity> findById(@PathVariable String id){
        return ResponseEntity.status(200).body(searchClientsService.findById(id));
    }

    @GetMapping("cpf/{cpf}")
    @Tag(name = "Retorna cliente referente ao cpf", description = "Devolve um objeto cliente com o cpf solicitado")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ClientEntity> findByCpf(@PathVariable String cpf){
        return ResponseEntity.status(200).body(searchClientsService.findByCpf(cpf));
    }

    @PostMapping
    @Tag(name = "Cadastra cliente", description = "Cadastra um cliente no sistema")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
    @ApiResponse(responseCode = "406", description = "Erro de validação")
    @ApiResponse(responseCode = "409", description = "CPF informado já consta na base de dados")
    public ResponseEntity<ClientEntity> save(@Valid @RequestBody ClientRequestDto clientRequestDto){
        if(!searchClientsService.existsByCpf(clientRequestDto.cpf())) {
            return ResponseEntity.status(201).body(createClientsService.save(clientRequestDto));
        }else{
            throw new DuplicateCpfException();
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Client> deleteById(@PathVariable String id){
        createClientsService.deleteById(id);
        return ResponseEntity.status(204).build();
    }



}
