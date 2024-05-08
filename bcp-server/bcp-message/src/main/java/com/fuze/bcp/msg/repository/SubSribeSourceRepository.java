package com.fuze.bcp.msg.repository;

import com.fuze.bcp.msg.domain.SubSribeSource;
import com.fuze.bcp.repository.BaseRepository;

import java.util.List;

/**
 * Created by CJ on 2017/9/14.
 */
public interface SubSribeSourceRepository extends BaseRepository<SubSribeSource, String> {
    List<SubSribeSource> findAllByDataStatusAndGroupName(Integer dataStatus, String groupName);
}
