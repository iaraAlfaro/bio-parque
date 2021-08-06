package arg.com.bioparque.data;

import arg.com.bioparque.domain.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieRepository extends JpaRepository<Especie, Long>{
}
