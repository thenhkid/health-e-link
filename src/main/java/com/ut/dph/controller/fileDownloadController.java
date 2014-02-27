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
import org.springframework.web.bind.annotation.RequestMethod;
import com.ut.dph.model.Organization;
import com.ut.dph.service.organizationManager;
import com.ut.dph.reference.fileSystem;
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/FileDownload")
public class fileDownloadController {

    @Autowired
    private organizationManager organizationManager;
    
    /**
     * Size of a byte buffer to read/write file
     */
    private static final int BUFFER_SIZE = 4096;

    @RequestMapping(value = "/downloadFile.do", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, @RequestParam String filename, @RequestParam String foldername, @RequestParam(value= "orgId", required = false) Integer orgId, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream in = null;
        ServletContext context = request.getServletContext();
        
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
            
            String mimeType = context.getMimeType(dir.getDir() + filename);
            
            File f = new File(dir.getDir() + filename);
            
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            
            in = new FileInputStream(dir.getDir() + filename);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            
            response.setContentLength((int)f.length());
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
            
            outputStream = response.getOutputStream();
            while (0 < (bytesRead = in.read(buffer))) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            in.close();
            outputStream.close();
            
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
