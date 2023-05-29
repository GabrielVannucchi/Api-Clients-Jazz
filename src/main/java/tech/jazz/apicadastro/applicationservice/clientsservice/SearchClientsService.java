package tech.jazz.apicadastro.applicationservice.clientsservice;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.jazz.apicadastro.infrastructure.repository.ClientsRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchClientsService {
    private Logger logger = LoggerFactory.getLogger(SearchClientsService.class);
    private final ClientsRepository clientsRepository;
    private ViaCepClient viaCepClient;

    public List<ClientEntity> listAll() {
        return clientsRepository.findAll();

    }

    public ClientEntity findById(String id){
        ClientEntity client = clientsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "client not found"));
        //logger.info(String.format("Usuario %s encontrado no id %d", client.getNome(), client.getId()));
        return client;
    }

    public ClientEntity findByCpf(String cpf){
        if(cpf.length() == 14){
            cpf = cpf.replaceAll("[.-]", "");
        }
        ClientEntity client = clientsRepository.findFirstByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "client not found"));
        //logger.info(String.format("Usuario %s encontrado no cpf %s", client.getNome(), client.getCpf()));
        return client;
    }

    public boolean existsByCpf(String cpf){
        return clientsRepository.existsByCpf(cpf);
    }
}
