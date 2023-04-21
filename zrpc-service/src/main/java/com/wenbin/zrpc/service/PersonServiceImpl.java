package com.wenbin.zrpc.service;

import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.client.PersonService;
import com.wenbin.zrpc.core.config.annotation.Service;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * description
 * @author wenbin
 * Date：2023/4/18
 */
@Service(value = PersonService.class)
@Component
public class PersonServiceImpl implements PersonService {

    @Override
    public List<Person> getPersonList() {
        return Person.createTestPersons();
    }

    @Override
    public Person getPerson(String name) {
        return Person.createTestPersons().stream().filter(person -> person.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public boolean savePerson(Person person) {
        return true;
    }
}
