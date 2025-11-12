package Synergia_PI.SynergiaBlog.Interfaces.Repositories;

import Synergia_PI.SynergiaBlog.Entidades.Inscricao;
import Synergia_PI.SynergiaBlog.Entidades.Inscricao.StatusInscricao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByStatusOrderByCreatedAtDesc(StatusInscricao status);
    List<Inscricao> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
    List<Inscricao> findByLocalIdOrderByCreatedAtDesc(Long localId);
    
    @Query("SELECT i FROM Inscricao i WHERE i.usuario.id = :usuarioId AND i.local.id = :localId")
    Optional<Inscricao> findByUsuarioAndLocal(@Param("usuarioId") Long usuarioId, @Param("localId") Long localId);
    
    @Query("SELECT i FROM Inscricao i WHERE i.local.id = :localId AND i.dataDesejada = :dataDesejada")
    List<Inscricao> findByLocalAndData(@Param("localId") Long localId, @Param("dataDesejada") LocalDate dataDesejada);
    
    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.local.id = :localId AND i.dataDesejada = :data AND i.status = 'CONFIRMADA'")
    Integer countInscricoesConfirmadasPorLocalEData(@Param("localId") Long localId, @Param("data") LocalDate data);
}