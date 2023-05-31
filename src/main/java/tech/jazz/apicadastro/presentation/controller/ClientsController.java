package tech.jazz.apicadastro.presentation.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jazz.apicadastro.applicationservice.clientsservice.CreateClientsService;
import tech.jazz.apicadastro.applicationservice.clientsservice.SearchClientsService;
import tech.jazz.apicadastro.applicationservice.dto.ClientRequestDto;
import tech.jazz.apicadastro.infrastructure.domain.Client;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.DuplicateCpfException;

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
    public ResponseEntity<List<ClientResponseDto>> listAll() {
        return ResponseEntity.status(200).body(searchClientsService.listAll());
    }

    @GetMapping("{id}")
    @Tag(name = "Retorna cliente referente ao id", description = "Devolve um objeto cliente com o id solicitado")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ClientResponseDto> findById(@PathVariable String id) {
        return ResponseEntity.status(200).body(searchClientsService.findById(id));
    }

    @GetMapping("cpf/{cpf}")
    @Tag(name = "Retorna cliente referente ao cpf", description = "Devolve um objeto cliente com o cpf solicitado")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ClientResponseDto> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.status(200).body(searchClientsService.findByCpf(cpf));
    }

    @PostMapping
    @Tag(name = "Cadastra cliente", description = "Cadastra um cliente no sistema")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
    @ApiResponse(responseCode = "406", description = "Erro de validação")
    @ApiResponse(responseCode = "409", description = "CPF informado já consta na base de dados")
    public ResponseEntity<ClientResponseDto> save(@Valid @RequestBody ClientRequestDto clientRequestDto) {
        if (!searchClientsService.existsByCpf(clientRequestDto.cpf())) {
            return ResponseEntity.status(201).body(createClientsService.save(clientRequestDto));
        } else {
            throw new DuplicateCpfException("CPF already registered");
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Client> deleteById(@PathVariable String id) {
        createClientsService.deleteById(id);
        return ResponseEntity.status(204).build();
    }



}
