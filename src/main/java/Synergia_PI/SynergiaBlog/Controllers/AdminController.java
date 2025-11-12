package Synergia_PI.SynergiaBlog.Controllers;

//Synergia_PI.SynergiaBlog.Interfaces.Repositories.
//Synergia_PI.SynergiaBlog.DTOs.
//Synergia_PI.SynergiaBlog.Entidades

import Synergia_PI.SynergiaBlog.DTOs.InscricaoDTO;
import Synergia_PI.SynergiaBlog.DTOs.LocalDTO;
import Synergia_PI.SynergiaBlog.DTOs.FerramentaDTO;
import Synergia_PI.SynergiaBlog.Entidades.Inscricao.StatusInscricao;
import Synergia_PI.SynergiaBlog.Services.InscricaoService;
import Synergia_PI.SynergiaBlog.Services.LocalService;
import Synergia_PI.SynergiaBlog.Services.FerramentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@Tag(name = "Admin", description = "Operações administrativas")
public class AdminController {

    @Autowired
    private LocalService localService;

    @Autowired
    private FerramentaService ferramentaService;

    @Autowired
    private InscricaoService inscricaoService;

    // === LOCAIS ===
    @GetMapping("/locais")
    @Operation(summary = "Listar todos os locais (admin)")
    public ResponseEntity<List<LocalDTO>> listarLocais() {
        List<LocalDTO> locais = localService.findAll();
        return ResponseEntity.ok(locais);
    }

    // === FERRAMENTAS ===
    @GetMapping("/ferramentas")
    @Operation(summary = "Listar todas as ferramentas (admin)")
    public ResponseEntity<List<FerramentaDTO>> listarFerramentas() {
        List<FerramentaDTO> ferramentas = ferramentaService.findAll();
        return ResponseEntity.ok(ferramentas);
    }

    // === INSCRIÇÕES ===
    @GetMapping("/inscricoes")
    @Operation(summary = "Listar todas as inscrições (admin)")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoes() {
        List<InscricaoDTO> inscricoes = inscricaoService.findAll();
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/inscricoes/pendentes")
    @Operation(summary = "Listar inscrições pendentes (admin)")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesPendentes() {
        List<InscricaoDTO> inscricoes = inscricaoService.findByStatus(StatusInscricao.PENDENTE);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/inscricoes/confirmadas")
    @Operation(summary = "Listar inscrições confirmadas (admin)")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesConfirmadas() {
        List<InscricaoDTO> inscricoes = inscricaoService.findByStatus(StatusInscricao.CONFIRMADA);
        return ResponseEntity.ok(inscricoes);
    }

    @PutMapping("/inscricoes/{id}/confirmar")
    @Operation(summary = "Confirmar inscrição (admin)")
    public ResponseEntity<Void> confirmarInscricao(@PathVariable Long id) {
        if (inscricaoService.confirmarInscricao(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/inscricoes/{id}/recusar")
    @Operation(summary = "Recusar inscrição (admin)")
    public ResponseEntity<Void> recusarInscricao(@PathVariable Long id) {
        if (inscricaoService.recusarInscricao(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dashboard/estatisticas")
    @Operation(summary = "Obter estatísticas do dashboard (admin)")
    public ResponseEntity<DashboardEstatisticas> getEstatisticas() {
        List<InscricaoDTO> todasInscricoes = inscricaoService.findAll();
        List<InscricaoDTO> inscricoesPendentes = inscricaoService.findByStatus(StatusInscricao.PENDENTE);
        List<InscricaoDTO> inscricoesConfirmadas = inscricaoService.findByStatus(StatusInscricao.CONFIRMADA);
        List<LocalDTO> todosLocais = localService.findAll();
        List<FerramentaDTO> todasFerramentas = ferramentaService.findAll();

        DashboardEstatisticas estatisticas = new DashboardEstatisticas();
        estatisticas.setTotalInscricoes(todasInscricoes.size());
        estatisticas.setInscricoesPendentes(inscricoesPendentes.size());
        estatisticas.setInscricoesConfirmadas(inscricoesConfirmadas.size());
        estatisticas.setTotalLocais(todosLocais.size());
        estatisticas.setTotalFerramentas(todasFerramentas.size());

        return ResponseEntity.ok(estatisticas);
    }

    // Classe interna para estatísticas do dashboard
    public static class DashboardEstatisticas {
        private int totalInscricoes;
        private int inscricoesPendentes;
        private int inscricoesConfirmadas;
        private int totalLocais;
        private int totalFerramentas;

        // Getters e Setters
        public int getTotalInscricoes() { return totalInscricoes; }
        public void setTotalInscricoes(int totalInscricoes) { this.totalInscricoes = totalInscricoes; }
        
        public int getInscricoesPendentes() { return inscricoesPendentes; }
        public void setInscricoesPendentes(int inscricoesPendentes) { this.inscricoesPendentes = inscricoesPendentes; }
        
        public int getInscricoesConfirmadas() { return inscricoesConfirmadas; }
        public void setInscricoesConfirmadas(int inscricoesConfirmadas) { this.inscricoesConfirmadas = inscricoesConfirmadas; }
        
        public int getTotalLocais() { return totalLocais; }
        public void setTotalLocais(int totalLocais) { this.totalLocais = totalLocais; }
        
        public int getTotalFerramentas() { return totalFerramentas; }
        public void setTotalFerramentas(int totalFerramentas) { this.totalFerramentas = totalFerramentas; }
    }
}