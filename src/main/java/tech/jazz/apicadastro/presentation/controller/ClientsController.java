package tech.jazz.apicadastro.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jazz.apicadastro.applicationservice.clientsservice.CreateClientsService;
import tech.jazz.apicadastro.applicationservice.clientsservice.SearchClientsService;
import tech.jazz.apicadastro.applicationservice.dto.ClientRequestDto;
import tech.jazz.apicadastro.infrastructure.domain.Client;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apicadastro.presentation.handler.exceptions.DuplicateCpfException;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientsController {
    private final CreateClientsService createClientsService;
    private final SearchClientsService searchClientsService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> findById(@PathVariable UUID id) {
        if (id != null) {
            return ResponseEntity.status(200).body(searchClientsService.findById(id));
        }
        throw new ClientNotFoundException("Please insert client ID");

    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> findClientBy(@RequestParam(required = false) String cpf) {
        return ResponseEntity.status(200).body(searchClientsService.findBy(cpf));
    }

    @PostMapping
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
