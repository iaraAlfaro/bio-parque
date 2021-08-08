package arg.com.bioparque.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "especie_habitat")
public class EspecieHabitat implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especie_habitat")
    private Long idEspecieHabitat;
    
    @JoinColumn(name = "id_especie", referencedColumnName = "id_especie")
    @ManyToOne
    private Especie especie;
    
    @JoinColumn(name = "id_habitat", referencedColumnName = "id_habitat")
    @ManyToOne
    private Habitat habitat;
            
}
