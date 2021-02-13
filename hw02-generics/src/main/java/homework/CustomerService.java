package homework;


import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> store = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> entry = store.firstEntry();
        return Map.entry(entry.getKey().copy(), entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> customerEntry = store.higherEntry(customer);
        if (customerEntry == null) {
            return null;
        }
        return Map.entry(customerEntry.getKey().copy(), customerEntry.getValue());
    }

    public void add(Customer customer, String data) {
        store.put(customer, data);
    }
}
