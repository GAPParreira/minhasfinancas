package com.projeto.minhasfinancas.model.entity;

//import java.math.BigDecimal;
//import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;

//import com.chioratto.minhasfinancas.model.entity.Lancamentos.LancamentosBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Esta notação faz com que o JPA reconheça esta classe como o mapeamento de uma etidade
@Entity
//Esta notação indica qual tabela e qual schema esta entidade representa na base
@Table(name = "usuario", schema = "financas")
//Estas duas anotações se referem ao Lombok
//O Lombok cria os metodos getters, setters, hashCode and equals, toString e constructor. Tudo isso é criado em tempo de compilação
@Data
@Builder
//Esta notação garante que seja feito um construtor sem argumentos
@NoArgsConstructor
//Esta notação garante que seja feito um construtor com todos os argumentos
@AllArgsConstructor
public class Usuario {
	
	//Esta notação indica a qual coluna da tabela do DB esta se referindo
	@Column(name = "id")
	//Esta notação indica que este atributo é a chave primaria do DB
	@Id
	//Esta notação indica que este atributo é auto-increment no DB
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	private String senha;

}
