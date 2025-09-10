package br.edu.infnet.repository;

import br.edu.infnet.model.entity.Notificacao;
import br.edu.infnet.model.enums.TipoNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de persistência da entidade Notificacao
 */
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    /**
     * Busca notificações por cliente
     */
    List<Notificacao> findByClienteIdOrderByDataEnvioDesc(Long clienteId);

    /**
     * Busca notificações por pedido
     */
    List<Notificacao> findByPedidoIdOrderByDataEnvioDesc(Long pedidoId);

    /**
     * Busca notificações não lidas por cliente
     */
    List<Notificacao> findByClienteIdAndLidaFalseOrderByDataEnvioDesc(Long clienteId);

    /**
     * Busca notificações por tipo
     */
    List<Notificacao> findByTipoOrderByDataEnvioDesc(TipoNotificacao tipo);

    /**
     * Busca notificações por cliente e tipo
     */
    List<Notificacao> findByClienteIdAndTipoOrderByDataEnvioDesc(Long clienteId, TipoNotificacao tipo);

    /**
     * Busca notificações enviadas em um período
     */
    @Query("SELECT n FROM Notificacao n WHERE n.dataEnvio BETWEEN :inicio AND :fim ORDER BY n.dataEnvio DESC")
    List<Notificacao> findByDataEnvioBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Conta notificações não lidas por cliente
     */
    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.cliente.id = :clienteId AND n.lida = false")
    long countNotificacaoesNaoLidasPorCliente(@Param("clienteId") Long clienteId);

    /**
     * Busca notificações recentes (últimas 24h)
     */
    @Query("SELECT n FROM Notificacao n WHERE n.dataEnvio >= :dataLimite ORDER BY n.dataEnvio DESC")
    List<Notificacao> findNotificacoesRecentes(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca últimas N notificações de um cliente
     */
    @Query("SELECT n FROM Notificacao n WHERE n.cliente.id = :clienteId ORDER BY n.dataEnvio DESC LIMIT :limite")
    List<Notificacao> findTopNByClienteId(@Param("clienteId") Long clienteId, @Param("limite") int limite);

    /**
     * Verifica se existe notificação para um pedido e cliente específicos
     */
    boolean existsByPedidoIdAndClienteId(Long pedidoId, Long clienteId);

    /**
     * Conta total de notificações por tipo
     */
    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.tipo = :tipo")
    long countByTipo(@Param("tipo") TipoNotificacao tipo);

    /**
     * Busca notificações por cliente com paginação
     */
    @Query("SELECT n FROM Notificacao n WHERE n.cliente.id = :clienteId ORDER BY n.dataEnvio DESC")
    List<Notificacao> findByClienteIdWithPagination(@Param("clienteId") Long clienteId);

    /**
     * Marca todas as notificações de um cliente como lidas
     */
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.cliente.id = :clienteId AND n.lida = false")
    int marcarTodasComoLidasPorCliente(@Param("clienteId") Long clienteId);

    /**
     * Remove notificações antigas (mais de 30 dias)
     */
    @Query("DELETE FROM Notificacao n WHERE n.dataEnvio < :dataLimite")
    int removerNotificacoesAntigas(@Param("dataLimite") LocalDateTime dataLimite);
}