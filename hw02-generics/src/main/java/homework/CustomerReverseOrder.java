package homework;


import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public class CustomerReverseOrder {

    private final Queue<Customer> store = Collections.asLifoQueue(new ArrayDeque<>());
    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        store.add(customer);
    }

    public Customer take() {
        return store.poll();
    }
}
