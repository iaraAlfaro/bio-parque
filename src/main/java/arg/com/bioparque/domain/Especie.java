package arg.com.bioparque.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "especie")
public class Especie implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especie")
    private Long idEspecie;
    
    private String nombre;
    
    @Column(name = "nombre_cientifico")
    private String nombreCientifico;
    
    private String descripcion;
    
    @OneToMany(mappedBy = "especie")
    private List<CuidadorEspecie> cuidadorEspecies;
    
    @OneToMany(mappedBy = "especie")
    private List<EspecieHabitat> especieHabitats;
}
