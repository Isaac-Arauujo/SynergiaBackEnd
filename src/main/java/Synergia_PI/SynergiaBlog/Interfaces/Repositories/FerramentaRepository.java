package Synergia_PI.SynergiaBlog.Interfaces.Repositories;

import Synergia_PI.SynergiaBlog.Entidades.Ferramenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FerramentaRepository extends JpaRepository<Ferramenta, Long> {
    Optional<Ferramenta> findByNomeIgnoreCase(String nome);
    List<Ferramenta> findByOrderByNomeAsc();
    
    @Query("SELECT f FROM Ferramenta f WHERE f.quantidade > 0 ORDER BY f.nome")
    List<Ferramenta> findFerramentasDisponiveis();
    
    @Query("SELECT f FROM Ferramenta f WHERE f.quantidade >= :quantidadeMinima")
    List<Ferramenta> findByQuantidadeGreaterThanEqual(@Param("quantidadeMinima") Integer quantidadeMinima);
}