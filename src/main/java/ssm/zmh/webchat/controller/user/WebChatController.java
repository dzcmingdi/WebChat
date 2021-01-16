//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.controller.user;

import com.google.gson.JsonObject;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.ModelAndView;
import ssm.zmh.webchat.dao.impl.user.ContactsService;
import ssm.zmh.webchat.dao.impl.user.UserService;
import ssm.zmh.webchat.exception.user.AddUserException;
import ssm.zmh.webchat.factory.SingletonGsonBuilderFactory;
import ssm.zmh.webchat.factory.SingletonJsonObjectBuilderFactory;
import ssm.zmh.webchat.factory.SingletonUserBuilderFactory;
import ssm.zmh.webchat.model.message.MessageUser;
import ssm.zmh.webchat.model.request.ContactDeleteInBound;
import ssm.zmh.webchat.model.request.ContactQueryBound;
import ssm.zmh.webchat.model.request.ContactSelectBound;
import ssm.zmh.webchat.model.request.MessageUpload;
import ssm.zmh.webchat.model.response.ContactQueryOutBound;
import ssm.zmh.webchat.model.response.GroupQueryOutBound;
import ssm.zmh.webchat.model.response.UnAckMessage;
import ssm.zmh.webchat.model.user.Group;
import ssm.zmh.webchat.model.user.Setting;
import ssm.zmh.webchat.model.user.User;
import ssm.zmh.webchat.service.MessageService;
@Controller
public class WebChatController {
    @Autowired
    UserService userService;
    @Autowired
    ContactsService contactsService;
    @Autowired
    MessageService messageService;

    public WebChatController() {
    }

    @RequestMapping(
            value = {"/user/login"},
            method = {RequestMethod.GET}
    )
    public ModelAndView userLoginView() {
        return new ModelAndView("user/login");
    }

    @RequestMapping(
            value = {"/user/login"},
            method = {RequestMethod.POST}
    )
    public @ResponseBody JsonObject userLogin(@RequestParam("mail") String mail, @RequestParam("password") String password, @RequestParam(value = "rememberMe",required = false) boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(mail, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        JsonObject jsonObject = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();

        try {
            try {
                subject.login(token);
                User user = ((User)subject.getPrincipal()).clone();
                user.setUserId(0);
                user.setPassword((String)null);
                jsonObject.addProperty("CODE", "1");
                jsonObject.addProperty("MSG", "SUCCESS");
                jsonObject.add("user", SingletonGsonBuilderFactory.getGson().toJsonTree(user));
                jsonObject.add("setting",SingletonGsonBuilderFactory.getGson().toJsonTree(userService.getUserSetting(user.getUserUUID())));
            } catch (UnknownAccountException e) {
                jsonObject.addProperty("CODE", "0");
                jsonObject.addProperty("MSG", "FAIL,UnKnown Account");
                e.printStackTrace();
            } catch (IncorrectCredentialsException e) {
                jsonObject.addProperty("CODE", "0");
                jsonObject.addProperty("MSG", "FAIL,Wrong Account");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.addProperty("CODE", "0");
                jsonObject.addProperty("MSG", "NETWORK ERROR");
            }

            return jsonObject;
        } finally {
            ;
        }
    }

    @RequestMapping(
            value = {"/user/register"},
            method = {RequestMethod.GET}
    )
    public ModelAndView userRegisterView() {
        return new ModelAndView("user/register");
    }

    @RequestMapping(
            value = {"/user/register/submit"},
            method = {RequestMethod.POST}
    )

    public @ResponseBody JsonObject userRegister(@RequestParam("mail") String mail, @RequestParam("userName") String userName, @RequestParam("password") String password) {
        User user = SingletonUserBuilderFactory.getUserBuilder().build(mail, userName, password);
        JsonObject res = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();

        try {
            this.userService.setUserToRedis(user);
            res.addProperty("CODE", "1");
        } catch (MessagingException | SqlSessionException var7) {
            res.addProperty("CODE", "0");
        }

        return res;
    }

    @RequestMapping(
            value = {"/user/register/confirm"},
            method = {RequestMethod.GET}
    )
    public ModelAndView userRegisterConfirmView(@RequestParam("mail") String mail, @RequestParam("token") String token) {
        User user = this.userService.getUserFromRedis(mail);
        if (user == null) {
            return new ModelAndView("redirect:/404");
        } else if (user.getUserToken().equals(token)) {
            try {
                this.userService.insertUser(user);
            } catch (AddUserException e) {
                e.printStackTrace();
                return new ModelAndView("redirect:/404");
            }

            return new ModelAndView("user/registerSuccess");
        } else {
            return new ModelAndView("redirect:/404");
        }
    }

    @RequestMapping({"/user/logout"})

    public @ResponseBody JsonObject userLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CODE", "1");
        return jsonObject;
    }

    @RequestMapping(
            value = {"/user/friend/list"},
            method = {RequestMethod.GET}
    )

    public @ResponseBody List<User> getUserFriendsList() {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        return user == null ? null : this.contactsService.getOnesFriendsList(user.getUserId());
    }

    @RequestMapping(
            value = {"/user/group/list"},
            method = {RequestMethod.GET}
    )

    public @ResponseBody List<GroupQueryOutBound> getUserGroupsList() {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        return user == null ? null : this.contactsService.getSelfGroupList(user.getUserUUID());
    }

    @RequestMapping(
            value = {"/user/message/reload"},
            method = {RequestMethod.GET}
    )
    public @ResponseBody JsonObject userMessageReloadView(@RequestParam("loadMessages") int loadMessages, @RequestParam("loadMessageList") int loadMessageList) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        return this.messageService.loadMessagesFromMongo(user.getUserUUID(), loadMessages, loadMessageList);
    }

    @RequestMapping(
            value = {"/user/chat/message/load_unaccepted"},
            method = {RequestMethod.GET}
    )
    public @ResponseBody UnAckMessage userChatMessagesLoadUnAcceptedView(@RequestParam("userUUID") String userUUID) {

        return this.messageService.loadUnAcceptedPrivateMessage(userUUID);
    }

    @RequestMapping(
            value = {"/api/v1/user/chat/message/upload"},
            method = {RequestMethod.POST},
            consumes = "text/plain;charset=utf-8"
    )

    public @ResponseBody String userChatMessageUploadView(HttpEntity<String> stringHttpEntity) {
        MessageUser messageUser = SingletonGsonBuilderFactory.getGson().fromJson(stringHttpEntity.getBody(),MessageUser.class);
        this.messageService.uploadMessageListToMongo(messageUser);
        return "ok";
    }

    @RequestMapping(
            value = {"/user/contacts/get"},
            method = {RequestMethod.GET}
    )

    public @ResponseBody ContactQueryOutBound userContactsGet(ContactQueryBound contactQueryBound) {
        return this.contactsService.getContactsByTargetNumber(contactQueryBound.getTargetNumber());
    }

    @RequestMapping(
            value = {"/user/contact/add"},
            method = {RequestMethod.POST}
    )

    public @ResponseBody boolean userContactAdd(@RequestBody ContactSelectBound contactSelectBound) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        boolean addRes = this.contactsService.addOnesContact(user, contactSelectBound);
        return addRes;
    }






    @RequestMapping(
            value = {"/user/contact/delete"},
            method = {RequestMethod.POST}
    )

    public  @ResponseBody boolean contactDelete(@RequestBody ContactDeleteInBound contactDeleteInBound) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();

        try {
            this.contactsService.contactDelete(user, contactDeleteInBound);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @RequestMapping(value = "/api/v1/user/setting/get", method = RequestMethod.GET)
    public @ResponseBody
    Setting userSettingGet(@RequestParam(value = "userUUID") String userUUID){
        try {
            return userService.getUserSetting(userUUID);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresUser
    @RequestMapping(value = "/api/v1/user/setting/update",method = RequestMethod.POST)
    public @ResponseBody String userSettingUpdate(Setting setting){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        setting.setUserUUID(user.getUserUUID());

        try {
            userService.updateUserSetting(setting);

            return "ok";
        }
        catch (Exception e){

            return "error";
        }

    }
}
