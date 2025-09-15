package br.edu.infnet.repository;

import br.edu.infnet.model.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciar operações de Avaliação
 */
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Optional<Avaliacao> findByPedidoId(Long pedidoId);

    List<Avaliacao> findByClienteId(Long clienteId);

    @Query("SELECT a FROM Avaliacao a WHERE a.cliente.id = :clienteId ORDER BY a.dataAvaliacao DESC")
    List<Avaliacao> findByClienteIdOrderByDataAvaliacaoDesc(@Param("clienteId") Long clienteId);

    @Query("SELECT AVG(a.notaAcompanhamento) FROM Avaliacao a")
    Double getMediaNotaAcompanhamento();

    @Query("SELECT AVG(a.notaEntrega) FROM Avaliacao a")
    Double getMediaNotaEntrega();

    @Query("SELECT AVG((a.notaAcompanhamento + a.notaEntrega) / 2.0) FROM Avaliacao a")
    Double getMediaGeral();

    @Query("SELECT COUNT(a) FROM Avaliacao a WHERE a.comentario IS NOT NULL AND a.comentario != ''")
    Long countAvaliacoesComComentario();

    boolean existsByPedidoId(Long pedidoId);
}