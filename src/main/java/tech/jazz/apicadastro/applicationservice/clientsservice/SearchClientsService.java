package tech.jazz.apicadastro.applicationservice.clientsservice;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.jazz.apicadastro.infrastructure.mapper.ClientMapper;
import tech.jazz.apicadastro.infrastructure.repository.ClientsRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;

@Service
@RequiredArgsConstructor
public class SearchClientsService {
    private Logger logger = LoggerFactory.getLogger(SearchClientsService.class);
    private final ClientsRepository clientsRepository;
    private final ClientMapper clientMapper;
    private ViaCepClient viaCepClient;

    public List<ClientResponseDto> listAll() {
        final List<ClientResponseDto> clientResponseDtoList = new ArrayList<>();
        for (ClientEntity entity:
                clientsRepository.findAll()) {
            clientResponseDtoList.add(clientMapper.from(entity));
        }
        return clientResponseDtoList;
    }

    public ClientResponseDto findById(String id) {
        final ClientEntity client = clientsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "client not found"));
        //logger.info(String.format("Usuario %s encontrado no id %d", client.getNome(), client.getId()));
        return clientMapper.from(client);
    }

    public ClientEntity findByCpf(String cpf) {
        if (cpf.length() == 14) {
            cpf = cpf.replaceAll("[.-]", "");
        }
        final ClientEntity client = clientsRepository.findFirstByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "client not found"));
        //logger.info(String.format("Usuario %s encontrado no cpf %s", client.getNome(), client.getCpf()));
        return client;
    }

    public boolean existsByCpf(String cpf) {
        return clientsRepository.existsByCpf(cpf);
    }
}
