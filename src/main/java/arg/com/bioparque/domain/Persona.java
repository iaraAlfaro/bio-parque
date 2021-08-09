package arg.com.bioparque.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Table(name = "persona")
public class Persona implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;
    
    private String nombre;
    
    private String direccion;
    
    private String telefono;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;
    
    @Column(name = "user_name")
    private String userName;
    
    private String rol;
    
    private String password;
    
    @OneToMany(mappedBy = "persona")
    private List<CuidadorEspecie> cuidadorEspecies;
    
    @OneToMany(mappedBy = "persona")
    private List<GuiaItinerario> guiaItinerario;
}
