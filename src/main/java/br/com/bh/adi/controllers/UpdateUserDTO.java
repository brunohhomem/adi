package br.com.bh.adi.controllers;

public record UpdateUserDTO(String username, String password) {
    //Esse dto, permite a atualização de apenas 2 atributos da entidade

}
