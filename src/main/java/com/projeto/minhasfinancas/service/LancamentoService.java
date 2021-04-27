package com.projeto.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.projeto.minhasfinancas.model.entity.Lancamentos;
import com.projeto.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamentos salvar(Lancamentos lancamentos);
	
	Lancamentos atualizar(Lancamentos lancamentos);
	
	void deletar(Lancamentos lancamentos);
	
	List<Lancamentos> buscar(Lancamentos lancamentos);
	
	void atualizarStatus(Lancamentos lancamentos,StatusLancamento status);
	
	void validar(Lancamentos lancamentos);
	
	Optional<Lancamentos> obterPorId(Long id);
	
	BigDecimal obterSaldoPorUsuario(Long id);
}
