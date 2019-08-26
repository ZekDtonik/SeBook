package struts2;

import Core.ResultMessage;
import Persistence.Category;
import Persistence.Cnn.DAO;
import Persistence.Persister;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ParentPackage("mainPackage")

public class CategoryManagerAction extends ActionSupport {

    HttpServletRequest request = ServletActionContext.getRequest();
    Map<String,Object> result = new HashMap<>();

    public Map<String, Object> getResult() {
        return result;
    }

    @Override
    public String execute(){

        return SUCCESS;
    }
    @Action(value = "/system/category/add")
    public String add(){

        if(this.request.getParameterMap().size() != 0){
            String name = this.request.getParameter("name");
            if(name.equals("")){
                this.result = ResultMessage.applyResult("CM00",false,"O nome da categoria deve ser preenchido.");
            }
            else{
                Category category = new Category();
                category.setName(name);
                boolean result = DAO.insert(category);
                if(DAO.getError() != null && DAO.getError().getMessage().contains("Duplicate entry ")){
                    this.result = ResultMessage.applyResult("CM10",false,"O nome da categoria já existe no banco de dados.");
                }
                else if (result){
                    this.result = ResultMessage.applyResult("CM200",true,"A categoria foi incluída com sucesso.");
                }
                else{
                    this.result = ResultMessage.applyResult("CM300", false, "A categoria não pode ser salva.");
                }

            }
        }
        else{
            this.result = ResultMessage.applyResult("CM400", false, "Nenhuma estrutura válida de dados foi recebida na requisição.");
        }
        return SUCCESS;
    }
    @Action(value = "/system/category/edit")
    public String edt(){
        String name = this.request.getParameter("name");
        String edit = this.request.getParameter("newName");

        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        Persister objectToEdit = DAO.findOne("SELECT ctg FROM Category AS ctg WHERE name=:name",map);

        if(name == null || name.equals("")){
            this.result = ResultMessage.applyResult("CM10",false,"A resolução para alteração do conteúdo não foi encontrado");
        }
        else if (edit ==null || edit.equals("")){
            this.result = ResultMessage.applyResult("CM15",false,"O novo valor não pode ficar em branco.");
        }
        else if (objectToEdit == null){
            this.result = ResultMessage.applyResult("CM20",false,"O novo valor não pode ficar em branco.");
        }
        else {
            ((Category) objectToEdit).setName(edit);
            boolean result = DAO.update(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("CM200",true,"A Categoria foi atualizado com sucesso");
            }
            else {
                this.result = ResultMessage.applyResult("CM300",false,"A categoria não pôde ser alterada.");
            }
        }
        return SUCCESS;
    }

    @Action(value = "/system/category/remove")
    public String del(){
        String name = this.request.getParameter("name");


        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        Persister objectToEdit = DAO.findOne("SELECT ctg FROM Category AS ctg WHERE name=:name",map);

        if(name.equals("")) {
            this.result = ResultMessage.applyResult("CM10", false, "A resolução para alteração do conteúdo não foi encontrado");
        }
        else if (objectToEdit == null){
            this.result = ResultMessage.applyResult("CM20",false,"O novo valor não pode ficar em branco.");
        }
        else {

            boolean result = DAO.delete(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("CM200",true,"A Categoria foi removida com sucesso");
            }
            else {
                this.result = ResultMessage.applyResult("CM300",false,"A categoria não pôde ser removida.");
            }
        }
        return SUCCESS;
    }
}
