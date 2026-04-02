package com.sultanov.present_project.core.abstractions;


public abstract class AbstractModelMapper<E extends AbstractModel, DTO extends AbstractDTO<E>>
{
    protected abstract DTO toIndex(E entity);
    protected abstract DTO toShow(E entity);
    protected abstract DTO toStored(E entity);
}