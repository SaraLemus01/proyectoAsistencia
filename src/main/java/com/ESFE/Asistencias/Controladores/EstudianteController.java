package com.ESFE.Asistencias.Controladores;

import com.ESFE.Asistencias.Entidades.Estudiante;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteGrupoService;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IEstudianteServices;
import com.ESFE.Asistencias.Servicios.Interfaces.IGrupoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {
    @Autowired
    private IEstudianteServices estudianteServices;

    @Autowired
    private IEstudianteServices estudianteService;

    @Autowired
    private IGrupoServices grupoServices;


    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Restar 1 para ajustar la página actual
        int pageSize = size.orElse(5); // Tamaño de la página
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Estudiante> estudiantes = estudianteServices.BuscarTodosPaginados(pageable);
        model.addAttribute("estudiantes", estudiantes);

        int totalPage = estudiantes.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "estudiante/index";
    }

    @GetMapping("/create")
    public String create(Estudiante estudiante) {
        return "estudiante/create";
    }

    @PostMapping("/save")
    public String save(Estudiante estudiante, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("estudiante", estudiante);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "estudiante/create";
        }
        estudianteServices.CrearOeditar(estudiante);
        attributes.addFlashAttribute("msg", "Estudiante creado correctamente");
        return "redirect:/estudiantes";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Estudiante estudiante = estudianteServices.BuscarPorId(id).orElse(null);
        if (estudiante != null) {
            model.addAttribute("estudiante", estudiante);
            return "estudiante/details";
        } else {
            return "redirect:/estudiantes";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Estudiante estudiante = estudianteServices.BuscarPorId(id).orElse(null);
        if (estudiante != null) {
            model.addAttribute("estudiante", estudiante);
            return "estudiante/edit";
        } else {
            return "redirect:/estudiantes";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Estudiante estudiante = estudianteServices.BuscarPorId(id).orElse(null);
        if (estudiante != null) {
            model.addAttribute("estudiante", estudiante);
            return "estudiante/delete";
        } else {
            return "redirect:/estudiantes";
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        if (estudianteServices.BuscarPorId(id).isPresent()) {
            estudianteServices.EliminarPorId(id);
            attributes.addFlashAttribute("msg", "Estudiante eliminado correctamente");
        } else {
            attributes.addFlashAttribute("error", "Estudiante no encontrado");
        }
        return "redirect:/estudiantes";
    }
}


