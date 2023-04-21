import com.wenbin.zrpc.client.Complex;
import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.test.ComplexServiceInvoker;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * description
 * @author wenbin
 * Date：2023/4/21
 */
public class ComplexTest extends BaseTest {

    @Resource
    private ComplexServiceInvoker complexServiceInvoker;

    @Test
    public void test() {
        Complex complex = complexServiceInvoker.getComplex();
        assert complex != null;
        assert complex.getPerson() != null;
        assert complex.getPerson().toString().equalsIgnoreCase("1,1,1");
        assert complex.getPersonList() != null;
        List<Person> expectedPersonList = Person.createTestPersons();
        assert complex.getPersonList().size() == expectedPersonList.size();
        for (int i = 0; i < complex.getPersonList().size(); i++) {
            assert expectedPersonList.get(i).toString().equalsIgnoreCase(complex.getPersonList().get(i).toString());
        }

        Map<Integer, Person> personMap = complex.getPersonMap();
        assert personMap != null;
        assert personMap.size() == expectedPersonList.size();
        for (int i = 0; i < expectedPersonList.size(); i++) {
            assert personMap.get(i).toString().equalsIgnoreCase(expectedPersonList.get(i).toString());
        }
    }
}
