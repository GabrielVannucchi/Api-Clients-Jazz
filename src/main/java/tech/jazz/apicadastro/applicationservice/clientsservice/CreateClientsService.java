package tech.jazz.apicadastro.applicationservice.clientsservice;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.jazz.apicadastro.applicationservice.dto.ClientRequestDto;
import tech.jazz.apicadastro.infrastructure.domain.Adress;
import tech.jazz.apicadastro.infrastructure.domain.Client;
import tech.jazz.apicadastro.infrastructure.mapper.ClientMapper;
import tech.jazz.apicadastro.infrastructure.repository.ClientsRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepFormatException;

@Service
@RequiredArgsConstructor
public class CreateClientsService {
    private Logger logger = LoggerFactory.getLogger(CreateClientsService.class);
    private final ClientsRepository clientsRepository;
    private final ViaCepClient viaCepClient;
    private final SearchClientsService searchClientsService;
    private final ClientMapper clientMapper;

    public ClientResponseDto save(ClientRequestDto clientRequestDto) {
        final Client client = clientMapper.from(clientRequestDto);
        final Adress adress = validateAdress(clientRequestDto.cep(), clientRequestDto.houseNumber(), clientRequestDto.complement());
        final Client clientWithAdress = client.addAdress(adress);
        final ClientEntity clientEntity = clientMapper.from(clientWithAdress);
        logger.info(clientEntity.getName());
        logger.info(clientEntity.getCpf());
        logger.info(clientWithAdress.name());
        clientsRepository.save(clientEntity);
        final ClientResponseDto clientResponse = clientMapper.from(clientEntity);
        logger.info(String.format("Usuario %s cadastrado com sucesso no id %s", clientEntity.getName(), clientEntity.getId()));
        return clientResponse;
    }

    public void deleteById(String id) {
        final ClientEntity client = clientsRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "client not found"));

        clientsRepository.delete(client);
        //logger.info(String.format("Usuario %s deletado com sucesso do id %d", client.getNome(), client.getId()));
    }

    private Adress validateAdress(String cep, Integer numResidencia, String complemento) {
        if (!(cep.matches("^\\d{8}|\\d{5}-\\d{3}$"))) {
            throw new InvalidCepFormatException();
        }
        final Adress adress = new Adress(viaCepClient.getCep(cep),numResidencia, complemento);
        if (adress.getCep() == null) {
            throw new InvalidCepException();
        }
        return adress;
    }
}
