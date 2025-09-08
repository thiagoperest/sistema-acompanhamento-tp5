package br.edu.infnet.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidade AtendenteSuporte que estende Usuario
 */
@Entity
@Table(name = "atendentes_suporte")
@DiscriminatorValue("ATENDENTE_SUPORTE")
public class AtendenteSuporte extends Usuario {

    @NotBlank(message = "Matrícula é obrigatória")
    @Size(max = 20, message = "Matrícula deve ter no máximo 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @NotBlank(message = "Departamento é obrigatório")
    @Size(max = 50, message = "Departamento deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String departamento;

    public AtendenteSuporte() {
        super();
    }

    public AtendenteSuporte(String nome, String email, String senha, String matricula, String departamento) {
        super(nome, email, senha);
        this.matricula = matricula;
        this.departamento = departamento;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Verifica se o atendente está em um departamento específico
     */
    public boolean isDepartamento(String departamento) {
        return this.departamento != null && this.departamento.equalsIgnoreCase(departamento);
    }

    @Override
    public String toString() {
        return "AtendenteSuporte{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", matricula='" + matricula + '\'' +
                ", departamento='" + departamento + '\'' +
                ", ativo=" + getAtivo() +
                '}';
    }
}