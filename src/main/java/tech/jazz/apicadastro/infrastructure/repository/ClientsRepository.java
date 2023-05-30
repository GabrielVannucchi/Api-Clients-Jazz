package tech.jazz.apicadastro.infrastructure.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;

public interface ClientsRepository extends JpaRepository<ClientEntity, String> {
    Optional<ClientEntity> findFirstById(String id);

    Optional<ClientEntity> findFirstByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
