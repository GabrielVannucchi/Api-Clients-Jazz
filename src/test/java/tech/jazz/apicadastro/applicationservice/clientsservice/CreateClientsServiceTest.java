package tech.jazz.apicadastro.applicationservice.clientsservice;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.jazz.apicadastro.infrastructure.domain.Adress;
import tech.jazz.apicadastro.infrastructure.mapper.ClientMapper;
import tech.jazz.apicadastro.infrastructure.mapper.ClientMapperImpl;
import tech.jazz.apicadastro.infrastructure.repository.ClientsRepository;
import tech.jazz.apicadastro.infrastructure.repository.entity.ClientEntity;
import tech.jazz.apicadastro.presentation.dto.AdressDto;
import tech.jazz.apicadastro.applicationservice.dto.ClientRequestDto;
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepFormatException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateClientsServiceTest {
    @Mock
    ViaCepClient viaCepClient;
    @Mock
    private ClientsRepository clientsRepository;
    @Spy
    private ClientMapper clientMapper = new ClientMapperImpl();
    @InjectMocks
    private CreateClientsService createClientsService;
    @Captor
    private ArgumentCaptor<String> cepCaptor;
    @Captor
    private ArgumentCaptor<ClientEntity> entityCaptor;

    @Test
    void should_create_client(){
        Mockito.when(viaCepClient.getCep(cepCaptor.capture())).thenReturn(adressDtoFactory());
        Mockito.when(clientsRepository.save(entityCaptor.capture())).thenReturn(clientEntityFactory());

        ClientResponseDto response = createClientsService.save(clientRequestFactory());
        assertNotNull(response);
        assertNotNull(response.id());
    }



    @Test
    void should_throw_InvalidCepFormatException_when_cep_is_out_of_format(){

        assertThrows(InvalidCepFormatException.class, () -> createClientsService.save(
                ClientRequestDto.builder()
                        .cep("01234567890")
                        .build()
        ));
    }
    @Test
    void should_throw_InvalidCepException_when_cep_is_invalid(){
        Mockito.when(viaCepClient.getCep(cepCaptor.capture())).thenReturn(
                AdressDto.builder().cep(null).build()
        );

        assertThrows(InvalidCepException.class, () -> createClientsService.save(
                ClientRequestDto.builder()
                        .cep("03733000")
                        .build()
        ));
    }




    private ClientRequestDto clientRequestFactory(){
        return ClientRequestDto.builder()
                .name("Mauro Almeida")
                .cpf("01234567890")
                .birthdate("1997-04-10")
                .cep("03733000")
                .houseNumber(75)
                .complement("apto 73b")
                .build();
    }
    private AdressDto adressDtoFactory(){
        return AdressDto.builder()
                .cep("03733000")
                .logradouro("Avenida Teste")
                .complemento("vazio")
                .bairro("Bairro Testado")
                .localidade("Testera")
                .uf("TT")
                .ibge("0123456")
                .gia("0123")
                .ddd("00")
                .siafi("1234")
                .build();
    }
    private ClientEntity clientEntityFactory(){
        return ClientEntity.builder()
                .name("Teste da Silva")
                .cpf("01234567890")
                .birthdate(LocalDate.of(2011,11,11))
                .adress(new Adress(adressDtoFactory(), 11, "teste"))
                .build();
    }
}
