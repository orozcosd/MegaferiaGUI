package core.models.storage;

import core.models.Person;
import core.models.Author;
import core.models.Manager;
import core.models.Narrator;
import java.util.ArrayList;

public interface IPersonStorage {

    boolean addPerson(Person person);

    Person getPerson(long id);

    ArrayList<Person> getAllPersons();

    ArrayList<Author> getAuthors();

    ArrayList<Manager> getManagers();

    ArrayList<Narrator> getNarrators();

    boolean deletePerson(long id);

}
