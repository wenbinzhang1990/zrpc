package com.wenbin.zrpc.test;

import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.client.PersonService;
import com.wenbin.zrpc.core.config.annotation.Reference;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * The person service invoker
 * @author wenbin
 * Date：2023/4/19
 */
@Component
public class PersonServiceInvoker {

    @Reference
    private PersonService personService;

    public List<Person> getPersonList() {
        return personService.getPersonList();
    }

    public Person getPerson(String name) {
        return personService.getPerson(name);
    }

    public boolean savePerson(Person person) {
        return personService.savePerson(person);
    }

}
