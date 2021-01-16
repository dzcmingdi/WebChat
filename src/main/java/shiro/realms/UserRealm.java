package shiro.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ssm.zmh.webchat.dao.impl.user.RoleService;
import ssm.zmh.webchat.dao.impl.user.UserService;
import ssm.zmh.webchat.model.user.Role;
import ssm.zmh.webchat.model.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User)getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<>();
        List<Role> roleList = roleService.selectAllRoleByMail(user.getMail());
        for (Role role:
             roleList) {
            roleSet.add(role.getRoleName());
        }
        info.setRoles(roleSet);


        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String mail = (String) authenticationToken.getPrincipal();
        User user = userService.getOneByMail(mail);
        if (user == null){
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
    }
}
