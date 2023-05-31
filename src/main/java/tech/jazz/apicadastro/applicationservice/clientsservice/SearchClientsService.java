package tech.jazz.apicadastro.applicationservice.clientsservice;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
import tech.jazz.apicadastro.presentation.handler.exceptions.UuidOutOfFormatException;

@Service
@RequiredArgsConstructor
public class SearchClientsService {
    private Logger logger = LoggerFactory.getLogger(SearchClientsService.class);
    private final ClientsRepository clientsRepository;
    private final ClientMapper clientMapper;

    private final String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String cpfregex = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
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
        if (!Pattern.matches(uuidRegex, id)) {
            throw new UuidOutOfFormatException("Id out of pattern. Insert correct UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX");
        }
        final ClientEntity client = clientsRepository.findFirstById(id)
                .orElseThrow(() -> new ClientNotFoundException("client not found"));
        //logger.info(String.format("Usuario %s encontrado no id %d", client.getNome(), client.getId()));
        return clientMapper.from(client);
    }

    public ClientResponseDto findByCpf(String cpf) {
        cpf = verifyValidCpf(cpf);
        final ClientEntity client = clientsRepository.findFirstByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("client not found"));
        return clientMapper.from(client);
    }

    public boolean existsByCpf(String cpf) {
        cpf = verifyValidCpf(cpf);
        return clientsRepository.existsByCpf(cpf);
    }

    private String verifyValidCpf(String cpf) {
        if (!cpf.matches(cpfregex)) {
            throw new CpfOutOfFormatException("Cpf out of format");
        }
        if (cpf.length() == 14) {
            cpf = cpf.replaceAll("[.-]", "");
        }
        return cpf;
    }
}
