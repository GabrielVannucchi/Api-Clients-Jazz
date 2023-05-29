package tech.jazz.apicadastro.applicationservice.clientsservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.jazz.apicadastro.presentation.dto.AdressDto;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws/")
public interface ViaCepClient {
    @GetMapping("/{cep}/json")
    AdressDto getCep(@PathVariable String cep);

}
