package tech.jazz.apicadastro.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apicadastro.infrastructure.model.Adress;

import java.util.Optional;

public interface AdressRepository extends JpaRepository<Adress, Long> {
    Optional<Adress> findByCep(String cep);

}
