package br.edu.infnet.repository;

import br.edu.infnet.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca cliente por email
     * Usado para login e validação de email único
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Busca cliente por CPF
     * Usado para validação de CPF único
     */
    Optional<Cliente> findByCpf(String cpf);

    /**
     * Busca cliente por email e senha
     * Usado para autenticação
     */
    Optional<Cliente> findByEmailAndSenha(String email, String senha);

    /**
     * Verifica se existe cliente com o email (excluindo o próprio ID)
     * Usado para validação de email único em atualizações
     */
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.email = :email AND c.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    /**
     * Verifica se existe cliente com o CPF (excluindo o próprio ID)
     * Usado para validação de CPF único em atualizações
     */
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.cpf = :cpf AND c.id != :id")
    boolean existsByCpfAndIdNot(@Param("cpf") String cpf, @Param("id") Long id);

    /**
     * Busca clientes ativos
     */
    List<Cliente> findByAtivoTrue();

    /**
     * Busca clientes por cidade
     */
    List<Cliente> findByCidadeIgnoreCase(String cidade);

    /**
     * Busca clientes por nome (busca parcial, case insensitive)
     */
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Verifica se existe cliente com email
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe cliente com CPF
     */
    boolean existsByCpf(String cpf);

    /**
     * Conta total de clientes ativos
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.ativo = true")
    long countClientesAtivos();
}