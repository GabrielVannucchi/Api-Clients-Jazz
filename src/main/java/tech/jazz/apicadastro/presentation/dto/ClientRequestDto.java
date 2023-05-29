package tech.jazz.apicadastro.presentation.dto;

public record ClientRequestDto(
        String name,
        String cpf,
        String birthdate,
        String cep,
        Integer houseNumber,
        String complement
) {
}
