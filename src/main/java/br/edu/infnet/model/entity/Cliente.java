package br.edu.infnet.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Entidade Cliente que estende Usuario
 */
@Entity
@Table(name = "clientes")
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String endereco;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 dígitos")
    @Column(nullable = false, length = 8)
    private String cep;

    // Relacionamento 1:1 com PreferenciasNotificacao
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PreferenciasNotificacao preferenciasNotificacao;

    public Cliente() {
        super();
    }

    public Cliente(String nome, String email, String senha, String cpf, String endereco, String cidade, String cep) {
        super(nome, email, senha);
        this.cpf = cpf;
        this.endereco = endereco;
        this.cidade = cidade;
        this.cep = cep;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public PreferenciasNotificacao getPreferenciasNotificacao() {
        return preferenciasNotificacao;
    }

    public void setPreferenciasNotificacao(PreferenciasNotificacao preferenciasNotificacao) {
        this.preferenciasNotificacao = preferenciasNotificacao;
    }

    /**
     * Valida se o CPF tem formato válido (11 dígitos)
     */
    public boolean isCpfValido() {
        return cpf != null && cpf.matches("\\d{11}");
    }

    /**
     * Valida se o CEP tem formato válido (8 dígitos)
     */
    public boolean isCepValido() {
        return cep != null && cep.matches("\\d{8}");
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cpf='" + cpf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", cep='" + cep + '\'' +
                ", ativo=" + getAtivo() +
                '}';
    }
}