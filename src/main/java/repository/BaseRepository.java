package repository;

import model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<ID, TYPE extends BaseEntity<ID>> {
    TYPE saveOrUpdate(TYPE type);

    boolean deleteById(ID id);

    Optional<TYPE> findById(ID id);

    List<TYPE> findAll();

    Class<TYPE> getEntityClass();
}
