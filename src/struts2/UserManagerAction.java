package struts2;

import Core.AuthRequired;
import Core.ResultMessage;
import Persistence.Recovery;
import Persistence.Users;
import Persistence.Cnn.DAO;
import Persistence.Persister;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.SessionAware;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParentPackage("mainPackage")

public class UserManagerAction extends ActionSupport implements AuthRequired, SessionAware, CookiesAware {
    private Map<String, Object> session;
    private Map<String, String> cookie;
    private HttpServletRequest request = ServletActionContext.getRequest();
    private Map<String,Object> result = new HashMap<>();
    private Pattern patternMail = Pattern.compile("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+).([a-z0-9A-Z]){2,4}(.[a-z0-9A-Z]{2,4})?");
    @Override
    public void setCookiesMap(Map<String, String> map) {
        this.cookie = map;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    public Map<String, Object> getResult() {
        return result;
    }

    @Override
    public String execute(){
        return SUCCESS;
    }

    @Action(value = "/system/users/edit")
    public String edt(){
        String login = this.request.getParameter("login");
        String name = this.request.getParameter("name");
        String email = this.request.getParameter("email");

        Matcher checkMail = this.patternMail.matcher(email);

        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        Persister objectToEdit = DAO.findOne("SELECT usr FROM Users AS usr WHERE name=:name",map);

        if(login == null || login.equals("")){
            this.result = ResultMessage.applyResult("US10",false,"A resolução para alteração do conteúdo não foi encontrado.");
        }
        else if (name !=null && name.equals("")){
            this.result = ResultMessage.applyResult("US15",false,"O nome pessoal não pode ficar em branco");
        }
        else if (email != null && !checkMail.find()){
            this.result = ResultMessage.applyResult("US15",false,"Endereço de Email inválido. Verifique as informações");
        }
        else if (objectToEdit == null){
            this.result = ResultMessage.applyResult("US20",false,"O usuário que está tentando editar não existe!");
        }
        else {
            ((Users) objectToEdit).setNome(name);
            ((Users) objectToEdit).setEmail(name);
            boolean result = DAO.update(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("US200",true,"O usuário foi atualizado com sucesso");
            }
            else {
                this.result = ResultMessage.applyResult("US300",false,"O usuário não pôde ser alterado.");
            }
        }
        return SUCCESS;
    }

    @Action(value = "/system/users/remove")
    public String del(){
        String login = this.request.getParameter("login");

        Map<String,String> map = new HashMap<>();
        map.put("login",login);
        Persister objectToEdit = DAO.findOne("SELECT usr FROM Users AS usr WHERE login=:login",map);

        if(login.equals("")) {
            this.result = ResultMessage.applyResult("US30", false, "A resolução para remoção do conteúdo não foi encontrado");
        }
        else if (objectToEdit == null){
            this.result = ResultMessage.applyResult("US40",false,"O usuário que está tentando remover não existe.");
        }
        else {

            boolean result = DAO.delete(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("US200",true,"O usuário foi removido com sucesso");
            }
            else {
                this.result = ResultMessage.applyResult("US300",false,"O usuário não pôde ser removido.");
            }
        }
        return SUCCESS;
    }
    @Action(value = "/system/users/reset/sendmail")
    public String resetSendMail() {
        String email = this.request.getParameter("email");
        Matcher checkMail = this.patternMail.matcher(email);

        Map<String,String> map = new HashMap<>();
        map.put("email",email);
        Persister objectToEdit = DAO.findOne("SELECT eml FROM Users AS eml WHERE email=:email",map);
        if(email == null || email.equals("")){
            this.result = ResultMessage.applyResult("US500",false, "O campo de email não pode ficar em branco.");
        }
        else if(!checkMail.find()){
            this.result = ResultMessage.applyResult("US510",false, "O email inserido não é válido.");
        }
        else if(objectToEdit == null){
            this.result = ResultMessage.applyResult("US520",false, "Não existe um usuário cadastrado com esse email.");
        }
        else {
            Random codeGenerate = new Random();
            StringBuilder code = new StringBuilder();
            StringBuilder valid = new StringBuilder();
            for(int i = 0; i < 5; i ++){
                codeGenerate.setSeed(System.currentTimeMillis());
                code.append(codeGenerate.nextInt());
            }
            valid.append(System.currentTimeMillis() + 30 * 60 * 1000);
            Recovery recovery = new Recovery();
            recovery.setUsers((Users) objectToEdit);
            recovery.setCode(code.toString());
            recovery.setValidity(valid.toString());

            boolean result = DAO.insert(recovery,true);
            if (result){
                boolean mockEmailSendResult = true;
                if(mockEmailSendResult){
                    this.result = ResultMessage.applyResult("US201",true, "Uma nova requisição foi realizada com sucesso.");
                    DAO.getEntityManager().getTransaction().commit();
                }
                else {
                    this.result = ResultMessage.applyResult("US301",false, "A sua requisição de redefinição de senha não pode ser processada.");
                    DAO.getEntityManager().getTransaction().rollback();
                }
            }
        }
        return SUCCESS;
    }
    @Action(value = "/system/users/reset/password")
    public String resetPwd(){
        String code = request.getParameter("code");
        String newPassword = request.getParameter("newpassword");
        String rePassword = request.getParameter("repassword");

        if(code == null || code.equals("") || newPassword == null || newPassword.equals("") || rePassword == null || rePassword.equals("")){
            this.result = ResultMessage.applyResult("US600",false,"Todos os campos são obrigatórios");
        }
        else {
            Map<String, String> map = new HashMap<>();
            map.put("cd",code);
            Recovery recoveryData = (Recovery) DAO.findOne("SELECT rcv FROM Recovery as rcv WHERE code=:cd", map);
            if(recoveryData == null){
                this.result = ResultMessage.applyResult("US610",false,"Código inválido tente novamente");
            }
            else {
                long expireTime = Long.parseLong(recoveryData.getValidity());
                if(expireTime < System.currentTimeMillis()){
                    this.result = ResultMessage.applyResult("US620",false,"A tempo de validade do código expirou, repita a operação.");
                }
                else if(newPassword.length() < 6) {
                    this.result = ResultMessage.applyResult("US630",false,"A senha deve ser maior que 5 caracteres.");
                }
                else if(!rePassword.equals(newPassword)) {
                    this.result = ResultMessage.applyResult("US640",false,"As senhas não combinam.");
                }
                else {
                    Users user = recoveryData.getUsers();
                    user.setPassword(newPassword);

                    boolean result = DAO.update(user);
                    if(result) {
                        this.result = ResultMessage.applyResult("US203",true,"Senha alterada com sucesso.");
                    }
                    else {
                        this.result = ResultMessage.applyResult("US302",false,"Não foi possivel alterar a senha no momento. Tente novamente.");

                    }
                }
            }
        }
        return SUCCESS;
    }
}
