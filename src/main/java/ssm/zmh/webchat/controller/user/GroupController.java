package ssm.zmh.webchat.controller.user;


import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssm.zmh.webchat.builder.gson.JsonObjectBuilder;
import ssm.zmh.webchat.dao.impl.user.ContactsService;
import ssm.zmh.webchat.exception.group.AddGroupException;
import ssm.zmh.webchat.exception.group.AddGroupMemberException;
import ssm.zmh.webchat.factory.SingletonJsonObjectBuilderFactory;
import ssm.zmh.webchat.listener.user.GroupListener;
import ssm.zmh.webchat.model.request.ContactUpdate;
import ssm.zmh.webchat.model.request.GroupAddInBound;
import ssm.zmh.webchat.model.user.Group;

@RestController
public class GroupController {

    @Autowired
    ContactsService contactsService;
    @RequestMapping(
            value = {"/user/group/info/load"},
            method = {RequestMethod.GET}
    )

    public @ResponseBody
    Group groupInfoLoad(@RequestParam("groupUUID") String groupUUID) {
        return this.contactsService.loadGroupInfo(groupUUID);
    }

    @RequestMapping(value = "/api/v1/group/info/update", method = RequestMethod.POST)
    public String groupInfoUpdate(ContactUpdate contactUpdate){
        try{
            contactsService.updateGroupInfo(contactUpdate);
            return "ok";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }



    @RequestMapping(value = "/api/v1/group/add", method = RequestMethod.POST)
    public @ResponseBody JsonObject groupAdd(GroupAddInBound groupAddInBound){
        JsonObject jsonObject = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();

        try {
            Group group = contactsService.addGroup(groupAddInBound);
            jsonObject.addProperty("message","ok");
            jsonObject.addProperty("groupUUID",group.getGroupUUID());
            jsonObject.addProperty("groupTargetNumber",group.getGroupTargetNumber());

        } catch (AddGroupException e) {
            e.printStackTrace();
            jsonObject.addProperty("message","error");
        }

        return jsonObject;
    }
}
