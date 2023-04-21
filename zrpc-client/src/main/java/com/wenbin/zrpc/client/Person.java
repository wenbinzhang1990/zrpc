package com.wenbin.zrpc.client;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The person
 * @author wenbin
 * Date：2023/4/18
 */
@Getter
@Setter
@AllArgsConstructor
public class Person {

    private String name;

    private Integer age;

    private String sex;

    public String toString() {
        return String.format("%s,%s,%s", name, age, sex);
    }

    public static Person createTestPerson() {
        return new Person("1", 1, "1");
    }

    public static List<Person> createTestPersons() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            personList.add(new Person(String.valueOf(i), i, String.valueOf(i)));
        }

        return personList;
    }


}
