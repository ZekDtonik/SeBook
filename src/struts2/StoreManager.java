package struts2;

import Core.AuthRequired;
import Core.Defs;
import Core.ResultMessage;
import Persistence.Cnn.DAO;
import Persistence.Products;
import Persistence.PurchaseHistory;
import Persistence.PurchaseHistoryItem;
import Persistence.Users;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ParentPackage("mainPackage")

public class StoreManager extends ActionSupport implements AuthRequired, SessionAware {
    private Map <String, Object> session;
    private Map <String, Object> result = new HashMap<>();
    private HttpServletRequest request = ServletActionContext.getRequest();

    public Map<String, Object> getResult() {
        return result;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    @Action(value = "/system/store/add")
    public String addItem() {
        Map<String, String[]> map = this.request.getParameterMap();
        String[] itens = map.get("products[item]");
        String[] qnt = map.get("products[qnt]");
        String actionStatus = this.request.getParameter("action");

        if(itens == null) {
            this.result = ResultMessage.applyResult("ST00",false, "O carrinho está vazio, adicione um item ao carrinho para continuar.");
        }
        else {
            List<PurchaseHistoryItem> productsToAdd = new ArrayList<>();
            boolean statusCart = false;
            PurchaseHistory purchaseHistory = new PurchaseHistory();

            for (int i =0; i < itens.length; i++){
                String currentItem = itens[i];
                String currentQnt  = qnt[i];
                PurchaseHistoryItem purchaseHistoryItem = new PurchaseHistoryItem();
                Products currentProduct = (Products) DAO.getById(Products.class, Integer.parseInt(currentItem));

                if(Integer.parseInt(currentQnt) <= 0){
                    this.result = ResultMessage.applyResult("ST10",false, "Um dos itens adicionado ao carrinho possui uma quantidade inválida.");
                    break;
                }
                else if(currentProduct == null) {
                    this.result = ResultMessage.applyResult("ST11",false, "Ocorreu um erro no processamento do item selecionado. repita a operação.");
                    break;
                }
                else{
                    purchaseHistoryItem.setProduct(currentProduct);
                    purchaseHistoryItem.setQuantity(Integer.parseInt(currentQnt));

                    productsToAdd.add(purchaseHistoryItem);
                    statusCart = true;
                }
            }

            if(statusCart){
                Map<String, String> mapWhere = new HashMap<>();
                mapWhere.put("login",this.session.get(Defs.HANDLE_USER).toString());
                Users users = (Users) DAO.findOne("SELECT usr FROM Users usr WHERE login:login",mapWhere);
                purchaseHistory.setDate(new Date());
                purchaseHistory.setUsers(users);
                purchaseHistory.setProducts(productsToAdd);
                String messageVariable;
                if(actionStatus.equals("buy")) {
                    messageVariable = "Obrigado por comprar conosco. Voce pode visualizar os seus itens adquiridos no seu perfil. Volte Sempre!!";
                }
                else {
                    messageVariable = "Os itens foram salvos para compra futura. Observe que ao salvar o carrinho não garante o mesmo valor dos itens atuais caso sejam alterados.";
                }

                boolean result = DAO.insert(purchaseHistory);
                if(result) {
                    this.result = ResultMessage.applyResult("ST200",true,messageVariable);
                }
                else {
                    this.result = ResultMessage.applyResult("ST300",false,"Não foi possível processar a sua solicitação. Tente novamente");
                }
            }
        }

        return SUCCESS;
    }
}
