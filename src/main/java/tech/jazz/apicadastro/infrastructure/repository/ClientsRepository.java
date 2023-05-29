package tech.jazz.apicadastro.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicadastro.infrastructure.model.Client;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;

import java.util.Optional;

public interface ClientsRepository extends JpaRepository<ClientEntity, String> {
    Optional<ClientEntity> findFirstById(String id);
    Optional<ClientEntity> findFirstByCpf(String cpf);
    boolean existsByCpf(String cpf);

}
