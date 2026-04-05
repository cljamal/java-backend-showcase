package com.sultanov.present_project.core.abstractions;


public abstract class AbstractModelMapper<E extends AbstractModel, DTO extends AbstractDTO<E>>
{
    public abstract DTO toIndex(E entity);
    public abstract DTO toShow(E entity);
    public abstract DTO toStored(E entity);
}