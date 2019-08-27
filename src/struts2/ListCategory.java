package struts2;

import Persistence.Cnn.DAO;
import Persistence.Category;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParentPackage(value = "mainPackage")
public class ListCategory extends ActionSupport {

    private List<Category> categorys = new ArrayList<>();
    private String actionValue;
    public List<Category> getListCategory() {
        return categorys;
    }
    public void setCategory(List<Category> listPrd) {
        this.categorys = listPrd;
    }

    public ListCategory() {
        String[] uri = ServletActionContext.getRequest().getRequestURI().split("/");
        if(uri.length >= 4)
            this.actionValue = uri[4];
    }

    @Action(value = "/system/categorys")
    public String findAll(){
        List<Category> categorys = DAO.findAll("SELECT ctg FROM Category as ctg");
        this.setCategory(categorys);
        return SUCCESS;
    }
    @Action(value = "/system/categorys/id/*")
    public String findOne(){

        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher matcher = pattern.matcher(this.actionValue);
        if(matcher.matches()){
            Category category = (Category) DAO.getById(Category.class,Integer.parseInt(this.actionValue));
            this.categorys.add(category);

        }

        return SUCCESS;
    }
}
