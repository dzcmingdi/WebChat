package ssm.zmh.webchat.model.response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ssm.zmh.webchat.factory.SingletonGsonBuilderFactory;

public class Result {
    private final JsonObject jsonObject;
    public Result(){
        jsonObject = new JsonObject();
    }
    public Result add(String property, Object object2){
        jsonObject.add(property, SingletonGsonBuilderFactory.getGson().toJsonTree(object2));
        return this;
    }
    public JsonObject build(){return this.jsonObject;}

}
