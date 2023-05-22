package tech.jazz.apicadastro.infrastructure.mapper;

import org.mapstruct.Mapper;
import tech.jazz.apicadastro.infrastructure.model.Client;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientRequestDto;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client from(ClientRequestDto clientRequestDto);

    ClientEntity from(Client client);

    //ClientResponseDto from(Client c);
}
