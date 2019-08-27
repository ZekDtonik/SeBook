package struts2;

import Core.ResultMessage;
import Persistence.Cnn.DAO;
import Persistence.Products;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParentPackage(value = "mainPackage")
public class ListProducts extends ActionSupport {

    private List<Persistence.Products> products = new ArrayList<>();
    private String actionValue;
    public List<Persistence.Products> getListPrd() {
        return products;
    }
    public void setProducts(List<Persistence.Products> listPrd) {
        this.products = listPrd;
    }

    public ListProducts() {
        String[] uri = ServletActionContext.getRequest().getRequestURI().split("/");
        if(uri.length >= 4)
            this.actionValue = uri[4];
    }

    @Action(value = "/system/products")
    public String findAll(){
        List<Persistence.Products> products = DAO.findAll("SELECT pj FROM Products as pj");
        this.setProducts(products);
        return SUCCESS;
    }
    @Action(value = "/system/products/id/*")
    public String findOne(){

        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher matcher = pattern.matcher(this.actionValue);
        if(matcher.matches()){
            Products product = (Products) DAO.getById(Products.class,Integer.parseInt(this.actionValue));
            this.products.add(product);

        }

        return SUCCESS;
    }
}
