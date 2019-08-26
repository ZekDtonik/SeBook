package Core;

import java.util.HashMap;
import java.util.Map;

public class ResultMessage {
    private Map<String, Object> mapData = new HashMap<>();

    private ResultMessage(String code, String message, boolean status, String authentication){
        this.mapData.put("code",code);
        this.mapData.put("message",message);
        this.mapData.put("status",status);
        if(authentication != null)
            this.mapData.put("authentication",authentication);
    }
    private Map<String, Object> getResult () { return this.mapData ; }

    public static Map<String, Object> applyResult(boolean status, String message){
        ResultMessage resultMessage = new ResultMessage("GN00",message,status,null);
        return resultMessage.getResult();
    }
    public static Map<String, Object> applyResult(String code, boolean status, String message ){
        ResultMessage resultMessage = new ResultMessage(code, message, status,null);
        return resultMessage.getResult();
    }
    public static Map<String, Object> applyResult(String code, boolean status, String message, String auth){
        ResultMessage resultMessage = new ResultMessage(code, message, status,auth);
        return resultMessage.getResult();
    }
}
