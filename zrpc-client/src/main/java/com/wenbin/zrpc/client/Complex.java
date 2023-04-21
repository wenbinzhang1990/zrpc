package com.wenbin.zrpc.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * description
 * @author wenbin
 * Date：2023/4/21
 */
@Data
public class Complex {

    private List<Person> personList;


    private Map<Integer, Person> personMap;

    private Person person;


    public static Complex createComplex() {
        Complex complex = new Complex();
        complex.setPerson(Person.createTestPerson());
        complex.setPersonList(Person.createTestPersons());
        List<Person> persons = Person.createTestPersons();
        complex.setPersonMap(new HashMap<>());
        for (int i = 0; i < persons.size(); i++) {
            complex.getPersonMap().put(i, persons.get(i));
        }

        return complex;
    }
}
