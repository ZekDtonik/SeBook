package struts2;

import Persistence.Cnn.DAO;
import Persistence.Author;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParentPackage(value = "mainPackage")
public class ListAuthor extends ActionSupport {

    private List<Author> authors = new ArrayList<>();
    private String actionValue;
    public List<Author> getListAuthors() {
        return authors;
    }
    public void setAuthor(List<Author> listPrd) {
        this.authors = listPrd;
    }

    public ListAuthor() {
        String[] uri = ServletActionContext.getRequest().getRequestURI().split("/");
        if(uri.length >= 4)
            this.actionValue = uri[4];
    }

    @Action(value = "/system/authors!/")
    public String findAll(){
        List<Author> authors = DAO.findAll("SELECT atr FROM Author as atr");
        this.setAuthor(authors);
        return SUCCESS;
    }
    @Action(value = "/system/authors/id/*")
    public String findOne(){

        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher matcher = pattern.matcher(this.actionValue);
        if(matcher.matches()){
            Author author = (Author) DAO.getById(Author.class,Integer.parseInt(this.actionValue));
            this.authors.add(author);

        }

        return SUCCESS;
    }
}
