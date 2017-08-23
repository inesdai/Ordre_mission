/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veganet.gps.jsf;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.veganet.gps.entities.Notification;
import com.veganet.gps.entities.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

/**
 *
 * @author user
 */
public class PdfNotificationConverter {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private User connectedUser;

    public PdfNotificationConverter(User connectedUser) {

        this.connectedUser = connectedUser;
    }

    public void exportPDF(DataModel items) {

        try {

            Document document = new Document();

            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //PrintStream ps = new PrintStream(baos);
            String FILE = "/home/veganet/pdfExport/" + connectedUser.getUserid() + "NotificationPdf.pdf";

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));

            document.open();

            generatePDF(document, writer, items);

            document.close();

            download();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void generatePDF(Document document, PdfWriter writer, DataModel items)
            throws DocumentException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Paragraph preface2 = new Paragraph("Liste des notification", catFont);

        preface2.setAlignment(Element.ALIGN_CENTER);

        document.add(preface2);
        Paragraph preface = new Paragraph();

        addEmptyLine(preface, 1);
        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);

        PdfPCell c1 = new PdfPCell(new Phrase("ID"));

        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Type notification"));

        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description"));

        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));

        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Véhicule"));

        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        table.setHeaderRows(1);

        float[] columnWidths = new float[]{6f, 15f, 26f, 18f, 15f};

        table.setWidths(columnWidths);

        Iterator i = items.iterator();

        Notification no;

        while (i.hasNext()) {

            no = (Notification) i.next();

            table.addCell(no.getNotificationid().toString());

            table.addCell((no.getTypenotification() == 0) ? "Notification Systeme" : "Alerte");

            table.addCell(no.getDescription().toString());

            table.addCell(format.format(no.getTimenotification()).toString());

            table.addCell(no.getDeviceid().getVehicleid().getDescription().toString());

        }

        preface.add(table);

        document.add(preface);

    }

    private void addEmptyLine(Paragraph paragraph, int number) {

        for (int i = 0; i < number; i++) {

            paragraph.add(new Paragraph(" "));

        }

    }

    public void download() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.

        String basepath = "/home/veganet/pdfExport/" + connectedUser.getUserid() + "NotificationPdf.pdf";

        File file = new File(basepath);

        Path path = Paths.get(basepath);

        String contentType = Files.probeContentType(path);

        ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.

        //ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "FirstPdf.pdf" + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream outputStream = ec.getResponseOutputStream();

        // Copy uploaded file to destination path
        InputStream inputStream = null;

        try {

            inputStream = new FileInputStream(file);

            int read = 0;

            final byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {

                outputStream.write(bytes, 0, read);

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (outputStream != null) {

                outputStream.close();

            }

            if (inputStream != null) {

                inputStream.close();

            }

        }

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

    }
}
