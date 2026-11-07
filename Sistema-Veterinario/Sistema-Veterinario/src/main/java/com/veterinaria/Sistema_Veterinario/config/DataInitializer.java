package com.veterinaria.Sistema_Veterinario.config;

import com.veterinaria.Sistema_Veterinario.domain.CategoriaProduto;
import com.veterinaria.Sistema_Veterinario.domain.Consulta;
import com.veterinaria.Sistema_Veterinario.domain.Pet;
import com.veterinaria.Sistema_Veterinario.domain.Produto;
import com.veterinaria.Sistema_Veterinario.domain.Role;
import com.veterinaria.Sistema_Veterinario.domain.SexoPet;
import com.veterinaria.Sistema_Veterinario.domain.StatusConsulta;
import com.veterinaria.Sistema_Veterinario.domain.TipoConsulta;
import com.veterinaria.Sistema_Veterinario.domain.Tutor;
import com.veterinaria.Sistema_Veterinario.domain.Usuario;
import com.veterinaria.Sistema_Veterinario.domain.Veterinario;
import com.veterinaria.Sistema_Veterinario.repository.ConsultaRepository;
import com.veterinaria.Sistema_Veterinario.repository.PetRepository;
import com.veterinaria.Sistema_Veterinario.repository.ProdutoRepository;
import com.veterinaria.Sistema_Veterinario.repository.TutorRepository;
import com.veterinaria.Sistema_Veterinario.repository.UsuarioRepository;
import com.veterinaria.Sistema_Veterinario.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ConsultaRepository consultaRepository;
    private final ProdutoRepository produtoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            criarUsuario("Administrador", "admin@vet.com", "admin123", Role.ADMIN);
            criarUsuario("Dra. Ana Souza", "vet@vet.com", "vet123", Role.VETERINARIO);
            criarUsuario("Recepção", "recepcao@vet.com", "recepcao123", Role.RECEPCAO);
            log.info("Usuários padrão criados. admin@vet.com / admin123");
        }

        Veterinario veterinario = veterinarioRepository.findByAtivoTrueOrderByNomeAsc().stream()
                .findFirst()
                .orElseGet(() -> {
                    Veterinario novo = new Veterinario();
                    novo.setNome("Dra. Ana Souza");
                    novo.setCrmv("CRMV-SP-12345");
                    novo.setEspecialidade("Clínica geral");
                    novo.setEmail("ana.vet@vet.com");
                    novo.setTelefone("(11) 99999-1111");
                    novo.setAtivo(true);
                    return veterinarioRepository.save(novo);
                });

        Tutor tutor = tutorRepository.findByAtivoTrueOrderByNomeAsc().stream()
                .findFirst()
                .orElseGet(() -> {
                    Tutor novo = new Tutor();
                    novo.setNome("João da Silva");
                    novo.setCpf("12345678901");
                    novo.setEmail("joao@email.com");
                    novo.setTelefone("(11) 98888-2222");
                    novo.setEndereco("Rua das Flores, 100");
                    novo.setCidade("São Paulo");
                    novo.setUf("SP");
                    novo.setAtivo(true);
                    return tutorRepository.save(novo);
                });

        Pet pet = petRepository.findByAtivoTrueOrderByNomeAsc().stream()
                .findFirst()
                .orElseGet(() -> {
                    Pet novo = new Pet();
                    novo.setNome("Rex");
                    novo.setEspecie("Cachorro");
                    novo.setRaca("Labrador");
                    novo.setSexo(SexoPet.MACHO);
                    novo.setDataNascimento(LocalDate.now().minusYears(3));
                    novo.setPeso(new BigDecimal("28.500"));
                    novo.setTutor(tutor);
                    novo.setObservacoes("Paciente dócil");
                    novo.setAtivo(true);
                    return petRepository.save(novo);
                });

        if (consultaRepository.count() == 0) {
            Consulta consulta = new Consulta();
            consulta.setPet(pet);
            consulta.setVeterinario(veterinario);
            consulta.setDataHora(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0));
            consulta.setTipo(TipoConsulta.ROTINA);
            consulta.setStatus(StatusConsulta.AGENDADA);
            consulta.setMotivo("Consulta de rotina anual");
            consulta.setValor(new BigDecimal("150.00"));
            consultaRepository.save(consulta);
        }

        if (produtoRepository.count() == 0) {
            criarProduto("Ração Premium Adulto", "Ração para cães adultos", CategoriaProduto.RACAO,
                    new BigDecimal("120.00"), 30, 10);
            criarProduto("Vermífugo Pet", "Antiparasitário de amplo espectro", CategoriaProduto.MEDICAMENTO,
                    new BigDecimal("45.90"), 8, 10);
            criarProduto("Shampoo Hipoalergênico", "Shampoo para pele sensível", CategoriaProduto.HIGIENE,
                    new BigDecimal("38.50"), 15, 5);
        }
    }

    private void criarUsuario(String nome, String email, String senha, Role role) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setRole(role);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    private void criarProduto(String nome, String descricao, CategoriaProduto categoria,
                              BigDecimal preco, int quantidade, int estoqueMinimo) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setCategoria(categoria);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidade);
        produto.setEstoqueMinimo(estoqueMinimo);
        produto.setAtivo(true);
        produtoRepository.save(produto);
    }
}
