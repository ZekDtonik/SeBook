package struts2;

import Core.AuthRequired;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.interceptor.SessionAware;

import java.util.HashMap;
import java.util.Map;

@ParentPackage(value = "mainPackage")
public class MainAction extends ActionSupport implements AuthRequired, SessionAware  {
    private Map<String,Object> session = new HashMap<>();

    @Action(value = "/home",results = {
            @Result(type = "json")
    })
    public String execute(){
        return "SUCCESS";
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
