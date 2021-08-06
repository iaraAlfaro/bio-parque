package arg.com.bioparque.web;

import arg.com.bioparque.data.CuidadorEspecieRepository;
import arg.com.bioparque.data.EspecieRepository;
import arg.com.bioparque.data.GuiaItinerarioRepository;
import arg.com.bioparque.data.ItinerarioRepository;
import arg.com.bioparque.data.ItinerarioZonaRepository;
import arg.com.bioparque.data.PersonaRepository;
import arg.com.bioparque.data.ZonaRepository;
import arg.com.bioparque.domain.CuidadorEspecie;
import arg.com.bioparque.domain.Especie;
import arg.com.bioparque.domain.GuiaItinerario;
import arg.com.bioparque.domain.Itinerario;
import arg.com.bioparque.domain.ItinerarioZona;
import arg.com.bioparque.domain.Persona;
import arg.com.bioparque.domain.Zona;
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

    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user) {
        log.info("- Ejecutando el controlador String MVC");
        log.info("- Usuario que hizo login: " + user);
        
        Persona persona = personaRepository.findByUserName(user.getUsername());
        model.addAttribute("persona", persona);

        List<Persona> personas = personaRepository.findAll();
        model.addAttribute("personas", personas);

        List<CuidadorEspecie> cuidadorEspecies = cuidadorEspecieRepository.findAll();
        model.addAttribute("cuidadorEspecies", cuidadorEspecies);

        List<Especie> especies = especieRepository.findAll();
        model.addAttribute("especies", especies);

        List<GuiaItinerario> guiaItinerarios = guiaItinerarioRepository.findByPersona(persona);
        model.addAttribute("guiaItinerarios", guiaItinerarios);

        return "index";
    }

    @GetMapping("/eliminarCuidadorEspecie")
    public String eliminarCuidadorEspecie(CuidadorEspecie cuidadorEspecie) {
        cuidadorEspecieRepository.delete(cuidadorEspecie);
        return "redirect:/";
    }

    @GetMapping("/editarCuidadorEspecie/{idCuidadorEspecie}")
    public String editarCuidadorEspecie(CuidadorEspecie cuidadorEspecie, Model model) {
        cuidadorEspecie = cuidadorEspecieRepository.findById(cuidadorEspecie.getIdCuidadorEspecie()).orElse(null);
        model.addAttribute("cuidadorEspecie", cuidadorEspecie);
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
        Especie especie = especieRepository.save(cuidadorEspecie.getEspecie());
        Persona persona = personaRepository.findById(cuidadorEspecie.getPersona().getIdPersona()).orElse(null);
        cuidadorEspecie.setEspecie(especie);
        cuidadorEspecie.setPersona(persona);
        cuidadorEspecieRepository.save(cuidadorEspecie);
        return "redirect:/";
    }

    @PostMapping("/adminCuidadorEspecieEditado")
    public String adminCuidadorEspecieEditado(
            @Valid CuidadorEspecie cuidadorEspecie,
            Errors errores) {
        if (errores.hasErrors()) {
            return "adminModificarCuidadorEspecie";
        }
        System.out.println(cuidadorEspecie);
        Persona persona = personaRepository.findById(cuidadorEspecie.getPersona().getIdPersona()).orElse(null);
        especieRepository.save(cuidadorEspecie.getEspecie());
        cuidadorEspecie.setPersona(persona);
        cuidadorEspecieRepository.save(cuidadorEspecie);

        return "redirect:/";
    }

    @GetMapping("/eliminarPersona")
    public String eliminarPersona(Persona persona) {
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
        if (persona.getRol().equals("cuidador")) {
            List<CuidadorEspecie> cuidadorEspecies = cuidadorEspecieRepository.findByPersona(persona);
            model.addAttribute("cuidadorEspecies", cuidadorEspecies);
            List<Especie> especies = especieRepository.findAll();
            model.addAttribute("especies", especies);
            CuidadorEspecie cuidadorEspecie = new CuidadorEspecie();
            cuidadorEspecie.setPersona(persona);
            model.addAttribute("cuidadorEspecie", cuidadorEspecie);
            return "adminModificarCuidadorEspecie";
        } else if (persona.getRol().equals("guia")) {
            List<GuiaItinerario> guiaItinerarios = guiaItinerarioRepository.findByPersona(persona);
            model.addAttribute("guiaItinerarios", guiaItinerarios);
            GuiaItinerario guiaItinerario = new GuiaItinerario();
            Itinerario itinerario = new Itinerario();
            guiaItinerario.setItinerario(itinerario);
            guiaItinerario.setPersona(persona);
            model.addAttribute("guiaItinerario", guiaItinerario);
            List<Zona> zonas = zonaRepository.findAll();
            model.addAttribute("zonas", zonas);
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
        return "redirect:/";
    }

    @GetMapping("/eliminarGuiaItinerario")
    public String eliminarGuiaItinerario(GuiaItinerario guiaItinerario) {
        guiaItinerarioRepository.delete(guiaItinerario);
        return "redirect:/";
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
        Itinerario itinerario = itinerarioRepository.save(guiaItinerario.getItinerario());
        guiaItinerario.setPersona(persona);
        guiaItinerarioRepository.save(guiaItinerario);
        return "redirect:/";
    }

    @PostMapping("/adminGuiaItinerarioEditado")
    public String adminGuiaItinerarioEditado(
            @Valid GuiaItinerario guiaItinerario,
            Errors errores) {
        if (errores.hasErrors()) {
            return "adminModificarGuia";
        }
        List<Zona> zonas = new ArrayList<>();
        for(Zona zona : guiaItinerario.getItinerario().getZonas()){
            zonas.add(zona);
        }
        guiaItinerario.getItinerario().getZonas().clear();
        
        Persona persona = personaRepository.findById(guiaItinerario.getPersona().getIdPersona()).orElse(null);
        Itinerario itinerario = itinerarioRepository.save(guiaItinerario.getItinerario());
        guiaItinerario.setPersona(persona);
//     
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

        return "redirect:/";
    }

    @PostMapping("/guardarGuiaItinerario")
    public String guardarGuiaItinerario(
            @Valid GuiaItinerario guiaItinerario,
            Errors errores) {
        if (errores.hasErrors()) {
            System.out.println(errores.getAllErrors());
            return "adminModificarCuidadorEspecie";
        }

        
        List<Zona> zonas = new ArrayList<>();
        for(Zona zona : guiaItinerario.getItinerario().getZonas()){
            zonas.add(zona);
        }
        
        guiaItinerario.getItinerario().getZonas().clear();
        Itinerario itinerario = itinerarioRepository.save(guiaItinerario.getItinerario());
        Persona persona = personaRepository.findById(guiaItinerario.getPersona().getIdPersona()).orElse(null);

        for (Zona zona : zonas) {
            ItinerarioZona itinerarioZona = new ItinerarioZona();
            itinerarioZona.setItinerario(itinerario);
            itinerarioZona.setZona(zona);
            itinerarioZonaRepository.save(itinerarioZona);
        }

        guiaItinerario.setItinerario(itinerario);
        guiaItinerario.setPersona(persona);

        guiaItinerarioRepository.save(guiaItinerario);
        return "redirect:/";
    }

    @GetMapping("/agregarGuiaItinerario/{idPersona}")
    public String agregarGuiaItinerario(
            @Valid Persona persona,
            Model model,
            Errors errores) {
        persona = personaRepository.findById(persona.getIdPersona()).orElse(null);
        GuiaItinerario guiItinerario = new GuiaItinerario();
        guiItinerario.setPersona(persona);
        model.addAttribute("guiaItinerario", guiItinerario);
        List<Zona> zonas = zonaRepository.findAll();
        model.addAttribute("zonas", zonas);
        return "agregarGuiaItinerarioPrueba";
    }
}
