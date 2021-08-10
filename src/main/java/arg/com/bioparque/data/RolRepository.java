package arg.com.bioparque.data;

import arg.com.bioparque.domain.Rol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long>{
    //void deleteAllByIdUsuario(List<Long> idUsuarios);
    List<Rol> findAllByIdUsuario(Long idUsuario);
    Rol findByIdUsuario(Long idUsuario);
}
