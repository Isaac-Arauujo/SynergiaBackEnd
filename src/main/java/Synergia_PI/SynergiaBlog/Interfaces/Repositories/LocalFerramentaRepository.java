package Synergia_PI.SynergiaBlog.Interfaces.Repositories;

import Synergia_PI.SynergiaBlog.Entidades.LocalFerramenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocalFerramentaRepository extends JpaRepository<LocalFerramenta, Long> {
    List<LocalFerramenta> findByLocalId(Long localId);
    List<LocalFerramenta> findByFerramentaId(Long ferramentaId);
    
    @Query("SELECT lf FROM LocalFerramenta lf WHERE lf.local.id = :localId AND lf.ferramenta.id = :ferramentaId")
    Optional<LocalFerramenta> findByLocalAndFerramenta(@Param("localId") Long localId, @Param("ferramentaId") Long ferramentaId);
    
    @Query("SELECT SUM(lf.quantidade) FROM LocalFerramenta lf WHERE lf.ferramenta.id = :ferramentaId")
    Integer findQuantidadeTotalAlocada(@Param("ferramentaId") Long ferramentaId);
}