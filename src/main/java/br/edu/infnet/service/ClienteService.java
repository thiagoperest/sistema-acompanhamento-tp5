package br.edu.infnet.service;

import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para regras de negócio relacionadas aos Clientes
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Registra um novo cliente
     */
    public ClienteDto registrarCliente(ClienteDto clienteDto) {
        // Validações de negócio
        validarEmailUnico(clienteDto.getEmail(), null);
        validarCpfUnico(clienteDto.getCpf(), null);
        validarDadosObrigatorios(clienteDto);
        validarFormatoTelefone(clienteDto.getTelefone());

        Cliente cliente = converterDtoParaEntity(clienteDto);

        // Aplicar senha padrão se não informada
        if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            cliente.setSenha("123456789");
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return converterEntityParaDto(clienteSalvo);
    }

    /**
     * Busca cliente por ID
     */
    public Optional<ClienteDto> buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca cliente por email
     */
    public Optional<ClienteDto> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(this::converterEntityParaDto);
    }

    /**
     * Lista todos os clientes ativos
     */
    public List<ClienteDto> listarClientesAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza dados do cliente
     */
    public ClienteDto atualizarCliente(Long id, ClienteDto clienteDto) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }

        Cliente cliente = clienteExistente.get();

        if (!cliente.getEmail().equals(clienteDto.getEmail())) {
            validarEmailUnico(clienteDto.getEmail(), id);
        }

        if (!cliente.getCpf().equals(clienteDto.getCpf())) {
            validarCpfUnico(clienteDto.getCpf(), id);
        }

        validarDadosObrigatorios(clienteDto);
        validarFormatoTelefone(clienteDto.getTelefone());

        atualizarCamposCliente(cliente, clienteDto);

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return converterEntityParaDto(clienteAtualizado);
    }

    /**
     * Desativa cliente
     */
    public void desativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));

        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    /**
     * Busca clientes por nome
     */
    public List<ClienteDto> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca clientes por cidade
     */
    public List<ClienteDto> buscarPorCidade(String cidade) {
        return clienteRepository.findByCidadeIgnoreCase(cidade)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    private void validarEmailUnico(String email, Long idExcluir) {
        boolean emailJaExiste = (idExcluir == null) ?
                clienteRepository.existsByEmail(email) :
                clienteRepository.existsByEmailAndIdNot(email, idExcluir);

        if (emailJaExiste) {
            throw new RuntimeException("Email já cadastrado no sistema: " + email);
        }
    }

    private void validarCpfUnico(String cpf, Long idExcluir) {
        boolean cpfJaExiste = (idExcluir == null) ?
                clienteRepository.existsByCpf(cpf) :
                clienteRepository.existsByCpfAndIdNot(cpf, idExcluir);

        if (cpfJaExiste) {
            throw new RuntimeException("CPF já cadastrado no sistema: " + cpf);
        }
    }

    private void validarDadosObrigatorios(ClienteDto dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email é obrigatório");
        }
        if (!dto.getEmail().contains("@") || !dto.getEmail().contains(".")) {
            throw new RuntimeException("Email deve ter formato válido");
        }
        if (dto.getCpf() == null || !dto.getCpf().matches("\\d{11}")) {
            throw new RuntimeException("CPF deve conter exatamente 11 dígitos");
        }
        if (dto.getCep() == null || !dto.getCep().matches("\\d{8}")) {
            throw new RuntimeException("CEP deve conter exatamente 8 dígitos");
        }
    }

    private void validarFormatoTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty()) {
            String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
            if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
                throw new RuntimeException("Telefone deve ter 10 ou 11 dígitos");
            }
        }
    }

    private Cliente converterDtoParaEntity(ClienteDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());
        cliente.setEndereco(dto.getEndereco());
        cliente.setCidade(dto.getCidade());
        cliente.setCep(dto.getCep());
        cliente.setSenha(dto.getSenha());
        return cliente;
    }

    private ClienteDto converterEntityParaDto(Cliente cliente) {
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

    private void atualizarCamposCliente(Cliente cliente, ClienteDto dto) {
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());
        cliente.setEndereco(dto.getEndereco());
        cliente.setCidade(dto.getCidade());
        cliente.setCep(dto.getCep());
    }
}