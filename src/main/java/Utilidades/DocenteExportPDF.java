package Utilidades;

import com.ESFE.Asistencias.Entidades.Docente;
import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DocenteExportPDF {
    // Lista de docentes que se va a exportar en el PDF
    private List<Docente> listaDocentes;


    public DocenteExportPDF(List<Docente> listaDocentes){
        this.listaDocentes = listaDocentes;
    }


    private void setCabeceraTabla(PdfPTable tabla){
        PdfPCell celda = new PdfPCell();

        Color colorPersonalizado = new Color(100, 150, 200);
        celda.setBackgroundColor(colorPersonalizado);
        celda.setPadding(5);

        com.lowagie.text.Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.white);

        celda.setPhrase(new Phrase("Id",fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Nombre",fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Apellido",fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Email",fuente));
        tabla.addCell(celda);


    }

    private  void SetDatosTabla(PdfPTable tabla){
        for(Docente docente: listaDocentes){
            // Añadir una fila con los datos de cada docente
            tabla.addCell(String.valueOf(docente.getId()));
            tabla.addCell(docente.getNombre());
            tabla.addCell(docente.getApellido());
            tabla.addCell(docente.getEmail());
        }
    }

    // Método para exportar el documento PDF
    public void Exportar(HttpServletResponse response) throws IOException {

        Document documento = new Document(PageSize.A4);

        PdfWriter.getInstance(documento,response.getOutputStream());

        documento.open();

        try {
            // Cargar la imagen desde los recursos
            InputStream inputStream = getClass().getResourceAsStream("/static/dist/img/logo.png");
            if (inputStream != null) {
                com.lowagie.text.Image imagen = com.lowagie.text.Image.getInstance(ImageIO.read(inputStream), null);
                imagen.scaleToFit(100, 50); // Ajustar el tamaño de la imagen
                imagen.setAbsolutePosition(50, 750); // Posicionar la imagen en la esquina superior izquierda
                documento.add(imagen);
            } else {
                System.out.println("No se pudo cargar la imagen");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        documento.add(new com.lowagie.text.Paragraph(" "));
        documento.add(new Paragraph(" "));

        // Fuente para el título del documento
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(Color.BLACK);
        fuente.setSize(18);

        // Crear y agregar el título al documento
        Paragraph titulo = new Paragraph("Lista Docentes",fuente);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        // Crear la tabla con 4 columnas
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths(new float[] {1f,2f,2f,3.5f});// Ancho relativo de las columnas

        setCabeceraTabla(tabla);
        SetDatosTabla(tabla);

        // Añadir la tabla al documento
        documento.add(tabla);
        // Cerrar el documento
        documento.close();
    }
}


