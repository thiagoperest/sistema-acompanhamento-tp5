package br.edu.infnet.repository;

import br.edu.infnet.model.entity.HistoricoStatus;
import br.edu.infnet.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade HistoricoStatus
 */
@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {

    /**
     * Busca histórico por pedido ID ordenado por data (mais recente primeiro)
     */
    List<HistoricoStatus> findByPedidoIdOrderByDataAtualizacaoDesc(Long pedidoId);

    /**
     * Busca histórico por pedido ID ordenado por data (mais antigo primeiro)
     */
    List<HistoricoStatus> findByPedidoIdOrderByDataAtualizacaoAsc(Long pedidoId);

    /**
     * Busca último status de um pedido
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.pedido.id = :pedidoId ORDER BY h.dataAtualizacao DESC")
    Optional<HistoricoStatus> findUltimoStatusPedido(@Param("pedidoId") Long pedidoId);

    /**
     * Busca histórico por status
     */
    List<HistoricoStatus> findByStatus(StatusPedido status);

    /**
     * Busca histórico por status ordenado por data
     */
    List<HistoricoStatus> findByStatusOrderByDataAtualizacaoDesc(StatusPedido status);

    /**
     * Busca histórico por responsável
     */
    List<HistoricoStatus> findByResponsavelIgnoreCase(String responsavel);

    /**
     * Busca histórico por responsável e status
     */
    List<HistoricoStatus> findByResponsavelIgnoreCaseAndStatus(String responsavel, StatusPedido status);

    /**
     * Busca histórico por período de data
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.dataAtualizacao BETWEEN :dataInicio AND :dataFim ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findByDataAtualizacaoBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                                       @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca histórico por pedido e período
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.pedido.id = :pedidoId AND h.dataAtualizacao BETWEEN :dataInicio AND :dataFim ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findByPedidoIdAndDataAtualizacaoBetween(@Param("pedidoId") Long pedidoId,
                                                                  @Param("dataInicio") LocalDateTime dataInicio,
                                                                  @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca histórico por cliente (através do pedido)
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.pedido.cliente.id = :clienteId ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findByClienteId(@Param("clienteId") Long clienteId);

    /**
     * Busca histórico por cliente e status
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.pedido.cliente.id = :clienteId AND h.status = :status ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findByClienteIdAndStatus(@Param("clienteId") Long clienteId, @Param("status") StatusPedido status);

    /**
     * Conta histórico por pedido
     */
    @Query("SELECT COUNT(h) FROM HistoricoStatus h WHERE h.pedido.id = :pedidoId")
    long countByPedidoId(@Param("pedidoId") Long pedidoId);

    /**
     * Conta histórico por status
     */
    @Query("SELECT COUNT(h) FROM HistoricoStatus h WHERE h.status = :status")
    long countByStatus(@Param("status") StatusPedido status);

    /**
     * Conta histórico por responsável
     */
    @Query("SELECT COUNT(h) FROM HistoricoStatus h WHERE LOWER(h.responsavel) = LOWER(:responsavel)")
    long countByResponsavel(@Param("responsavel") String responsavel);

    /**
     * Busca primeiro status de um pedido
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.pedido.id = :pedidoId ORDER BY h.dataAtualizacao ASC")
    Optional<HistoricoStatus> findPrimeiroStatusPedido(@Param("pedidoId") Long pedidoId);

    /**
     * Verifica se pedido já tem um status específico
     */
    @Query("SELECT COUNT(h) > 0 FROM HistoricoStatus h WHERE h.pedido.id = :pedidoId AND h.status = :status")
    boolean existsByPedidoIdAndStatus(@Param("pedidoId") Long pedidoId, @Param("status") StatusPedido status);

    /**
     * Busca mudanças de status recentes (últimas 24 horas)
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.dataAtualizacao >= :dataLimite ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findStatusRecentes(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca histórico com observações não vazias
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.observacao IS NOT NULL AND h.observacao != '' ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findHistoricoComObservacoes();

    /**
     * Busca histórico por pedido número
     */
    @Query("SELECT h FROM HistoricoStatus h WHERE h.pedido.numeroPedido = :numeroPedido ORDER BY h.dataAtualizacao DESC")
    List<HistoricoStatus> findByPedidoNumeroPedido(@Param("numeroPedido") String numeroPedido);
}