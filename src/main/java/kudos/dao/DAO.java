package kudos.dao;

import kudos.model.User;

/**
 * Created by chc on 15.8.5.
 */
public interface DAO<T> {
    T create(T user);

    T update(T user);

    void remove(String id);
}
