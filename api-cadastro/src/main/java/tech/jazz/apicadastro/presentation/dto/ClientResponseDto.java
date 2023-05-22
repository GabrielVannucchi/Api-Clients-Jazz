package tech.jazz.apicadastro.presentation.dto;

import tech.jazz.apicadastro.infrastructure.model.Adress;

public record ClientResponseDto(
        String id,
        String name,
        String cpf,
        String birthdate,
        Adress adress
) {
}
