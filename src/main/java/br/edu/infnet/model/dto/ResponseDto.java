package br.edu.infnet.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * DTO padronizado para respostas da API
 */
public class ResponseDto<T> {

    private boolean sucesso;
    private String mensagem;
    private T dados;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDto(boolean sucesso, String mensagem, T dados) {
        this();
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public ResponseDto(boolean sucesso, String mensagem) {
        this(sucesso, mensagem, null);
    }


    /**
     * Cria uma resposta de sucesso com dados
     */
    public static <T> ResponseDto<T> sucesso(String mensagem, T dados) {
        return new ResponseDto<>(true, mensagem, dados);
    }

    /**
     * Cria uma resposta de sucesso sem dados
     */
    public static <T> ResponseDto<T> sucesso(String mensagem) {
        return new ResponseDto<>(true, mensagem, null);
    }

    /**
     * Cria uma resposta de erro
     */
    public static <T> ResponseDto<T> erro(String mensagem) {
        return new ResponseDto<>(false, mensagem, null);
    }

    /**
     * Cria uma resposta de erro com dados (ex: detalhes do erro)
     */
    public static <T> ResponseDto<T> erro(String mensagem, T dados) {
        return new ResponseDto<>(false, mensagem, dados);
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public T getDados() {
        return dados;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "sucesso=" + sucesso +
                ", mensagem='" + mensagem + '\'' +
                ", dados=" + dados +
                ", timestamp=" + timestamp +
                '}';
    }
}