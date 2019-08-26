package Interceptors;

import Core.Defs;
import struts2.LoginAction;
import Core.AuthRequired;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Map;

public class Guard extends AbstractInterceptor {
    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        System.out.println("Intercept Guard Actived! On "+ actionInvocation.getClass().getName());
        Map<String,Object> session = actionInvocation.getInvocationContext().getSession();

        //Se existir uma sessão no contexto da action, invoca a ação recebida
        if(session.get(Defs.HANDLE_USER) != null){
            return actionInvocation.invoke();
        }
        //A ação disparada pela requisição propriamente dita
        //Ex: Se eu chamei meusite.com/system/UserProfileData
        //e a action foi ProfileAction contendo @Action
        //então este é o objeto recuperado
        Object action = actionInvocation.getAction();

        //Estou utilizando uma interface chamada de AuthRequired para identificar as ACTIONS que
        //necessitam de autenticação para ser executada, dessa forma,
        //Verifico se a minha ação invocada possui a interface de controle
        if(!(action instanceof AuthRequired)) {
            return actionInvocation.invoke();
        }
        //se essa solicitação exigir login e a ação atual não for a ação de login, redirecione o usuário
        if(!(action instanceof LoginAction)){
            return Action.LOGIN;
        }

        return actionInvocation.invoke();
    }
}
