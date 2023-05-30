package tech.jazz.apicadastro.presentation.dto;

import lombok.Builder;
import tech.jazz.apicadastro.infrastructure.domain.Adress;

public record ClientResponseDto(
        String id,
        String name,
        String cpf,
        String birthdate,
        Adress adress
) {
    @Builder
    public ClientResponseDto(String id, String name, String cpf, String birthdate, Adress adress) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.birthdate = birthdate;
        this.adress = adress;
    }
}
