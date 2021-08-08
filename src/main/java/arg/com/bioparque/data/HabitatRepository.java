package arg.com.bioparque.data;

import arg.com.bioparque.domain.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitatRepository extends JpaRepository<Habitat, Long>{
    
}
