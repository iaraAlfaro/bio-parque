package arg.com.bioparque.web;

import arg.com.bioparque.data.CuidadorEspecieRepository;
import arg.com.bioparque.data.EspecieHabitatRepository;
import arg.com.bioparque.data.EspecieRepository;
import arg.com.bioparque.data.GuiaItinerarioRepository;
import arg.com.bioparque.data.HabitatRepository;
import arg.com.bioparque.data.ItinerarioRepository;
import arg.com.bioparque.data.ItinerarioZonaRepository;
import arg.com.bioparque.data.PersonaRepository;
import arg.com.bioparque.data.RolRepository;
import arg.com.bioparque.data.UsuarioRepository;
import arg.com.bioparque.data.ZonaRepository;
import arg.com.bioparque.domain.CuidadorEspecie;
import arg.com.bioparque.domain.Especie;
import arg.com.bioparque.domain.EspecieHabitat;
import arg.com.bioparque.domain.GuiaItinerario;
import arg.com.bioparque.domain.Habitat;
import arg.com.bioparque.domain.Itinerario;
import arg.com.bioparque.domain.ItinerarioZona;
import arg.com.bioparque.domain.Persona;
import arg.com.bioparque.domain.Rol;
import arg.com.bioparque.domain.Usuario;
import arg.com.bioparque.domain.Zona;
import arg.com.bioparque.util.EncriptPassword;
import arg.com.bioparque.util.Roles;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CuidadorEspecieRepository cuidadorEspecieRepository;

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private GuiaItinerarioRepository guiaItinerarioRepository;

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private ItinerarioZonaRepository itinerarioZonaRepository;

    @Autowired
    private HabitatRepository habitatRepository;

    @Autowired
    private EspecieHabitatRepository especieHabitatRepository;
    
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user) {

        Persona persona = personaRepository.findByUserName(user.getUsername());
        model.addAttribute("persona", persona);

        List<Persona> personas = personaRepository.findAll();
        model.addAttribute("personas", personas);

        List<CuidadorEspecie> cuidadorEspecies = cuidadorEspecieRepository.findByPersona(persona);
        model.addAttribute("cuidadorEspecies", cuidadorEspecies);

        List<Especie> especies = especieRepository.findAll();
        model.addAttribute("especies", especies);

        List<GuiaItinerario> guiaItinerarios = guiaItinerarioRepository.findByPersona(persona);
        model.addAttribute("guiaItinerarios", guiaItinerarios);

        List<Zona> zonas = zonaRepository.findAll();
        model.addAttribute("zonas", zonas);

        Itinerario itinerario = new Itinerario();
        model.addAttribute("itinerario", itinerario);

        List<Habitat> habitats = habitatRepository.findAll();
        model.addAttribute("habitats", habitats);

        Especie especie = new Especie();
        model.addAttribute("especie", especie);
        
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_CUIDADOR");
        roles.add("ROLE_GUIA");
       
        model.addAttribute("roles", roles);
        
        return "index";
    }

    @GetMapping("/eliminarCuidadorEspecie")
    public String eliminarCuidadorEspecie(CuidadorEspecie cuidadorEspecie) {
        cuidadorEspecie = cuidadorEspecieRepository.findById(cuidadorEspecie.getIdCuidadorEspecie()).orElse(null);
        Long id = cuidadorEspecie.getPersona().getIdPersona();
        cuidadorEspecieRepository.delete(cuidadorEspecie);
        return "redirect:/verPersona?idPersona=" + id;
    }

    @GetMapping("/editarCuidadorEspecie/{idCuidadorEspecie}")
    public String editarCuidadorEspecie(CuidadorEspecie cuidadorEspecie, Model model) {
        cuidadorEspecie = cuidadorEspecieRepository.findById(cuidadorEspecie.getIdCuidadorEspecie()).orElse(null);
        model.addAttribute("cuidadorEspecie", cuidadorEspecie);
        List<Habitat> habitats = habitatRepository.findAll();
        model.addAttribute("habitats", habitats);
        return "modificarCuidadorEspecie";
    }

    @GetMapping("/agregarCuidadorEspecie/{idPersona}")
    public String agregarCuidadorEspecie(
            @Valid Persona persona,
            Model model,
            Errors errores) {
        persona = personaRepository.findById(persona.getIdPersona()).orElse(null);
        CuidadorEspecie cuidadorEspecie = new CuidadorEspecie();
        cuidadorEspecie.setPersona(persona);
        model.addAttribute("cuidadorEspecie", cuidadorEspecie);
        return "agregarCuidadorEspecie";
    }

    @PostMapping("/guardarCuidadorEspecie")
    public String guardarCuidadorEspecie(@Valid CuidadorEspecie cuidadorEspecie, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "adminModificarCuidadorEspecie";
        }
        Persona persona = personaRepository.findById(cuidadorEspecie.getPersona().getIdPersona()).orElse(null);
        cuidadorEspecie.setPersona(persona);
        cuidadorEspecieRepository.save(cuidadorEspecie);
        return "redirect:/verPersona?idPersona=" + persona.getIdPersona();
    }

    @PostMapping("/adminCuidadorEspecieEditado")
    public String adminCuidadorEspecieEditado(
            @Valid CuidadorEspecie cuidadorEspecie,
            Errors errores) {
        if (errores.hasErrors()) {
            return "adminModificarCuidadorEspecie";
        }
        
        List<Habitat> habitats = new ArrayList<>();
        for (Habitat habitat : cuidadorEspecie.getEspecie().getHabitats()) {
            habitats.add(habitat);
        }
        cuidadorEspecie.getEspecie().getHabitats().clear();

        Persona persona = personaRepository.findById(cuidadorEspecie.getPersona().getIdPersona()).orElse(null);
        Especie especie = especieRepository.save(cuidadorEspecie.getEspecie());
        cuidadorEspecie.setPersona(persona);
        
        if (habitats != null || !habitats.isEmpty()) {
            List<EspecieHabitat> especieHabitats = especieHabitatRepository.findAllByEspecie(especie);
            especieHabitatRepository.deleteAll(especieHabitats);
            for (Habitat habitat : habitats) {
                EspecieHabitat especieHabitat = new EspecieHabitat();
                especieHabitat.setEspecie(especie);
                especieHabitat.setHabitat(habitat);
                especieHabitatRepository.save(especieHabitat);
            }
        }

        cuidadorEspecieRepository.save(cuidadorEspecie);
        return "redirect:/verPersona?idPersona=" + persona.getIdPersona();
    }

    @GetMapping("/eliminarPersona")
    public String eliminarPersona(Persona persona) {
        persona = personaRepository.findById(persona.getIdPersona()).orElse(null);
        Usuario usuario =  usuarioRepository.findByUsername(persona.getUserName());
        Rol rol = rolRepository.findByIdUsuario(usuario.getIdUsuario());
        rolRepository.delete(rol);
        
        usuarioRepository.delete(usuario);
        personaRepository.delete(persona);
        return "redirect:/";
    }

    @GetMapping("/editarPersona/{idPersona}")
    public String editarPersona(Persona persona, Model model) {
        persona = personaRepository.findById(persona.getIdPersona()).orElse(null);
        model.addAttribute("persona", persona);
        return "modificarPersona";
    }

    @GetMapping("/verPersona")
    public String verPersona(Persona persona, Model model) {
        persona = personaRepository.findById(persona.getIdPersona()).orElse(null);
        model.addAttribute("persona", persona);
        if (persona.getRol().equals("ROLE_CUIDADOR")) {
            List<CuidadorEspecie> cuidadorEspecies = cuidadorEspecieRepository.findByPersona(persona);
            model.addAttribute("cuidadorEspecies", cuidadorEspecies);
            List<Especie> especies = especieRepository.findAll();
            model.addAttribute("especies", especies);
            CuidadorEspecie cuidadorEspecie = new CuidadorEspecie();
            cuidadorEspecie.setPersona(persona);
            model.addAttribute("cuidadorEspecie", cuidadorEspecie);
            return "adminModificarCuidadorEspecie";
        } else if (persona.getRol().equals("ROLE_GUIA")) {
            List<GuiaItinerario> guiaItinerarios = guiaItinerarioRepository.findByPersona(persona);
            model.addAttribute("guiaItinerarios", guiaItinerarios);
            GuiaItinerario guiaItinerario = new GuiaItinerario();
            Itinerario itinerario = new Itinerario();
            guiaItinerario.setItinerario(itinerario);
            guiaItinerario.setPersona(persona);
            model.addAttribute("guiaItinerario", guiaItinerario);
            List<Zona> zonas = zonaRepository.findAll();
            model.addAttribute("zonas", zonas);
            List<Itinerario> itinerarios = itinerarioRepository.findAll();
            model.addAttribute("itinerarios", itinerarios);
            return "adminModificarGuia";
        }
        return "redirect:/";
    }

    @PostMapping("/guardarPersona")
    public String guardar(@Valid Persona persona, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "modificarPersona";
        }
        personaRepository.save(persona);
        
        EncriptPassword encriptPassword = new EncriptPassword();
        String passwordEncriptada = encriptPassword.encriptar(persona.getPassword());
        
        Usuario usuario = new Usuario();
        usuario.setUsername(persona.getUserName());
        usuario.setPassword(passwordEncriptada);
        usuario = usuarioRepository.save(usuario);
        
        Rol rol = new Rol();
        rol.setIdUsuario(usuario.getIdUsuario());
        rol.setNombre(persona.getRol());
        
        rolRepository.save(rol);
        return "redirect:/";
    }
    
    @PostMapping("/guardarPersonaEditada")
    public String guardarPersonaEditada(@Valid Persona persona, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "modificarPersona";
        }
        Persona personaOriginal = new Persona();
        personaOriginal = personaRepository.findById(persona.getIdPersona()).orElse(null);
        persona.setUserName(personaOriginal.getUserName());
        persona.setRol(personaOriginal.getRol());
        personaRepository.save(persona);
        
        EncriptPassword encriptPassword = new EncriptPassword();
        String passwordEncriptada = encriptPassword.encriptar(persona.getPassword());
        
        Usuario usuario = new Usuario();
        usuario = usuarioRepository.findByUsername(persona.getUserName());
        usuario.setPassword(passwordEncriptada);
        usuario = usuarioRepository.save(usuario);
                
        return "redirect:/";
    }

    @GetMapping("/eliminarGuiaItinerario")
    public String eliminarGuiaItinerario(GuiaItinerario guiaItinerario) {
        guiaItinerario = guiaItinerarioRepository.findById(guiaItinerario.getIdGuiaItinerario()).orElse(null);
        Long id = guiaItinerario.getPersona().getIdPersona();
        guiaItinerarioRepository.delete(guiaItinerario);
        return "redirect:/verPersona?idPersona=" + id;
    }

    @GetMapping("/editarGuiaItinerario/{idGuiaItinerario}")
    public String editarGuiaItinerario(GuiaItinerario guiaItinerario, Model model) {
        guiaItinerario = guiaItinerarioRepository.findById(guiaItinerario.getIdGuiaItinerario()).orElse(null);
        model.addAttribute("guiaItinerario", guiaItinerario);
        List<Zona> zonas = zonaRepository.findAll();
        model.addAttribute("zonas", zonas);
        return "adminModificarGuiaItinerario";
    }

    @PostMapping("/guardarGuiaItinerarioEditado")
    public String guardarGuiaItinerarioEditado(
            @Valid GuiaItinerario guiaItinerario,
            Errors errores) {
        if (errores.hasErrors()) {
            return "adminModificarGuia";
        }
        Persona persona = personaRepository.findById(guiaItinerario.getPersona().getIdPersona()).orElse(null);
        guiaItinerario.setPersona(persona);
        guiaItinerarioRepository.save(guiaItinerario);
        return "redirect:/verPersona?idPersona=" + persona.getIdPersona();
    }

    @PostMapping("/adminGuiaItinerarioEditado")
    public String adminGuiaItinerarioEditado(
            @Valid GuiaItinerario guiaItinerario,
            Errors errores) {
        if (errores.hasErrors()) {
            return "adminModificarGuia";
        }
        List<Zona> zonas = new ArrayList<>();
        for (Zona zona : guiaItinerario.getItinerario().getZonas()) {
            zonas.add(zona);
        }
        guiaItinerario.getItinerario().getZonas().clear();
        Persona persona = personaRepository.findById(guiaItinerario.getPersona().getIdPersona()).orElse(null);
        Itinerario itinerario = itinerarioRepository.save(guiaItinerario.getItinerario());
        guiaItinerario.setPersona(persona);
        
        if (zonas != null || !zonas.isEmpty()) {
            List<ItinerarioZona> ItinerarioZonas = itinerarioZonaRepository.findAllByItinerario(itinerario);
            itinerarioZonaRepository.deleteAll(ItinerarioZonas);
            for (Zona zona : zonas) {
                ItinerarioZona itinerarioZona = new ItinerarioZona();
                itinerarioZona.setItinerario(itinerario);
                itinerarioZona.setZona(zona);
                itinerarioZonaRepository.save(itinerarioZona);
            }
        }
        guiaItinerarioRepository.save(guiaItinerario);
        
        return "redirect:/verPersona?idPersona=" + persona.getIdPersona();
    }

    @PostMapping("/guardarGuiaItinerario")
    public String guardarGuiaItinerario(
            @Valid GuiaItinerario guiaItinerario,
            Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "adminModificarCuidadorEspecie";
        }
        Persona persona = personaRepository.findById(guiaItinerario.getPersona().getIdPersona()).orElse(null);
        guiaItinerario.setPersona(persona);
        guiaItinerarioRepository.save(guiaItinerario);
        return "redirect:/verPersona?idPersona=" + persona.getIdPersona();
    }

    @PostMapping("/guardarEspecie")
    public String guardarEspecie(@Valid Especie especie, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "listadoPersonas";
        }
        List<Habitat> habitats = new ArrayList<>();
        for (Habitat habitat : especie.getHabitats()) {
            habitats.add(habitat);
        }
        especie.getHabitats().clear();
        especieRepository.save(especie);

        for (Habitat habitat : habitats) {
            EspecieHabitat especieHabitat = new EspecieHabitat();
            especieHabitat.setEspecie(especie);
            especieHabitat.setHabitat(habitat);
            especieHabitatRepository.save(especieHabitat);
        }
        return "redirect:/";
    }

    @PostMapping("/guardarItinerario")
    public String guardarItinerario(@Valid Itinerario itinerario, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "listadoPersonas";
        }
        List<Zona> zonas = new ArrayList<>();
        for (Zona zona : itinerario.getZonas()) {
            zonas.add(zona);
        }
        itinerario.getZonas().clear();
        itinerarioRepository.save(itinerario);

        for (Zona zona : zonas) {
            ItinerarioZona itinerarioZona = new ItinerarioZona();
            itinerarioZona.setItinerario(itinerario);
            itinerarioZona.setZona(zona);
            itinerarioZonaRepository.save(itinerarioZona);
        }
        return "redirect:/";
    }
    
    @PostMapping("/guardarZona")
    public String guardarZona(@Valid Zona zona, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "redirect:/";
        }
        Itinerario itinerario = itinerarioRepository.findById(111L).orElse(null);
        zona.setItinerario(itinerario);
        zonaRepository.save(zona);
        return "redirect:/";
    }
    
    @PostMapping("/guardarHabitat")
    public String guardarZona(@Valid Habitat habitat, Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "redirect:/";
        }
        Especie especie = especieRepository.findById(11L).orElse(null);
        habitat.setEspecie(especie);
        habitatRepository.save(habitat);
        return "redirect:/";
    }
}
