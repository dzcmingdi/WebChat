package ssm.zmh.webchat.redis.jedisutils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import ssm.zmh.webchat.factory.SingletonGsonBuilderFactory;
import ssm.zmh.webchat.model.user.User;


@Component
public class JedisUserRegisterUtil implements JedisUtil {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private final Gson gson = SingletonGsonBuilderFactory.getGson();


    public JedisUserRegisterUtil() {

    }
    public void set(String key, Object object) {
        stringRedisTemplate.opsForValue().set(key,gson.toJson(object));
    }

    public Object get(String key, Type type) {

        return gson.fromJson(stringRedisTemplate.opsForValue().get(key), User.class);
    }

    public void sAdd(String key, String member) {
    }

    public Set<String> sMembers(String key) {
        return null;
    }
}