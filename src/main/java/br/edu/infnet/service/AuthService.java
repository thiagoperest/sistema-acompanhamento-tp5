package br.edu.infnet.service;

import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.LoginDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.repository.AtendenteSuporteRepository;
import br.edu.infnet.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service para autenticação e autorização
 */
@Service
@Transactional(readOnly = true)
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AtendenteSuporteRepository atendenteSuporteRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Realiza login do cliente
     */
    public ClienteDto loginCliente(LoginDto loginDto) {
        validarCredenciais(loginDto);

        Optional<Cliente> clienteOpt = clienteRepository.findByEmailAndSenha(
                loginDto.getEmail(),
                loginDto.getSenha()
        );

        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Credenciais inválidas. Email ou senha incorretos.");
        }

        Cliente cliente = clienteOpt.get();

        if (!cliente.getAtivo()) {
            throw new RuntimeException("Conta de cliente desativada. Entre em contato com o suporte.");
        }

        return converterClienteParaDto(cliente);
    }

    /**
     * Realiza login do atendente de suporte
     */
    public AtendenteSuporte loginAtendente(LoginDto loginDto) {
        validarCredenciais(loginDto);

        Optional<AtendenteSuporte> atendenteOpt = atendenteSuporteRepository.findByEmailAndSenha(
                loginDto.getEmail(),
                loginDto.getSenha()
        );

        if (atendenteOpt.isEmpty()) {
            throw new RuntimeException("Credenciais inválidas. Email ou senha incorretos.");
        }

        AtendenteSuporte atendente = atendenteOpt.get();

        if (!atendente.getAtivo()) {
            throw new RuntimeException("Conta de atendente desativada. Entre em contato com a administração.");
        }

        return atendente;
    }

    /**
     * Verifica se email é de cliente ou atendente
     */
    public String identificarTipoUsuario(String email) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        if (cliente.isPresent()) {
            return "CLIENTE";
        }

        Optional<AtendenteSuporte> atendente = atendenteSuporteRepository.findByEmail(email);
        if (atendente.isPresent()) {
            return "ATENDENTE";
        }

        return "NAO_ENCONTRADO";
    }

    /**
     * Valida se as credenciais têm formato válido
     */
    public void validarCredenciais(LoginDto loginDto) {
        if (loginDto == null) {
            throw new RuntimeException("Dados de login são obrigatórios");
        }

        if (loginDto.getEmail() == null || loginDto.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email é obrigatório");
        }

        if (loginDto.getSenha() == null || loginDto.getSenha().trim().isEmpty()) {
            throw new RuntimeException("Senha é obrigatória");
        }

        if (!loginDto.getEmail().contains("@") || !loginDto.getEmail().contains(".")) {
            throw new RuntimeException("Email deve ter formato válido");
        }

        if (loginDto.getSenha().length() < 8) {
            throw new RuntimeException("Senha deve ter pelo menos 8 caracteres");
        }
    }

    /**
     * Verifica se email já está cadastrado no sistema (cliente ou atendente)
     */
    public boolean emailJaCadastrado(String email) {
        return clienteRepository.existsByEmail(email) ||
                atendenteSuporteRepository.existsByEmail(email);
    }

    /**
     * Busca cliente por email
     */
    public Optional<ClienteDto> buscarClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(this::converterClienteParaDto);
    }

    /**
     * Busca atendente por email (usado para recuperação de dados)
     */
    public Optional<AtendenteSuporte> buscarAtendentePorEmail(String email) {
        return atendenteSuporteRepository.findByEmail(email);
    }

    /**
     * Atualiza senha do cliente
     */
    @Transactional
    public void atualizarSenhaCliente(String email, String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new RuntimeException("Nova senha deve ter pelo menos 8 caracteres");
        }

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setSenha(novaSenha);
        clienteRepository.save(cliente);
    }

    /**
     * Atualiza senha do atendente
     */
    @Transactional
    public void atualizarSenhaAtendente(String email, String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new RuntimeException("Nova senha deve ter pelo menos 8 caracteres");
        }

        AtendenteSuporte atendente = atendenteSuporteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Atendente não encontrado"));

        atendente.setSenha(novaSenha);
        atendenteSuporteRepository.save(atendente);
    }

    /**
     * Verifica se o login é válido
     */
    public boolean validarLogin(LoginDto loginDto) {
        try {
            validarCredenciais(loginDto);

            // Verificar se é cliente
            Optional<Cliente> cliente = clienteRepository.findByEmailAndSenha(
                    loginDto.getEmail(), loginDto.getSenha()
            );

            if (cliente.isPresent() && cliente.get().getAtivo()) {
                return true;
            }

            // Verificar se é atendente
            Optional<AtendenteSuporte> atendente = atendenteSuporteRepository.findByEmailAndSenha(
                    loginDto.getEmail(), loginDto.getSenha()
            );

            return atendente.isPresent() && atendente.get().getAtivo();

        } catch (Exception e) {
            return false;
        }
    }

    private ClienteDto converterClienteParaDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setCpf(cliente.getCpf());
        dto.setEndereco(cliente.getEndereco());
        dto.setCidade(cliente.getCidade());
        dto.setCep(cliente.getCep());
        dto.setAtivo(cliente.getAtivo());
        dto.setDataCadastro(cliente.getDataCadastro().format(FORMATTER));
        return dto;
    }
}