package com.chioratto.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

//import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;

import com.projeto.minhasfinancas.exception.ErroAutenticacao;
import com.projeto.minhasfinancas.exception.RegraNegocioException;
import com.projeto.minhasfinancas.model.entity.Usuario;
import com.projeto.minhasfinancas.model.repository.UsuarioRepository;
import com.projeto.minhasfinancas.service.impl.UsuarioServiceImpl;

//@SpringBootTest
//@RunWith(SpringRunner.class)
@ExtendWith( SpringExtension.class )
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	//@Autowired
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	//@Autowired
	UsuarioRepository repository;
	
	//Esta anotação indica que este metodo seja executado ante dos teste
	/*@BeforeEach
	public void setUp() {
		//Com a anotação @MockBean não precisa mais desse trecho
		//repository = Mockito.mock(UsuarioRepository.class);
		
		//service = new UsuarioServiceImpl(repository);
		//
		//service = Mockito.spy(UsuarioServiceImpl.class);
	}*/
	
	@Test
	public void deveSalvarUmUsuario() {
		//Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1L)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//Ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificação
		//Aula 49
		//Preciso pegar os dados do retorno do teste e comparar para verificar se foi inserido corretamente
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//Cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//Ação
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.salvarUsuario(usuario);
		});
		
		//Verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Ação
		Usuario result = service.autenticar(email, senha);
		
		//Verificação
		Assertions.assertDoesNotThrow(() -> result);
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioComOEmailInformado() {
		//cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//Ação
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			service.autenticar("email@email.com", "senha");
		});
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenário
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//Ação
		//Aula 47 preciso pegar o exception e comparar com a mensagem que deve ser lançada  
		
		//Verificação
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			service.autenticar("email@email.com", "123");
		});
	}
	
	@Test
	public void deveValidarEmail() {
		//Cenario
		//repository.deleteAll();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("email@email.com");
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//Cenario
		//Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		//repository.save(usuario);
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//Ação
		//Isso indica que sera lançado uma exceção
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.validarEmail("email@email.com");
		});
	}

}
