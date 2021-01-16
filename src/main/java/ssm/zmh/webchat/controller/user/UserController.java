package ssm.zmh.webchat.controller.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssm.zmh.webchat.dao.impl.user.ContactsService;
import ssm.zmh.webchat.model.request.ContactUpdate;
import ssm.zmh.webchat.model.user.User;

import java.security.Principal;

@RestController
public class UserController {
    @Autowired
    ContactsService contactsService;


    @RequiresUser
    @RequestMapping(value = "/api/v1/user/info/update", method = RequestMethod.POST)
    public String userInfoUpdate(ContactUpdate contactUpdate) {
        User user = (User) (SecurityUtils.getSubject().getPrincipal());

        contactUpdate.setUserUUID(user.getUserUUID());
        try {
            contactsService.updateUserInfo(contactUpdate);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }


    }

    @RequestMapping(
            value = {"/user/private/info/load"},
            method = {RequestMethod.GET}
    )
    public @ResponseBody
    User userInfoLoad(@RequestParam("userUUID") String userUUID) {
        return this.contactsService.loadUserInfo(userUUID);
    }
}
