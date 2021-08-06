package arg.com.bioparque.data;

import arg.com.bioparque.domain.Itinerario;
import arg.com.bioparque.domain.ItinerarioZona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItinerarioZonaRepository extends JpaRepository<ItinerarioZona, Long>{
    List<ItinerarioZona> findAllByItinerario(Itinerario itinerario);
}
