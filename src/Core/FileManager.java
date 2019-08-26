package Core;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    // Create a factory for disk-based file items
    private DiskFileItemFactory factory = new DiskFileItemFactory();
    private HttpServletRequest request= ServletActionContext.getRequest();
    // Configure a repository (to ensure a secure temp location is used)
    private ServletContext servletContext = ServletActionContext.getRequest().getServletContext();
    private File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    List<FileItem> files = new ArrayList<>();
    private FileManager() throws FileUploadException {


        factory.setRepository(repository);
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(1024);

        // Parse the request
        List<FileItem> items = upload.parseRequest(request);

        // Process the uploaded items
        for (FileItem item : items) {
            if (!item.isFormField()) {
                this.files.add(item);
            }
        }
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public static List<FileItem> uploaded() {
        try{
            FileManager fileManager = new FileManager();
            return fileManager.getFiles();
        }
        catch (FileUploadException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
