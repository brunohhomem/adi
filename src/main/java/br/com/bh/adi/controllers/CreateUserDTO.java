package br.com.bh.adi.controllers;

public record CreateUserDTO(String username, String email, String password) {
    //Esse DTO expoe apenas os atributos necessarios para criação de um usuario entidade
    //de forma q atributos que quem manipular a inserção, nao tem acesso ao id, nem aos timestamps
}
