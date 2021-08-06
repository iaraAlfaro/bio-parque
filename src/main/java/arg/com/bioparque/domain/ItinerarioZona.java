package arg.com.bioparque.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "itinerario_zona")
public class ItinerarioZona implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_itinerario_zona")
    private Long idItinerarioZona;
    
    @JoinColumn(name = "id_zona", referencedColumnName = "id_zona")
    @ManyToOne
    private Zona zona;
    
    @JoinColumn(name = "id_itinerario", referencedColumnName = "id_itinerario")
    @ManyToOne
    private Itinerario itinerario;
}
