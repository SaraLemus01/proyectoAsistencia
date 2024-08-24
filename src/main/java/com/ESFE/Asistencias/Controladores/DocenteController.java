package com.ESFE.Asistencias.Controladores;

import Utilidades.DocenteExportPDF;
import com.ESFE.Asistencias.Entidades.Docente;
import com.ESFE.Asistencias.Entidades.Grupo;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteServices;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Controller
@RequestMapping("/docentes")
public class DocenteController {
    @Autowired
    private IDocenteServices docenteServices;
    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size")Optional<Integer>size){
        int currentPage = page.orElse(1) -1;//es para restarle un valor para que este laposision cero
        int pageSize = size.orElse(5);//es el tamaño de la pagina donde le asinamos un valor 10 ,por cada pagina abra 10 valores
        Pageable pageable = PageRequest.of(currentPage,pageSize);

        Page<Docente> docentes =docenteServices.BuscarTodosPaginados(pageable);
        model.addAttribute("docentes",docentes);

        int totalPage = docentes.getTotalPages();
        if(totalPage > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPage)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "docente/index";
    }
    @GetMapping("/create")
    public  String create(Docente docente){
        return "docente/create";
    }
    @PostMapping("/save")
    public String save(Docente docente, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(docente);
            attributes.addFlashAttribute("error","No se pudo guardar debido a un error.");
            return "docente/create";
        }
        docenteServices.CrearOeditar(docente);
        attributes.addFlashAttribute("msg","Docente creado correctamente");
        return "redirect:/docentes";
    }
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteServices.BuscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteServices.BuscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/edit";
    }
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteServices.BuscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/delete";
    }
    @PostMapping("/delete")
    public String delete(Docente docente, RedirectAttributes attributes){
        docenteServices.EliminarPorId(docente.getId());
        attributes.addFlashAttribute("msg","Docente eliminado correctamente");
        return "redirect:/docentes";
    }
    @GetMapping("/exportarPDF")
    public void exportarDocentes(HttpServletResponse response) throws IOException {
        // Establece el tipo de contenido de la respuesta como "application/pdf"
        response.setContentType("application/pdf");

        // Crea un objeto SimpleDateFormat para formatear la fecha actual en un formato específico
        DateFormat dateFormattter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

        String fechaActual = dateFormattter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "inline; filename=Docentes_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        // Obtiene una lista de todos los docentes desde el servicio de docentes
        List<Docente> docentes = docenteServices.ObtenerTodos();
        // Crea una instancia de la clase DocenteExportPDF, pasando la lista de docentes como parámetro
        DocenteExportPDF exporter = new DocenteExportPDF(docentes);
        // Llama al método Exportar para generar el PDF y enviarlo en la respuesta HTTP
        exporter.Exportar(response);
    }

}
