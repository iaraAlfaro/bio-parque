package arg.com.bioparque.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "zona")
public class Zona implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id_zona")
    private Long idZona;
    
    private String nombre;
    
    private String extension;
    
    @OneToMany(mappedBy = "zona")
    private List<ItinerarioZona> ItinerarioZonas;
//    @ManyToOne
//    @JoinColumn(name="id_itinerario", referencedColumnName="id_itinerario")
//    private Itinerario itinerario;
}
