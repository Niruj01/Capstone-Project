package com.BusReservation.capstone.Service;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

@Service
public class PdfCreationService {

    @Autowired
    private TemplateEngine templateEngine;

    public void createPdf(String template, Map<String, Object> variables, String outputPdf) {
        Context context = new Context();
        context.setVariables(variables);

        String html = templateEngine.process(template, context);
        try (FileOutputStream fos = new FileOutputStream(outputPdf)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(fos, false);
            renderer.finishPDF();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + outputPdf, e);
        } catch (DocumentException e) {
            throw new RuntimeException("PDF creation error", e);
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed: " + e.getMessage(), e);
        }
    }
}
