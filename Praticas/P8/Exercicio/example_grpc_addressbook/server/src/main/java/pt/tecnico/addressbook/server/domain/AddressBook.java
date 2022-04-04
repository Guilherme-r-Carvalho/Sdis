package pt.tecnico.addressbook.server.domain;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import pt.tecnico.addressbook.grpc.AddressBookList;
import pt.tecnico.addressbook.grpc.PersonInfo;
import pt.tecnico.addressbook.grpc.PersonInfo.PhoneType;
import pt.tecnico.addressbook.server.domain.exception.DuplicatePersonInfoException;

public class AddressBook {

    private ConcurrentHashMap<String, Person> people = new ConcurrentHashMap<>();

    public AddressBook() {
    }

    public void addPerson(String name, String email, int phoneNumber, PhoneType type) throws DuplicatePersonInfoException {
        if(people.putIfAbsent(email, new Person(name, email, phoneNumber, type)) != null) {
            throw new DuplicatePersonInfoException(email);
        }
    }

    public AddressBookList proto() {
        return AddressBookList.newBuilder()
                .addAllPeople(people.values().stream().map(Person::proto).collect(Collectors.toList()))
                .build();
    }
    public PhoneType getType(String type){
        switch(type){
            case "MOBILE":
                return PersonInfo.PhoneType.MOBILE;
            case "HOME":
                return PersonInfo.PhoneType.HOME;
            case "WORK":
                return PersonInfo.PhoneType.WORK;
            case "EMERGENCY":
                return PersonInfo.PhoneType.EMERGENCY;
            default:
                return null;
        }
    }

    public ArrayList<PersonInfo> listAll(String PhoneType) throws IllegalArgumentException{
        ArrayList<PersonInfo> persons = new ArrayList<>();
        System.out.println(PhoneType);
        PhoneType type = getType(PhoneType.toUpperCase());
        people.forEach((k,v) -> {if(v.getType() == type){persons.add(v.proto());
        }});
        if(persons.isEmpty()){
            throw new IllegalArgumentException();
        }
        return persons;
    }
}
