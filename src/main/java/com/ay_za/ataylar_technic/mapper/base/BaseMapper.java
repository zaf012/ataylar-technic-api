package com.ay_za.ataylar_technic.mapper.base;

import java.util.List;

public interface BaseMapper<E, D> {

  E convertToEntity(D dto);

  D convertToDTO(E entity);

  List<E> convertAllToEntity(List<D> dto);

  List<D> convertAllToDTO(List<E> entity);
}
