package struts2;

import Core.AuthRequired;
import Core.Defs;
import Persistence.Cnn.DAO;
import Persistence.PurchaseHistory;
import Persistence.Users;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParentPackage(value = "mainPackage")
public class ListPurchaseHistory extends ActionSupport implements SessionAware, AuthRequired {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> session;
    private List<PurchaseHistory> purchaseHistorys = new ArrayList<>();
    private String actionValue;
    private Users user;
    public List<PurchaseHistory> getListStore() {
        return purchaseHistorys;
    }
    public void setPurchaseHistory(List<PurchaseHistory> listPrd) {
        this.purchaseHistorys = listPrd;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    
    public ListPurchaseHistory() {


        String[] uri = ServletActionContext.getRequest().getRequestURI().split("/");
        this.actionValue = uri[(uri.length -1)];
    }
    private void buildUserFromSession(){
        Map<String, String> paramUser = new HashMap<>();
        paramUser.put("lg",(String) this.session.get(Defs.HANDLE_USER));
        this.user = (Users) DAO.findOne("SELECT usr FROM Users AS usr WHERE login=:lg",paramUser);
    }
    @Action(value = "/system/store")
    public String findAll(){
        this.buildUserFromSession();


        Map<String, String> params = new HashMap<>();
        params.put("user",this.user.getId().toString());
        List<PurchaseHistory> purchaseHistorys = DAO.findAll("SELECT purchase FROM PurchaseHistory as purchase WHERE user=:user",params);
        this.setPurchaseHistory(purchaseHistorys);

        return SUCCESS;
    }
    @Action(value = "/system/store/id/*")
    public String findOne(){
        this.buildUserFromSession();
        Pattern pattern = Pattern.compile("([0-9]+)");
        Matcher matcher = pattern.matcher(this.actionValue);
        if(matcher.matches()){


            Map<String, String> params = new HashMap<>();
            params.put("user",this.user.getId().toString());
            params.put("id",this.actionValue);
            PurchaseHistory purchaseHistory = (PurchaseHistory) DAO.findOne("SELECT purchase FROM PurchaseHistory AS purchase WHERE user=:user AND id=:id",params);
            this.purchaseHistorys.add(purchaseHistory);

        }
        return SUCCESS;
    }

}
