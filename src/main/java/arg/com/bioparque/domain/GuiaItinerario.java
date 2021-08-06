package arg.com.bioparque.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "guia_itinerario")
public class GuiaItinerario implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_guia_itinerario")
    private Long idGuiaItinerario;
    
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne
    private Persona persona;
    
    @JoinColumn(name = "id_itinerario", referencedColumnName = "id_itinerario")
    @ManyToOne
    private Itinerario itinerario;
}
