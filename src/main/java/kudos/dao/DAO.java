package kudos.dao;

import com.google.common.base.Optional;

/**
 * Created by chc on 15.8.5.
 */
public interface DAO<T> {
    Optional<T> get (String id);

    Optional<T> create(T obj);

    Optional<T> update(T obj);

    void remove(String id);
}
