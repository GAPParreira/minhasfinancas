package com.projeto.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.minhasfinancas.exception.RegraNegocioException;
import com.projeto.minhasfinancas.model.entity.Lancamentos;
import com.projeto.minhasfinancas.model.enums.StatusLancamento;
import com.projeto.minhasfinancas.model.enums.TipoLancamento;
import com.projeto.minhasfinancas.model.repository.LancamentoRepository;
import com.projeto.minhasfinancas.service.LancamentoService;

@Service
public class LancamentosServiceImpl implements LancamentoService {

	private LancamentoRepository repository;
	
	public LancamentosServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamentos salvar(Lancamentos lancamentos) {
		validar(lancamentos);
		lancamentos.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamentos);
	}

	@Override
	@Transactional
	public Lancamentos atualizar(Lancamentos lancamentos) {
		Objects.requireNonNull(lancamentos.getId());
		validar(lancamentos);
		return repository.save(lancamentos);
	}

	@Override
	@Transactional
	public void deletar(Lancamentos lancamentos) {
		Objects.requireNonNull(lancamentos.getId());
		repository.delete(lancamentos);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamentos> buscar(Lancamentos lancamentosFiltro) {
		Example example = Example.of(lancamentosFiltro,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamentos lancamentos, StatusLancamento status) {
		lancamentos.setStatus(status);
		atualizar(lancamentos);
	}

	@Override
	public void validar(Lancamentos lancamentos) {
		
		if (lancamentos.getDescricao() == null || lancamentos.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida.");
		}
		
		if (lancamentos.getMes() == null || lancamentos.getMes() < 1 || lancamentos.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if (lancamentos.getAno() == null || lancamentos.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if (lancamentos.getUsuario() == null || lancamentos.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		if (lancamentos.getValor() == null || lancamentos.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		if (lancamentos.getTipo() == null) {
			throw new RegraNegocioException("Informe um Tipo de Lançamento");
		}
	}

	@Override
	public Optional<Lancamentos> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	//Especifica que sera feito uma transação, porém só transações de consulta
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);
		
		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if (despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		
		return receitas.subtract(despesas);
	}

}
