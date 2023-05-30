package tech.jazz.apicadastro.presentation.dto;

import lombok.Builder;

public record AdressDto(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String ibge,
        String gia,
        String ddd,
        String siafi
) {
    @Builder
    public AdressDto(String cep, String logradouro, String complemento, String bairro, String localidade, String uf, String ibge, String gia,
                     String ddd,
                     String siafi) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.ibge = ibge;
        this.gia = gia;
        this.ddd = ddd;
        this.siafi = siafi;
    }
}
