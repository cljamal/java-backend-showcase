package com.sultanov.present_project.core.abstractions;


public abstract class AbstractController <
        E extends AbstractModel,
        Repository extends AbstractRepository<E>,
        Mapper extends AbstractModelMapper<E, AbstractDTO<E>>
    > {

    protected Repository repository;
    protected Mapper mapper;

    public AbstractController(Repository repository, Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
}