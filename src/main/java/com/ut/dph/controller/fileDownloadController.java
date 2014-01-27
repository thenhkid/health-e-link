package com.ut.dph.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ut.dph.model.Organization;
import com.ut.dph.service.organizationManager;
import com.ut.dph.reference.fileSystem;

@Controller
@RequestMapping("/fileDownloadController")
public class fileDownloadController {

    @Autowired
    private organizationManager organizationManager;

    @RequestMapping("/downloadFile")
    public void downloadFile(@RequestParam String filename, @RequestParam String foldername, @RequestParam(value= "orgId", required = false) Integer orgId, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream in = null;
        try {
            fileSystem dir = new fileSystem();
            
            if(orgId != null) {

                Organization organization = organizationManager.getOrganizationById(orgId);
                String cleanURL = organization.getcleanURL();

                dir.setDir(cleanURL, foldername);
            }
            else {
                dir.setDirByName(foldername);
            }

            in = new FileInputStream(dir.getDir() + filename);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
            outputStream = response.getOutputStream();
            while (0 < (bytesRead = in.read(buffer))) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
