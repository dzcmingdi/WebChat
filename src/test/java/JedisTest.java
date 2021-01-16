
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ssm.zmh.webchat.model.user.User;
import ssm.zmh.webchat.redis.jedisutils.JedisUserRegisterUtil;
import ssm.zmh.webchat.utils.file.save.FileUtil;


//@SpringJUnitConfig(locations = "file:D:\\IdeaProjects\\WebChatServer\\src\\test\\applicationContext.xml")
public class JedisTest {

//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//
//
//    @Test
//    public void test01(){
//        System.out.println(stringRedisTemplate.opsForValue().get("test"));
//
//    }

    @Test
    public void test02(){
        System.out.println(FileUtil.createRoomDir("/test"));
    }
}
