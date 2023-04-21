import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.test.PersonServiceInvoker;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * description
 * @author wenbin
 * Date：2023/4/19
 */
public class PersonTest extends BaseTest {

    @Resource
    private PersonServiceInvoker personServiceInvoker;

    @Test
    public void testPerson() {
        List<Person> personList = personServiceInvoker.getPersonList();
        List<Person> expectedPersonList = Person.createTestPersons();
        assert personList.size() == expectedPersonList.size();
        for (int i = 0; i < personList.size(); i++) {
            assert expectedPersonList.get(i).toString().equalsIgnoreCase(personList.get(i).toString());
        }

        Person person = personServiceInvoker.getPerson("wenbin");
        assert person == null;
        person = personServiceInvoker.getPerson("0");
        assert person != null && person.toString().equalsIgnoreCase("0,0,0");
        assert personServiceInvoker.savePerson(new Person("wenbin", 18, "男"));
    }
}
