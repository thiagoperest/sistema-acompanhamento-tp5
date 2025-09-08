package br.edu.infnet.repository;

import br.edu.infnet.model.entity.AtendenteSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade AtendenteSuporte
 */
@Repository
public interface AtendenteSuporteRepository extends JpaRepository<AtendenteSuporte, Long> {

    /**
     * Busca atendente por email
     * Usado para login e validação de email único
     */
    Optional<AtendenteSuporte> findByEmail(String email);

    /**
     * Busca atendente por matrícula
     * Usado para validação de matrícula única
     */
    Optional<AtendenteSuporte> findByMatricula(String matricula);

    /**
     * Busca atendente por email e senha
     * Usado para autenticação
     */
    Optional<AtendenteSuporte> findByEmailAndSenha(String email, String senha);

    /**
     * Busca atendentes por departamento
     */
    List<AtendenteSuporte> findByDepartamentoIgnoreCase(String departamento);

    /**
     * Busca atendentes ativos
     */
    List<AtendenteSuporte> findByAtivoTrue();

    /**
     * Busca atendentes ativos por departamento
     */
    List<AtendenteSuporte> findByDepartamentoIgnoreCaseAndAtivoTrue(String departamento);

    /**
     * Verifica se existe atendente com email (excluindo o próprio ID)
     * Usado para validação de email único em atualizações
     */
    @Query("SELECT COUNT(a) > 0 FROM AtendenteSuporte a WHERE a.email = :email AND a.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    /**
     * Verifica se existe atendente com matrícula (excluindo o próprio ID)
     * Usado para validação de matrícula única em atualizações
     */
    @Query("SELECT COUNT(a) > 0 FROM AtendenteSuporte a WHERE a.matricula = :matricula AND a.id != :id")
    boolean existsByMatriculaAndIdNot(@Param("matricula") String matricula, @Param("id") Long id);

    /**
     * Busca atendentes por nome (busca parcial, case insensitive)
     */
    @Query("SELECT a FROM AtendenteSuporte a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<AtendenteSuporte> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Verifica se existe atendente com email
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe atendente com matrícula
     */
    boolean existsByMatricula(String matricula);

    /**
     * Conta total de atendentes ativos
     */
    @Query("SELECT COUNT(a) FROM AtendenteSuporte a WHERE a.ativo = true")
    long countAtendentesAtivos();

    /**
     * Conta atendentes por departamento
     */
    @Query("SELECT COUNT(a) FROM AtendenteSuporte a WHERE LOWER(a.departamento) = LOWER(:departamento) AND a.ativo = true")
    long countByDepartamento(@Param("departamento") String departamento);
}