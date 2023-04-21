import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.test.HelloServiceInvoker;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * description
 * @author wenbin
 * Date：2023/4/19
 */
public class HelloTest extends BaseTest {

    @Resource
    private HelloServiceInvoker helloServiceInvoker;



    @Test
    public void testHello() {
        String sayHello = helloServiceInvoker.sayHello();
        assert sayHello.equalsIgnoreCase("hello");
        sayHello = helloServiceInvoker.sayHello("wenbin");
        assert sayHello.equalsIgnoreCase("hello:wenbin");
        Person person = new Person("wenbin", 18, "男");
        sayHello = helloServiceInvoker.sayHello(person);
        assert sayHello.equalsIgnoreCase("hello:" + person);
    }
}
