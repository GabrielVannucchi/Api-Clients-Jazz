package tech.jazz.apicadastro.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;

public interface ClientsRepository extends JpaRepository<ClientEntity, String> {
    Optional<ClientEntity> findFirstById(String id);

    List<ClientEntity> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
