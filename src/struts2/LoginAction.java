package struts2;

import Core.AuthRequired;
import Core.Defs;
import Core.ResultMessage;
import Persistence.Cnn.DAO;
import Persistence.Users;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage(value = "mainPackage")
@Action(value = "/system/login",results = {@Result(type = "json")})
public class LoginAction extends ActionSupport implements SessionAware, AuthRequired, CookiesAware {

    private static final long serialVersionUID = 1L;

    private HttpServletRequest req = ServletActionContext.getRequest();
    private HttpServletResponse res = ServletActionContext.getResponse();

    private Map<String, Object> result = new HashMap<>();
    public Map<String, Object> getResult() { return result; }
    public void setResult(Map<String, Object> result) { this.result = result; }

    private Map<String, String> cookie;
    @Override
    public void setCookiesMap(Map<String, String> map) { this.cookie = map; }

    private Map<String, Object> session;
    @Override
    public void setSession(Map<String, Object> map) { this.session = map; }

    public String execute() throws Exception {
        if(req.getMethod().equals("POST")){

            res.addHeader("Access-Control-Allow-Origin","*");
            Enumeration params = req.getParameterNames();
            HttpSession sess = ServletActionContext.getRequest().getSession(true);
            //Caso a requisição recebida seja diretamente para a ação de login
            //verifica a existencia de sessão retornando informações de acesso
            if(this.session.get(Defs.HANDLE_USER) != null){
                this.result = ResultMessage.applyResult("AU200",true,"Usuário já está logado.","granted");

            }
            else if(!params.hasMoreElements()){
                this.result = ResultMessage.applyResult("AU00",false,"Nenhum valor de credencial foi fornecido!","denied");

            }
            else{
                Map<String,String> param = new HashMap<>();
                param.put("login",req.getParameter("login"));
                param.put("pass",req.getParameter("password"));
                List users = DAO.findAll("SELECT usr FROM Users AS usr WHERE usr.login =:login AND usr.passwd =:pass",param);
                if(users.isEmpty()){
                    this.result = ResultMessage.applyResult("AU01",false,"Login e/ou senha estão incorretos.","denied");
                }
                else{
                    this.session.put(Defs.HANDLE_USER,req.getParameter("login"));
                    this.session.put(Defs.HANDLE_USER_LEVEL, ((Users) users.get(0)).getLevel());

                    Cookie sessionCookie = new Cookie(Defs.HANDLE_USER,((Users) users.get(0)).getNome());
                    sessionCookie.setMaxAge(60 * 30);
                    res.addCookie(sessionCookie);

                    this.result = ResultMessage.applyResult("AU200",true,"Login realizado com sucesso!","grated");

                }
            }
        }
        else{
            this.result = ResultMessage.applyResult("AU100",false,"É necessário estar logado para realizar esse tipo de operação.","denied");

        }
        return SUCCESS;
    }

    @Action(value = "/system/logout", results = {@Result(type = "json")})
    public String logout(){
        if(this.session.containsKey(Defs.HANDLE_USER)){
            this.session.remove(Defs.HANDLE_USER);
            this.session.remove("data");
            Cookie[] cookies = req.getCookies();
            if(cookies!=null){
                for (int i = 0; i < cookies.length; i++) {
                    cookies[i].setValue("");
                    cookies[i].setMaxAge(0);
                    res.addCookie(cookies[i]);
                }
            }
        }
        this.result = ResultMessage.applyResult("AU300",true,"Usuário desconectado com sucesso!","released");
        return SUCCESS;
    }

}
