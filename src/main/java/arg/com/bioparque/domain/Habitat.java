package arg.com.bioparque.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "habitat")
public class Habitat implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitat")
    private Long idHabitat;
    private String nombre;
    private String clima;
    private String vegetacion;
    private String continente;
    
    @OneToMany(mappedBy = "habitat")
    private List<EspecieHabitat> especieHabitats;
    
}
