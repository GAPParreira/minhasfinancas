package com.chioratto.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projeto.minhasfinancas.model.entity.Lancamentos;
import com.projeto.minhasfinancas.model.enums.StatusLancamento;
import com.projeto.minhasfinancas.model.enums.TipoLancamento;
import com.projeto.minhasfinancas.model.repository.LancamentoRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamentos lancamentos = criarLancamento();
		
		lancamentos = repository.save(lancamentos);
		
		Assertions.assertThat(lancamentos.getId()).isNotNull();

	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamentos lancamentos = criarLancamento();
		entityManager.persist(lancamentos);
		
		lancamentos = entityManager.find(Lancamentos.class, lancamentos.getId());
		
		repository.delete(lancamentos);
		
		Lancamentos lancamentosInexistentes = entityManager.find(Lancamentos.class,lancamentos.getId());
		Assertions.assertThat(lancamentosInexistentes).isNull();
	}
	
	private Lancamentos criarLancamento() {
		return Lancamentos.builder()
				.ano(2019)
				.mes(1)
				.descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();
	}
}
