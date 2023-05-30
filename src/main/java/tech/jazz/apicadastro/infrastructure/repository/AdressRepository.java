package tech.jazz.apicadastro.infrastructure.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicadastro.infrastructure.domain.Adress;

public interface AdressRepository extends JpaRepository<Adress, Long> {
    Optional<Adress> findByCep(String cep);

}
