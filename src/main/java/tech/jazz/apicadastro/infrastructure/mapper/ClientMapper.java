package tech.jazz.apicadastro.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import tech.jazz.apicadastro.applicationservice.dto.ClientRequestDto;
import tech.jazz.apicadastro.infrastructure.domain.Client;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ClientMapper {

    Client from(ClientRequestDto clientRequestDto);

    ClientEntity from(Client client);

    ClientResponseDto from(ClientEntity clientEntity);

    //ClientResponseDto from(Client c);
}
