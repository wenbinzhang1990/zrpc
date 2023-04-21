package com.wenbin.zrpc.client;

import java.util.List;

/**
 * The person service 8
 * @author wenbin
 * Date：2023/4/18
 */
public interface PersonService {

    List<Person> getPersonList();

    Person getPerson(String name);

    boolean savePerson(Person person);
}
