package tech.jazz.apicadastro.applicationservice.clientsservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jazz.apicadastro.infrastructure.mapper.ClientMapper;
import tech.jazz.apicadastro.infrastructure.repository.ClientsRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apicadastro.presentation.handler.exceptions.CpfOutOfFormatException;

@Service
@RequiredArgsConstructor
public class SearchClientsService {
    private Logger logger = LoggerFactory.getLogger(SearchClientsService.class);
    private final ClientsRepository clientsRepository;
    private final ClientMapper clientMapper;
    private final String cpfregex = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
    private ViaCepClient viaCepClient;

    public ClientResponseDto findById(UUID id) {
        final ClientEntity client = clientsRepository.findFirstById(id.toString())
                .orElseThrow(() -> new ClientNotFoundException("client not found"));
        //logger.info(String.format("Usuario %s encontrado no id %d", client.getNome(), client.getId()));
        return clientMapper.from(client);
    }

    public List<ClientResponseDto> findBy(String cpf) {
        final List<ClientEntity> clients;
        if (cpf != null) {
            cpf = correctCpf(cpf);
            clients = clientsRepository.findByCpf(cpf);
            if (clients.size() == 0) {
                throw new ClientNotFoundException("Client not found");
            }
        } else {
            clients = clientsRepository.findAll();
        }
        return clients.stream()
                .map(clientMapper::from)
                .collect(Collectors.toList());
    }

    public boolean existsByCpf(String cpf) {
        cpf = correctCpf(cpf);
        return clientsRepository.existsByCpf(cpf);
    }

    private String correctCpf(String cpf) {
        if (!cpf.matches(cpfregex)) {
            throw new CpfOutOfFormatException("Cpf out of format");
        }
        if (cpf.length() == 14) {
            cpf = cpf.replaceAll("[.-]", "");
        }
        return cpf;
    }


}
