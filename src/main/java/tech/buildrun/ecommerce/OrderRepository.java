package tech.buildrun.ecommerce;

public interface OrderRepository {
    void save(Order order);
    Order findById(int id);
}
