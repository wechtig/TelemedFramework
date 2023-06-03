package at.fhjoanneum.telemedframework.telemedframeworkinitializer.controller;

import at.fhjoanneum.telemedframework.telemedframeworkinitializer.dto.DownloadDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class InitializerController {
    @PostMapping("/download")
    public void downloadModules(HttpServletResponse response, @RequestBody DownloadDto downloadDtod) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"telemedframework.zip\"");

        String propertyFile = "ACTIVE=";

        try {
            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            addToZip("modules/root.jar", zipOut);
            addToZip("files/createroot.sql", zipOut);

            if(downloadDtod.isSymptom()) {
                addToZip("modules/symptom.jar", zipOut);
                addToZip("files/createsymptom.sql", zipOut);
                propertyFile += "SYMPTOM-";
            }
            if(downloadDtod.isCommunication()) {
                addToZip("modules/communication.jar", zipOut);
                addToZip("files/createcommunication.sql", zipOut);
                propertyFile += "COMMUNICATION-";
            }
            if(downloadDtod.isCourse()) {
                addToZip("modules/course.jar", zipOut);
                addToZip("files/createcourse.sql", zipOut);
                propertyFile += "COURSE-";
            }
            if(downloadDtod.isAppointment()) {
                addToZip("modules/appointment.jar", zipOut);
                addToZip("files/createappointment.sql", zipOut);
                propertyFile += "APPOINTMENT-";
            }
            if(downloadDtod.isExport()) {
                addToZip("modules/export.jar", zipOut);
                propertyFile += "EXPORT-";
            }

            if(downloadDtod.getColor() != "") {
                propertyFile += "\nCOLOR="+downloadDtod.getColor();
            }

            if(downloadDtod.getPraxisname() != "") {
                propertyFile += "\nPRAXISNAME="+downloadDtod.getPraxisname();
            }

            if(downloadDtod.getLogo() != null && downloadDtod.getLogo() != "") {
                ZipEntry zipEntryLogo = new ZipEntry("logo.png");
                zipOut.putNextEntry(zipEntryLogo);
                String partSeparator = ",";
                String encodedImg = downloadDtod.getLogo().split(partSeparator)[1];
                zipOut.write(Base64.getDecoder().decode(encodedImg));
            }

            ZipEntry zipEntry = new ZipEntry("telemed.properties");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(propertyFile.getBytes());

            zipOut.close();
            response.flushBuffer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToZip(String filename, ZipOutputStream zipOutputStream) {
        File inputFile = new File(filename);
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
