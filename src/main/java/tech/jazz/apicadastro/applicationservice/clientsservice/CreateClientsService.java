package tech.jazz.apicadastro.applicationservice.clientsservice;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jazz.apicadastro.infrastructure.mapper.ClientMapper;
import tech.jazz.apicadastro.infrastructure.model.Adress;
import tech.jazz.apicadastro.infrastructure.model.Client;
import tech.jazz.apicadastro.infrastructure.repository.AdressRepository;
import tech.jazz.apicadastro.infrastructure.repository.ClientsRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientRequestDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepFormatException;

@Service
@RequiredArgsConstructor
public class CreateClientsService {
    private Logger logger = LoggerFactory.getLogger(CreateClientsService.class);
    private final ClientsRepository clientsRepository;
    private final AdressRepository adressRepository;
    private final ViaCepClient viaCepClient;
    private final SearchClientsService searchClientsService;
    private final ClientMapper clientMapper;

    public ClientEntity save(ClientRequestDto clientRequestDto){
        Client client = clientMapper.from(clientRequestDto);
        Adress adress = validateAdress(clientRequestDto.cep(), clientRequestDto.houseNumber(), clientRequestDto.complement());
        //client.setEndereco(validateAdress(clientRequestDto.cep(), clientRequestDto.houseNumber(), clientRequestDto.complement()));
        Client clientWithAdress = client.addAdress(adress);
        ClientEntity clientEntity = clientMapper.from(clientWithAdress);
        logger.info(clientEntity.getName());
        logger.info(clientEntity.getCpf());
        logger.info("aaaaaaaaaaaaaaaaaa");
        logger.info(clientWithAdress.name());
        clientsRepository.save(clientEntity);
        logger.info(String.format("Usuario %s cadastrado com sucesso no id %s", clientEntity.getName(), clientEntity.getId()));
        return clientEntity;
    }
    public void deleteById(String id){
        ClientEntity client = searchClientsService.findById(id);
        clientsRepository.delete(client);
        //logger.info(String.format("Usuario %s deletado com sucesso do id %d", client.getNome(), client.getId()));
    }
    private Adress validateAdress(String cep, Integer numResidencia, String complemento){
        if (!(cep.matches("^\\d{8}|\\d{5}-\\d{3}$"))){
            throw new InvalidCepFormatException();
        }
        Adress adress = new Adress(viaCepClient.getCep(cep),numResidencia, complemento);
        if (adress.getCep() == null){
            throw new InvalidCepException();
        }
        return adress;
    }
}
