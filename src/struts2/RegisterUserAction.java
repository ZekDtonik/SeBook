package struts2;

import Core.ResultMessage;
import Persistence.Cnn.DAO;
import Persistence.Users;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;

@ParentPackage("mainPackage")
@Action(value = "/system/register/user", results = @Result(type = "json"))
public class RegisterUserAction extends ActionSupport {

    private HttpServletRequest req = ServletActionContext.getRequest();
    private HttpServletResponse res = ServletActionContext.getResponse();

    private Map<String, Object> result = new HashMap<>();
    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
    @Override
    public String execute(){
        Enumeration forms = req.getParameterNames();
        if (!forms.hasMoreElements()){
            this.result = ResultMessage.applyResult("RG00",false,"Todos os campos são obrigatórios");
        }
        else {
            if(this.req.getMethod().equals("GET") &&
                (this.req.getContentType().contains("x-www-form") ||
                this.req.getContentType().contains("multipart/form"))){
                String nome =  this.req.getParameter("name");
                String login = this.req.getParameter("login");
                String email = this.req.getParameter("email");
                String password = this.req.getParameter("password");
                String repassword = this.req.getParameter("repassword");

                if(nome.equals("") || login.equals("") || email.equals("") || password.equals("") || repassword.equals("")){
                    this.result = ResultMessage.applyResult("RG100",false,"Todos os campos são obrigatórios.");
                }
                else if ( login.length() < 4 || login.length() > 20) {
                    this.result = ResultMessage.applyResult("RG105",false,"Tamanho do logind deve ser maior que 4 caracteres e menor que 20.");
                }
                else if(password.length() < 6 ) {
                    this.result = ResultMessage.applyResult("RG110",false,"A senha deve ser maior que 6 caracteres..");
                }
                else if (!repassword.equals(password)){
                    this.result = ResultMessage.applyResult("RG115",false,"Todos os campos são obrigatórios.");
                }
                else{
                    Users users = new Users();
                    users.setNome(nome);
                    users.setLogin(login);
                    users.setEmail(email);
                    users.setPassword(password);
                    users.setLevel(2);

                    boolean result =DAO.insert(users);
                    if(result){
                        this.result = ResultMessage.applyResult("RG200",true,"Usuário cadastrado com sucesso.");
                    }
                    else{
                        this.result = ResultMessage.applyResult("RG404",false,"Não foi possível salvar o usuário, tente novamente mais tarde.");
                    }
                }
            }
        }
        return SUCCESS;
    }
}
