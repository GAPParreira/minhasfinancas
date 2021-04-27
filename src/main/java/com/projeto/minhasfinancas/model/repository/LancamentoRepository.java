package com.projeto.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projeto.minhasfinancas.model.entity.Lancamentos;
import com.projeto.minhasfinancas.model.enums.StatusLancamento;
import com.projeto.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {

	@Query( value = 
			"SELECT SUM(l.valor) FROM Lancamentos l join l.usuario u "
			+ "WHERE u.id = :idUsuario AND l.tipo = :tipo AND l.status = :status GROUP BY u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus( 
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status);
}
