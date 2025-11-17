package core.models.storage;

import core.models.Person;
import core.models.Author;
import core.models.Manager;
import core.models.Narrator;
import java.util.ArrayList;

public class PersonStorage {

    private static PersonStorage instance;

    private ArrayList<Person> persons;

    private PersonStorage() {
        this.persons = new ArrayList<>();
    }

    public static PersonStorage getInstance() {
        if (instance == null) {
            instance = new PersonStorage();
        }
        return instance;
    }

    public boolean addPerson(Person person) {
        for (Person p : this.persons) {
            if (p.getId() == person.getId()) {
                return false;
            }
        }
        this.persons.add(person);
        return true;
    }

    public Person getPerson(long id) {
        for (Person person : this.persons) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    public ArrayList<Person> getAllPersons() {
        return new ArrayList<>(this.persons);
    }

    public ArrayList<Author> getAuthors() {
        ArrayList<Author> authors = new ArrayList<>();
        for (Person person : this.persons) {
            if (person instanceof Author) {
                authors.add((Author) person);
            }
        }
        return authors;
    }

    public ArrayList<Manager> getManagers() {
        ArrayList<Manager> managers = new ArrayList<>();
        for (Person person : this.persons) {
            if (person instanceof Manager) {
                managers.add((Manager) person);
            }
        }
        return managers;
    }

    public ArrayList<Narrator> getNarrators() {
        ArrayList<Narrator> narrators = new ArrayList<>();
        for (Person person : this.persons) {
            if (person instanceof Narrator) {
                narrators.add((Narrator) person);
            }
        }
        return narrators;
    }

    public boolean deletePerson(long id) {
        for (Person person : this.persons) {
            if (person.getId() == id) {
                this.persons.remove(person);
                return true;
            }
        }
        return false;
    }

}
