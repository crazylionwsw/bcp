package com.fuze.bcp.bd.repository;

import com.fuze.bcp.bd.domain.Orginfo;
import com.fuze.bcp.repository.TreeDataRepository;

import java.util.List;

/**
 * Created by dell on 2016/10/20.
 */
public interface OrginfoRepository extends TreeDataRepository<Orginfo,String> {

    List<Orginfo> findAllByParentIdIsNullAndDataStatus(Integer ids);

    List<Orginfo> findAllByDataStatusAndLeaderId(Integer ds, String leaderId);
}
