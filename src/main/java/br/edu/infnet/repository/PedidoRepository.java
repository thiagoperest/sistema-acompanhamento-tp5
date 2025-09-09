package br.edu.infnet.repository;

import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Pedido
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca pedido por número do pedido
     */
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    /**
     * Busca pedidos por cliente ID
     */
    List<Pedido> findByClienteId(Long clienteId);

    /**
     * Busca pedidos por cliente ID ordenados por data de compra (mais recentes primeiro)
     */
    List<Pedido> findByClienteIdOrderByDataCompraDesc(Long clienteId);

    /**
     * Busca pedidos por status
     */
    List<Pedido> findByStatus(StatusPedido status);

    /**
     * Busca pedidos por status ordenados por data de compra
     */
    List<Pedido> findByStatusOrderByDataCompraDesc(StatusPedido status);

    /**
     * Busca pedidos por cliente e status
     */
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);

    /**
     * Busca pedidos por período de data de compra
     */
    @Query("SELECT p FROM Pedido p WHERE p.dataCompra BETWEEN :dataInicio AND :dataFim ORDER BY p.dataCompra DESC")
    List<Pedido> findByDataCompraBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                         @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca pedidos por cliente e período
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataCompra BETWEEN :dataInicio AND :dataFim ORDER BY p.dataCompra DESC")
    List<Pedido> findByClienteIdAndDataCompraBetween(@Param("clienteId") Long clienteId,
                                                     @Param("dataInicio") LocalDateTime dataInicio,
                                                     @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca pedidos por número do pedido (busca parcial)
     */
    @Query("SELECT p FROM Pedido p WHERE LOWER(p.numeroPedido) LIKE LOWER(CONCAT('%', :numeroPedido, '%')) ORDER BY p.dataCompra DESC")
    List<Pedido> findByNumeroPedidoContainingIgnoreCase(@Param("numeroPedido") String numeroPedido);

    /**
     * Busca pedidos por nome do cliente (busca parcial)
     */
    @Query("SELECT p FROM Pedido p WHERE LOWER(p.cliente.nome) LIKE LOWER(CONCAT('%', :nomeCliente, '%')) ORDER BY p.dataCompra DESC")
    List<Pedido> findByClienteNomeContainingIgnoreCase(@Param("nomeCliente") String nomeCliente);

    /**
     * Busca pedidos com previsão de entrega vencida
     */
    @Query("SELECT p FROM Pedido p WHERE p.previsaoEntrega < CURRENT_DATE AND p.status NOT IN ('ENTREGUE', 'CANCELADO', 'DEVOLVIDO')")
    List<Pedido> findPedidosComPrevisaoVencida();

    /**
     * Busca pedidos que podem ser cancelados
     */
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('CONFIRMADO', 'PROCESSANDO')")
    List<Pedido> findPedidosCancelaveis();

    /**
     * Conta pedidos por status
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = :status")
    long countByStatus(@Param("status") StatusPedido status);

    /**
     * Conta pedidos por cliente
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId")
    long countByClienteId(@Param("clienteId") Long clienteId);

    /**
     * Conta pedidos por cliente e status
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status = :status")
    long countByClienteIdAndStatus(@Param("clienteId") Long clienteId, @Param("status") StatusPedido status);

    /**
     * Verifica se existe pedido com número (excluindo o próprio ID)
     */
    @Query("SELECT COUNT(p) > 0 FROM Pedido p WHERE p.numeroPedido = :numeroPedido AND p.id != :id")
    boolean existsByNumeroPedidoAndIdNot(@Param("numeroPedido") String numeroPedido, @Param("id") Long id);

    /**
     * Verifica se existe pedido com número
     */
    boolean existsByNumeroPedido(String numeroPedido);

    /**
     * Busca últimos pedidos por cliente
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.dataCompra DESC")
    List<Pedido> findTop10ByClienteIdOrderByDataCompraDesc(@Param("clienteId") Long clienteId);

    /**
     * Busca pedidos recentes (últimos 30 dias)
     */
    @Query("SELECT p FROM Pedido p WHERE p.dataCompra >= :dataLimite ORDER BY p.dataCompra DESC")
    List<Pedido> findPedidosRecentes(@Param("dataLimite") LocalDateTime dataLimite);
}