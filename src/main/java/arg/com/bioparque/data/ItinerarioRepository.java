package arg.com.bioparque.data;

import arg.com.bioparque.domain.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItinerarioRepository extends JpaRepository<Itinerario, Long>{
}
