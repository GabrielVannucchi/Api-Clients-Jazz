package tech.jazz.apicadastro.infrastructure.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;
import tech.jazz.apicadastro.infrastructure.domain.Adress;
import tech.jazz.apicadastro.presentation.handler.exceptions.ClientNotFoundException;

@Entity
@Table(name = "CLIENT")
@Immutable
public class ClientEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;
    private String id;
    private String name;
    private String cpf;
    private LocalDate birthdate;
    @OneToOne(cascade = CascadeType.ALL)
    private Adress adress;

    @CreationTimestamp
    @Column(name = "createdAt")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    LocalDateTime updatedAt;

    public ClientEntity() {
    }

    @Builder
    public ClientEntity(String name, String cpf, LocalDate birthdate, Adress adress) {
        if (name == null) {
            throw new NullPointerException("Object is null for mapping");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.cpf = cpf;
        this.birthdate = birthdate;
        this.adress = adress;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Adress getAdress() {
        return adress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
