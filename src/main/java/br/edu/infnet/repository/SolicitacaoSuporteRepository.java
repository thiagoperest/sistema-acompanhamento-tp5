package br.edu.infnet.repository;

import br.edu.infnet.model.entity.SolicitacaoSuporte;
import br.edu.infnet.model.enums.StatusSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciar operações de SolicitacaoSuporte
 */
@Repository
public interface SolicitacaoSuporteRepository extends JpaRepository<SolicitacaoSuporte, Long> {

    Optional<SolicitacaoSuporte> findByProtocolo(String protocolo);

    List<SolicitacaoSuporte> findByClienteId(Long clienteId);

    List<SolicitacaoSuporte> findByAtendenteId(Long atendenteId);

    List<SolicitacaoSuporte> findByStatus(StatusSuporte status);

    List<SolicitacaoSuporte> findByClienteIdAndStatus(Long clienteId, StatusSuporte status);

    @Query("SELECT s FROM SolicitacaoSuporte s WHERE s.cliente.id = :clienteId ORDER BY s.dataAbertura DESC")
    List<SolicitacaoSuporte> findByClienteIdOrderByDataAberturaDesc(@Param("clienteId") Long clienteId);

    @Query("SELECT s FROM SolicitacaoSuporte s WHERE s.status = :status ORDER BY s.dataAbertura ASC")
    List<SolicitacaoSuporte> findByStatusOrderByDataAberturaAsc(@Param("status") StatusSuporte status);

    long countByStatus(StatusSuporte status);

    long countByClienteId(Long clienteId);
}