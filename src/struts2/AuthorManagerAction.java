package struts2;

import Core.ResultMessage;
import Persistence.Author;
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

public class AuthorManagerAction extends ActionSupport {

    HttpServletRequest request = ServletActionContext.getRequest();
    Map<String,Object> result = new HashMap<>();

    public Map<String, Object> getResult() {
        return result;
    }

    @Override
    public String execute(){

        return SUCCESS;
    }
    @Action(value = "/system/author/add")
    public String add(){

        if(this.request.getParameterMap().size() != 0){
            String name = this.request.getParameter("name");
            if(name.equals("")){
                this.result = ResultMessage.applyResult("AT00",false,"O nome da autor deve ser preenchido.");
            }
            else{
                Author author = new Author();
                author.setName(name);
                boolean result = DAO.insert(author);
                if(DAO.getError() != null && DAO.getError().getMessage().contains("Duplicate entry ")){
                    this.result = ResultMessage.applyResult("AT10",false,"O nome da autor já existe no banco de dados.");
                }
                else if (result){
                    this.result = ResultMessage.applyResult("AT200",true,"O autor foi incluído com sucesso.");
                }
                else{
                    this.result = ResultMessage.applyResult("AT300", false, "O autor não pode ser salva.");
                }

            }
        }
        else{
            this.result = ResultMessage.applyResult("AT400", false, "Nenhuma estrutura válida de dados foi recebida na requisição.");
        }
        return SUCCESS;
    }
    @Action(value = "/system/author/edit")
    public String edt(){
        String name = this.request.getParameter("name");
        String edit = this.request.getParameter("newName");

        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        Persister objectToEdit = DAO.findOne("SELECT atr FROM Author AS atr WHERE name=:name",map);

        if(name == null || name.equals("")){
            this.result = ResultMessage.applyResult("AT10",false,"A resolução para alteração do conteúdo não foi encontrado");
        }
        else if (edit ==null || edit.equals("")){
            this.result = ResultMessage.applyResult("AT15",false,"O novo valor não pode ficar em branco.");
        }
        else if (objectToEdit == null){
            this.result = ResultMessage.applyResult("AT20",false,"O novo valor não pode ficar em branco.");
        }
        else {
            ((Author) objectToEdit).setName(edit);
            boolean result = DAO.update(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("AT200",true,"O autor foi atualizado com sucesso");
            }
            else {
                this.result = ResultMessage.applyResult("AT300",false,"O autor não pôde ser alterado.");
            }
        }
        return SUCCESS;
    }

    @Action(value = "/system/author/remove")
    public String del(){
        String name = this.request.getParameter("name");


        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        Persister objectToEdit = DAO.findOne("SELECT atr FROM Author AS atr WHERE name=:name",map);

        if(name.equals("")) {
            this.result = ResultMessage.applyResult("AT10", false, "A resolução para remoção do conteúdo não foi encontrado");
        }
        else if (objectToEdit == null){
            this.result = ResultMessage.applyResult("AT20",false,"O autor que está tentando remover não existe.");
        }
        else {

            boolean result = DAO.delete(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("AT200",true,"O autor foi removido com sucesso");
            }
            else {
                this.result = ResultMessage.applyResult("AT300",false,"O autor não pôde ser removido.");
            }
        }
        return SUCCESS;
    }
}
