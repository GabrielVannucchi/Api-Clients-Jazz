package tech.jazz.apicadastro.applicationservice.clientsservice;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import tech.jazz.apicadastro.presentation.dto.ClientResponseDto;
import tech.jazz.apicadastro.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apicadastro.presentation.handler.exceptions.CpfOutOfFormatException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SearchClientsServiceTest {
    @Mock
    ViaCepClient viaCepClient;
    @Mock
    ClientsRepository clientsRepository;
    @Spy
    ClientMapper clientMapper = new ClientMapperImpl();
    @InjectMocks
    SearchClientsService searchClientsService;

    @Captor
    ArgumentCaptor<String> idCaptor;

    @Test
    void should_list_all() {
        Mockito.when(clientsRepository.findAll()).thenReturn(List.of(new ClientEntity(), new ClientEntity(), new ClientEntity()));
        List<ClientResponseDto> clientEntities = searchClientsService.findBy(null);
        assertEquals(3, clientEntities.size());
    }

    @Test
    void should_find_client_by_id() {
        Mockito.when(clientsRepository.findFirstById(idCaptor.capture())).thenReturn(Optional.of(clientEntityFactory()));
        ClientResponseDto responseDto = searchClientsService.findById(UUID.randomUUID());
        assertNotNull(responseDto);
    }

    @Test
    void should_throw_ClientNotFoundException_in_findById() {
        assertThrows(ClientNotFoundException.class, () -> searchClientsService.findById(UUID.randomUUID()));
    }

    @Test
    void should_throw_CpfOutOfFormatException_with_incorrect_CPF_format() {
        assertThrows(CpfOutOfFormatException.class, () -> searchClientsService.findBy("aaaaa"));
    }

    @Test
    void should_throw_ClientNotFoundException_in_findByCpf() {
        assertThrows(ClientNotFoundException.class, () -> searchClientsService.findBy("01234567890"));
    }

    @Test
    void should_find_client_by_cpf() {
        Mockito.when(clientsRepository.findByCpf(idCaptor.capture())).thenReturn(List.of(clientEntityFactory()));
        List<ClientResponseDto> responseDto = searchClientsService.findBy("01234567890");
        assertNotNull(responseDto);
        responseDto = searchClientsService.findBy("012.345.678-90");
        assertNotNull(responseDto);

    }

    @Test
    void should_confirm_client_exist_with_correct_cpf(){
        Mockito.when(clientsRepository.existsByCpf(idCaptor.capture())).thenReturn(true);
        assertTrue(searchClientsService.existsByCpf("01234567890"));
        assertTrue(searchClientsService.existsByCpf("012.345.678-90"));
        assertThrows(CpfOutOfFormatException.class, () -> searchClientsService.existsByCpf("invalid"));
    }



    private ClientResponseDto clientResponseDtoFactory(){
        return ClientResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Teste da Silva")
                .cpf("01234567890")
                .birthdate("2011-11-11")
                .adress(new Adress(1l, "03733000", "Rua Teste", "Bairro Teste", "Cidade Teste", "TT", 11, "11c"))
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
}
