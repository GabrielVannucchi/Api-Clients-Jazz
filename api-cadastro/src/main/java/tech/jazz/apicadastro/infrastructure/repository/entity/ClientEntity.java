package tech.jazz.apicadastro.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;
import tech.jazz.apicadastro.infrastructure.model.Adress;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
