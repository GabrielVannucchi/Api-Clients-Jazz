package tech.jazz.apicadastro.infrastructure.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import tech.jazz.apicadastro.presentation.dto.AdressDto;

@Entity
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private Integer houseNumber;
    @Size(max = 10)
    private String complement;


    public Adress(Long id, String cep, String street, String neighborhood, String city, String state, Integer houseNumber, String complement) {
        this.id = id;
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.houseNumber = houseNumber;
        this.complement = complement;
    }

    public Adress(AdressDto adressDto, Integer houseNumber, String complement) {
        this.cep = adressDto.cep();
        this.street = adressDto.logradouro();
        this.neighborhood = adressDto.bairro();
        this.city = adressDto.localidade();
        this.state = adressDto.uf();
        this.houseNumber = houseNumber;
        this.complement = complement;
    }


    public Adress() {
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "id=" + id +
                ", cep='" + cep + '\'' +
                ", logradouro='" + street + '\'' +
                ", bairro='" + neighborhood + '\'' +
                ", localidade='" + city + '\'' +
                ", uf='" + state + '\'' +
                ", numero='" + houseNumber + '\'' +
                ", complement='" + complement + '\'' +
                '}';
    }
}
