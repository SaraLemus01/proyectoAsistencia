package com.ESFE.Asistencias.Controladores;

import com.ESFE.Asistencias.Entidades.Docente;
import com.ESFE.Asistencias.Entidades.DocenteGrupo;
import com.ESFE.Asistencias.Entidades.Grupo;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteGrupoService;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteServices;
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
@RequestMapping("/docenteGrupo")
public class DocenteGrupoController {

    @Autowired
    private IDocenteGrupoService docenteGrupoService;

    @Autowired
    private IDocenteServices docenteServices;

    @Autowired
    private IGrupoServices grupoServices;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Page number is zero-based
        int pageSize = size.orElse(5); // Default page size
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<DocenteGrupo> docenteGrupo = docenteGrupoService.BuscarTodosPaginados(pageable);
        model.addAttribute("docenteGrupo", docenteGrupo);

        int totalPage = docenteGrupo.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "docenteGrupo/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("docenteGrupo", new DocenteGrupo());
        model.addAttribute("docentes", docenteServices.ObtenerTodos());
        model.addAttribute("grupos", grupoServices.ObtenerTodos());
        return "docenteGrupo/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer docenteId,
                       @RequestParam Integer grupoId,
                       @RequestParam Integer anio,
                       @RequestParam String ciclo,
                       RedirectAttributes attributes) {
        Optional<Docente> docenteOptional = docenteServices.BuscarPorId(docenteId);
        Optional<Grupo> grupoOptional = grupoServices.BuscarPorId(grupoId);

        if (docenteOptional.isPresent() && grupoOptional.isPresent()) {
            Docente docente = docenteOptional.get();
            Grupo grupo = grupoOptional.get();

            DocenteGrupo docenteGrupo = new DocenteGrupo();
            docenteGrupo.setDocente(docente);
            docenteGrupo.setGrupo(grupo);
            docenteGrupo.setAnio(anio);
            docenteGrupo.setCiclo(ciclo);

            docenteGrupoService.CrearOeditar(docenteGrupo);
            attributes.addFlashAttribute("msg", "Asignación guardada correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se pudo guardar la asignación. Verifique los datos.");
        }
        return "redirect:/docenteGrupo";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Optional<DocenteGrupo> docenteGrupoOptional = docenteGrupoService.BuscarPorId(id);
        if (docenteGrupoOptional.isPresent()) {
            model.addAttribute("docenteGrupo", docenteGrupoOptional.get());
        } else {
            model.addAttribute("error", "No se encontró la asignación con ID: " + id);
        }
        return "docenteGrupo/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        DocenteGrupo docenteGrupo = docenteGrupoService.BuscarPorId(id).orElseThrow(() -> new RuntimeException("No se encontró el DocenteGrupo con ID: " + id));

        model.addAttribute("docentes", docenteServices.ObtenerTodos());
        model.addAttribute("grupos", grupoServices.ObtenerTodos());
        model.addAttribute("docenteGrupo", docenteGrupo);
        return "docenteGrupo/edit";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute DocenteGrupo docenteGrupo, RedirectAttributes attributes){
        Optional<Docente> docenteOptional = docenteServices.BuscarPorId(docenteGrupo.getDocente().getId());
        Optional<Grupo> grupoOptional = grupoServices.BuscarPorId(docenteGrupo.getGrupo().getId());

        if (docenteOptional.isPresent() && grupoOptional.isPresent()) {
            Docente docente = docenteOptional.get();
            Grupo grupo = grupoOptional.get();

            docenteGrupo.setDocente(docente);
            docenteGrupo.setGrupo(grupo);

            docenteGrupoService.CrearOeditar(docenteGrupo);
            attributes.addFlashAttribute("msg", "Asignación modificada correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se pudo encontrar el docente o grupo.");
        }

        return "redirect:/docenteGrupo";
    }


    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Optional<DocenteGrupo> docenteGrupoOptional = docenteGrupoService.BuscarPorId(id);
        if (docenteGrupoOptional.isPresent()) {
            model.addAttribute("docenteGrupo", docenteGrupoOptional.get());
            return "docenteGrupo/delete"; // Redirect to delete confirmation view
        } else {
            model.addAttribute("error", "No se encontró la asignación con ID: " + id);
            return "docenteGrupo/error"; // Redirect to error view
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Integer id, RedirectAttributes attributes) {
        if (docenteGrupoService.BuscarPorId(id).isPresent()) {
            docenteGrupoService.EliminarPorId(id);
            attributes.addFlashAttribute("msg", "Asignación eliminada correctamente");
        } else {
            attributes.addFlashAttribute("error", "No se pudo eliminar la asignación. Verifique el ID.");
        }
        return "redirect:/docenteGrupo";
    }
}
