package service;

import model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseService <ID, TYPE extends BaseEntity<ID>> {

    TYPE saveOrUpdate (TYPE type);

    boolean deleteById (ID id);

    Optional<TYPE> findById (ID id);

    List<TYPE> findAll();
}
