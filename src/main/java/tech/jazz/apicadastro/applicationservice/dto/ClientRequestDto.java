package tech.jazz.apicadastro.applicationservice.dto;

import lombok.Builder;

public record ClientRequestDto(
        String name,
        String cpf,
        String birthdate,
        String cep,
        Integer houseNumber,
        String complement
) {
    @Builder
    public ClientRequestDto(String name, String cpf, String birthdate, String cep, Integer houseNumber, String complement) {
        this.name = name;
        this.cpf = cpf;
        this.birthdate = birthdate;
        this.cep = cep;
        this.houseNumber = houseNumber;
        this.complement = complement;
    }
}
