package com.example.firestoretest;

public class Persons {


    String Name ;
    String Age;
    String PersonId;

    public Persons(String name, String age, String personId) {
        Name = name;
        Age = age;
        PersonId = personId;
    }

    public Persons() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String personId) {
        PersonId = personId;
    }
}
