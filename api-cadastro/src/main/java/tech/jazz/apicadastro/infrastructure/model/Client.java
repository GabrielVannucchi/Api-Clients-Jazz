package tech.jazz.apicadastro.infrastructure.model;


import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;

public record Client (
        @NotBlank
        @Size(min = 4, max = 40)
        String name,
        @CPF
        String cpf,
        @PastOrPresent
        LocalDate birthdate,
        @OneToOne(cascade = CascadeType.ALL)
        Adress adress
){

    @Builder(toBuilder = true)
    public Client(String name, String cpf, LocalDate birthdate, Adress adress) {
        this.name = name;
        this.cpf = cpf;
        this.birthdate = birthdate;
        this.adress = adress;
    }

    public Client addAdress(Adress adress){
        return this.toBuilder().adress(adress).build();
    }
}
