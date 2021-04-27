package com.projeto.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.minhasfinancas.exception.ErroAutenticacao;
import com.projeto.minhasfinancas.exception.RegraNegocioException;
import com.projeto.minhasfinancas.model.entity.Usuario;
import com.projeto.minhasfinancas.model.repository.UsuarioRepository;
import com.projeto.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

//Esta notação faz com que o container do spring gerencie uma instancia desta classe
@Service
//Com esta notação e o "final" é feito o construtor
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	
	private final UsuarioRepository repository;
	
	/*
	 * public UsuarioServiceImpl(UsuarioRepository repository) { super();
	 * this.repository = repository; }
	 */

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	//Esta anotação ira executar o metodo de salvar o usuario e depois que for salvo, ira commitar
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email"); 
		}
		
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}
