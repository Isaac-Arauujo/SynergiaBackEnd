package Synergia_PI.SynergiaBlog.Interfaces.Repositories;


import Synergia_PI.SynergiaBlog.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Synergia_PI.SynergiaBlog.Entidades.Usuario> findByEmail(String email);
    Optional<Synergia_PI.SynergiaBlog.Entidades.Usuario> findByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha")
    Optional<Usuario> findByEmailAndSenha(@Param("email") String email, @Param("senha") String senha);
    
    @Query("SELECT u FROM Usuario u JOIN u.inscricoes i WHERE i.local.id = :localId")
    List<Usuario> findByLocalInscricao(@Param("localId") Long localId);
}