package kudos.dao;

/**
 * Created by chc on 15.8.5.
 */
public interface DAO<T> {
    T get (String email);

    T create(T user);

    T update(T user);

    void remove(String email);
}
