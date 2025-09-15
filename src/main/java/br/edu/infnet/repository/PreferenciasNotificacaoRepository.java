package br.edu.infnet.repository;

import br.edu.infnet.model.entity.PreferenciasNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade PreferenciasNotificacao
 */
@Repository
public interface PreferenciasNotificacaoRepository extends JpaRepository<PreferenciasNotificacao, Long> {

    /**
     * Busca preferências por cliente
     * Relacionamento 1:1 entre Cliente e PreferenciasNotificacao
     */
    Optional<PreferenciasNotificacao> findByClienteId(Long clienteId);

    /**
     * Busca clientes com email ativo
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE p.emailAtivo = true")
    List<PreferenciasNotificacao> findByEmailAtivoTrue();

    /**
     * Busca clientes com SMS ativo
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE p.smsAtivo = true")
    List<PreferenciasNotificacao> findBySmsAtivoTrue();

    /**
     * Busca clientes com push ativo
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE p.pushAtivo = true")
    List<PreferenciasNotificacao> findByPushAtivoTrue();

    /**
     * Busca clientes com pelo menos uma notificação ativa
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE p.emailAtivo = true OR p.smsAtivo = true OR p.pushAtivo = true")
    List<PreferenciasNotificacao> findComAlgumaNotificacaoAtiva();

    /**
     * Busca clientes sem nenhuma notificação ativa
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE p.emailAtivo = false AND p.smsAtivo = false AND p.pushAtivo = false")
    List<PreferenciasNotificacao> findSemNotificacoesAtivas();

    /**
     * Busca clientes que podem receber notificação no horário atual
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE " +
           "(p.emailAtivo = true OR p.smsAtivo = true OR p.pushAtivo = true) AND " +
           "(:horarioAtual BETWEEN p.horarioInicio AND p.horarioFim)")
    List<PreferenciasNotificacao> findClientesDisponiveis(@Param("horarioAtual") LocalTime horarioAtual);

    /**
     * Busca preferências por faixa de horário
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE p.horarioInicio >= :inicio AND p.horarioFim <= :fim")
    List<PreferenciasNotificacao> findByHorarioEntre(@Param("inicio") LocalTime inicio, @Param("fim") LocalTime fim);

    /**
     * Conta clientes com email ativo
     */
    @Query("SELECT COUNT(p) FROM PreferenciasNotificacao p WHERE p.emailAtivo = true")
    long countByEmailAtivoTrue();

    /**
     * Conta clientes com SMS ativo
     */
    @Query("SELECT COUNT(p) FROM PreferenciasNotificacao p WHERE p.smsAtivo = true")
    long countBySmsAtivoTrue();

    /**
     * Conta clientes com push ativo
     */
    @Query("SELECT COUNT(p) FROM PreferenciasNotificacao p WHERE p.pushAtivo = true")
    long countByPushAtivoTrue();

    /**
     * Verifica se cliente tem preferências cadastradas
     */
    boolean existsByClienteId(Long clienteId);

    /**
     * Busca clientes por horário comercial (8h às 18h)
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE " +
           "p.horarioInicio = :horarioComercialInicio AND p.horarioFim = :horarioComercialFim")
    List<PreferenciasNotificacao> findComHorarioComercial(@Param("horarioComercialInicio") LocalTime horarioComercialInicio,
                                                          @Param("horarioComercialFim") LocalTime horarioComercialFim);

    /**
     * Busca clientes por horário estendido (8h às 22h)
     */
    @Query("SELECT p FROM PreferenciasNotificacao p WHERE " +
           "p.horarioInicio = :horarioEstendidoInicio AND p.horarioFim = :horarioEstendidoFim")
    List<PreferenciasNotificacao> findComHorarioEstendido(@Param("horarioEstendidoInicio") LocalTime horarioEstendidoInicio,
                                                          @Param("horarioEstendidoFim") LocalTime horarioEstendidoFim);

    /**
     * Ativa email para todos os clientes
     */
    @Query("UPDATE PreferenciasNotificacao p SET p.emailAtivo = true")
    int ativarEmailParaTodos();

    /**
     * Desativa email para todos os clientes
     */
    @Query("UPDATE PreferenciasNotificacao p SET p.emailAtivo = false")
    int desativarEmailParaTodos();

    /**
     * Define horário padrão para clientes sem configuração
     */
    @Query("UPDATE PreferenciasNotificacao p SET p.horarioInicio = :inicio, p.horarioFim = :fim WHERE p.horarioInicio IS NULL OR p.horarioFim IS NULL")
    int definirHorarioPadrao(@Param("inicio") LocalTime inicio, @Param("fim") LocalTime fim);
}