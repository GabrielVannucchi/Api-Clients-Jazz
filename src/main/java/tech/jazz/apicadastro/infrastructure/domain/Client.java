package tech.jazz.apicadastro.infrastructure.domain;


import feign.FeignException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;
import tech.jazz.apicadastro.presentation.handler.exceptions.ClientNotFoundException;

public record Client(
        @NotBlank
        @Size(min = 4, max = 40)
        String name,
        @CPF
        String cpf,
        @PastOrPresent
        LocalDate birthdate,
        @OneToOne(cascade = CascadeType.ALL)
        Adress adress
) {

    @Builder(toBuilder = true)
    public Client(String name, String cpf, LocalDate birthdate, Adress adress) {
        this.name = name;
        this.cpf = cpf;
        this.birthdate = birthdate;
        this.adress = adress;
    }

    public Client addAdress(Adress adress) {
        return this.toBuilder().adress(adress).build();
    }
}
