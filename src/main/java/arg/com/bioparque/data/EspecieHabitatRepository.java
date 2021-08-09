package arg.com.bioparque.data;

import arg.com.bioparque.domain.Especie;
import arg.com.bioparque.domain.EspecieHabitat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieHabitatRepository extends JpaRepository<EspecieHabitat, Long>{
    List<EspecieHabitat> findAllByEspecie(Especie especie);
}
