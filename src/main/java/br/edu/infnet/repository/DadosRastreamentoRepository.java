package br.edu.infnet.repository;

import br.edu.infnet.model.entity.DadosRastreamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade DadosRastreamento
 */
@Repository
public interface DadosRastreamentoRepository extends JpaRepository<DadosRastreamento, Long> {

    /**
     * Busca dados de rastreamento por pedido
     * Relacionamento 1:1 entre Pedido e DadosRastreamento
     */
    Optional<DadosRastreamento> findByPedidoId(Long pedidoId);

    /**
     * Busca dados de rastreamento por código
     */
    Optional<DadosRastreamento> findByCodigoRastreamento(String codigoRastreamento);

    /**
     * Busca dados de rastreamento por transportadora
     */
    List<DadosRastreamento> findByTransportadoraIgnoreCaseOrderByUltimaAtualizacaoDesc(String transportadora);

    /**
     * Busca rastreamentos atualizados recentemente (últimas 24h)
     */
    @Query("SELECT d FROM DadosRastreamento d WHERE d.ultimaAtualizacao >= :dataLimite ORDER BY d.ultimaAtualizacao DESC")
    List<DadosRastreamento> findRastreamentosRecentes(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca rastreamentos desatualizados (mais de X dias)
     */
    @Query("SELECT d FROM DadosRastreamento d WHERE d.ultimaAtualizacao < :dataLimite ORDER BY d.ultimaAtualizacao ASC")
    List<DadosRastreamento> findRastreamentosDesatualizados(@Param("dataLimite") LocalDateTime dataLimite);

    /**
     * Busca rastreamentos por localização
     */
    @Query("SELECT d FROM DadosRastreamento d WHERE LOWER(d.localizacaoAtual) LIKE LOWER(CONCAT('%', :localizacao, '%')) ORDER BY d.ultimaAtualizacao DESC")
    List<DadosRastreamento> findByLocalizacaoAtualContainingIgnoreCase(@Param("localizacao") String localizacao);

    /**
     * Verifica se existe código de rastreamento (excluindo o próprio ID)
     */
    @Query("SELECT COUNT(d) > 0 FROM DadosRastreamento d WHERE d.codigoRastreamento = :codigo AND d.id != :id")
    boolean existsByCodigoRastreamentoAndIdNot(@Param("codigo") String codigo, @Param("id") Long id);

    /**
     * Verifica se existe código de rastreamento
     */
    boolean existsByCodigoRastreamento(String codigoRastreamento);

    /**
     * Conta rastreamentos por transportadora
     */
    @Query("SELECT COUNT(d) FROM DadosRastreamento d WHERE LOWER(d.transportadora) = LOWER(:transportadora)")
    long countByTransportadora(@Param("transportadora") String transportadora);

    /**
     * Busca todas as transportadoras distintas
     */
    @Query("SELECT DISTINCT d.transportadora FROM DadosRastreamento d ORDER BY d.transportadora")
    List<String> findDistinctTransportadoras();

    /**
     * Busca rastreamentos com localização não informada
     */
    @Query("SELECT d FROM DadosRastreamento d WHERE d.localizacaoAtual IS NULL OR d.localizacaoAtual = '' ORDER BY d.ultimaAtualizacao DESC")
    List<DadosRastreamento> findSemLocalizacao();

    /**
     * Busca rastreamentos por período de atualização
     */
    @Query("SELECT d FROM DadosRastreamento d WHERE d.ultimaAtualizacao BETWEEN :inicio AND :fim ORDER BY d.ultimaAtualizacao DESC")
    List<DadosRastreamento> findByUltimaAtualizacaoBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Atualiza localização em lote por transportadora
     */
    @Query("UPDATE DadosRastreamento d SET d.localizacaoAtual = :novaLocalizacao, d.ultimaAtualizacao = :agora WHERE d.transportadora = :transportadora")
    int atualizarLocalizacaoPorTransportadora(@Param("transportadora") String transportadora,
                                              @Param("novaLocalizacao") String novaLocalizacao,
                                              @Param("agora") LocalDateTime agora);

    /**
     * Remove dados de rastreamento antigos (mais de 90 dias)
     */
    @Query("DELETE FROM DadosRastreamento d WHERE d.ultimaAtualizacao < :dataLimite")
    int removerDadosAntigos(@Param("dataLimite") LocalDateTime dataLimite);
}