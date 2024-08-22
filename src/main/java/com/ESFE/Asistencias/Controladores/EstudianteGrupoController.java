package com.ESFE.Asistencias.Controladores;

import com.ESFE.Asistencias.Entidades.*;
import com.ESFE.Asistencias.Servicios.Interfaces.IEstudianteGrupoService;
import com.ESFE.Asistencias.Servicios.Interfaces.IEstudianteServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IGrupoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/estudianteGrupo")
public class EstudianteGrupoController {

    @Autowired
    private IEstudianteGrupoService estudianteGrupoService;

    @Autowired
    private IEstudianteServices estudianteServices;

    @Autowired
    private IGrupoServices grupoServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Page number is zero-based
        int pageSize = size.orElse(5); // Default page size
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<EstudianteGrupo> estudianteGrupo = estudianteGrupoService.BuscarTodosPaginados(pageable);
        model.addAttribute("estudianteGrupo", estudianteGrupo);

        int totalPage = estudianteGrupo.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "estudianteGrupo/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("estudianteGrupo", new EstudianteGrupo());
        model.addAttribute("estudiantes", estudianteServices.ObtenerTodos());
        model.addAttribute("grupos", grupoServices.ObtenerTodos());
        return "estudianteGrupo/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer estudianteId,
                       @RequestParam Integer grupoId,

                       RedirectAttributes attributes) {
        Optional<Estudiante> estudianteOptional = estudianteServices.BuscarPorId(estudianteId);
        Optional<Grupo> grupoOptional = grupoServices.BuscarPorId(grupoId);

        if (estudianteOptional.isPresent() && grupoOptional.isPresent()) {
            Estudiante estudiante = estudianteOptional.get();
            Grupo grupo = grupoOptional.get();

            EstudianteGrupo estudianteGrupo = new EstudianteGrupo();
            estudianteGrupo.setEstudiante(estudiante);
            estudianteGrupo.setGrupo(grupo);


            estudianteGrupoService.CrearOeditar(estudianteGrupo);
            attributes.addFlashAttribute("msg", "Asignación guardada correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se pudo guardar la asignación. Verifique los datos.");
        }
        return "redirect:/estudianteGrupo";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Optional<EstudianteGrupo> estudianteGrupoOptional = estudianteGrupoService.BuscarPorId(id);
        if (estudianteGrupoOptional.isPresent()) {
            model.addAttribute("estudianteGrupo", estudianteGrupoOptional.get());
        } else {
            model.addAttribute("error", "No se encontró la asignación con ID: " + id);
        }
        return "estudianteGrupo/details";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        EstudianteGrupo estudianteGrupo = estudianteGrupoService.BuscarPorId(id).orElseThrow(() -> new RuntimeException("No se encontró el esudianteGrupo con ID: " + id));

        model.addAttribute("estudiantes", estudianteServices.ObtenerTodos());
        model.addAttribute("grupos", grupoServices.ObtenerTodos());
        model.addAttribute("estudianteGrupo", estudianteGrupo);
        return "estudianteGrupo/edit";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute EstudianteGrupo estudianteGrupo, RedirectAttributes attributes){
        Optional<Estudiante> estudianteOptional = estudianteServices.BuscarPorId(estudianteGrupo.getEstudiante().getId());
        Optional<Grupo> grupoOptional = grupoServices.BuscarPorId(estudianteGrupo.getGrupo().getId());

        if (estudianteOptional.isPresent() && grupoOptional.isPresent()) {
           Estudiante estudiante = estudianteOptional.get();
            Grupo grupo = grupoOptional.get();

            estudianteGrupo.setEstudiante(estudiante);
            estudianteGrupo.setGrupo(grupo);

            estudianteGrupoService.CrearOeditar(estudianteGrupo);
            attributes.addFlashAttribute("msg", "Asignación modificada correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se pudo encontrar el estudiante o grupo.");
        }

        return "redirect:/estudianteGrupo";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Optional<EstudianteGrupo> estudianteGrupoOptional = estudianteGrupoService.BuscarPorId(id);
        if (estudianteGrupoOptional.isPresent()) {
            model.addAttribute("estudianteGrupo", estudianteGrupoOptional.get());
            return "estudianteGrupo/delete"; // Redirect to delete confirmation view
        } else {
            model.addAttribute("error", "No se encontró la asignación con ID: " + id);
            return "estudianteGrupo/error"; // Redirect to error view
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Integer id, RedirectAttributes attributes) {
        if (estudianteGrupoService.BuscarPorId(id).isPresent()) {
            estudianteGrupoService.EliminarPorId(id);
            attributes.addFlashAttribute("msg", "Asignación eliminada correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se pudo eliminar la asignación. Verifique el ID.");
        }
        return "redirect:/estudianteGrupo";
    }
}
