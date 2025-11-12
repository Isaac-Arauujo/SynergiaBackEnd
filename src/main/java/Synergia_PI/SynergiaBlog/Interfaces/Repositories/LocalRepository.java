package Synergia_PI.SynergiaBlog.Interfaces.Repositories;

import Synergia_PI.SynergiaBlog.Entidades.Local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
    List<Local> findByOrderByDataInicioAsc();
    
    @Query("SELECT l FROM Local l WHERE l.dataInicio <= :data AND l.dataFinal >= :data")
    List<Local> findLocaisDisponiveisNaData(@Param("data") LocalDate data);
    
    @Query("SELECT l FROM Local l WHERE :data BETWEEN l.dataInicio AND l.dataFinal")
    List<Local> findLocaisAtivosNaData(@Param("data") LocalDate data);
    
    @Query("SELECT COUNT(i) > 0 FROM Inscricao i WHERE i.local.id = :localId AND i.dataDesejada = :data")
    boolean existsInscricaoNaData(@Param("localId") Long localId, @Param("data") LocalDate data);
}