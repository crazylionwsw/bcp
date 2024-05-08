package com.fuze.bcp.bd.service.impl;

import com.fuze.bcp.bd.domain.Orginfo;
import com.fuze.bcp.bd.repository.OrginfoRepository;
import com.fuze.bcp.bd.service.IOrgInfoService;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.service.impl.TreeDataServiceImpl;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/2.
 */
@Service
public class OrgInfoServiceImpl extends TreeDataServiceImpl<Orginfo, OrginfoRepository> implements IOrgInfoService {

    @Autowired
    OrginfoRepository orginfoRepository;

    @Override
    public List<Orginfo> getAllByParentIdIsNullAndDataStatus(Integer ids) {
        return orginfoRepository.findAllByParentIdIsNullAndDataStatus(ids);
    }

    @Override
    public List<Orginfo> getOrgByLeaderId(String leaderId) {
        return orginfoRepository.findAllByDataStatusAndLeaderId(DataStatus.SAVE, leaderId);
    }

    //  根据查询条件，查询部门信息
    public List<String> getOrgInfoIdsByOrgInfoId(String orginfoId){
        List<String> childOrgInfos = new ArrayList<String>();
        childOrgInfos.add(orginfoId);
        List<BasicDBObject> basicDBObjects = mongoTemplate.find(Query.query(Criteria.where("parentId").is(orginfoId)).addCriteria(Criteria.where("dataStatus").is(DataStatus.SAVE)), BasicDBObject.class,"bd_orginfo");
        if (basicDBObjects != null && basicDBObjects.size() > 0){
            for (BasicDBObject bb : basicDBObjects){
                childOrgInfos.addAll(this.getOrgInfoIdsByOrgInfoId(bb.get("_id").toString()));
            }
        }
        return childOrgInfos;
    }

    public  List<String> getOrgInfoIdsByEmployeeId(String employeeId,boolean includeChild){
        List<Orginfo> orginfos =  orginfoRepository.findAllByDataStatusAndLeaderId(DataStatus.SAVE, employeeId);
        List<String> childOrgInfos = new ArrayList<String>();
        for (Orginfo orginfo : orginfos) {
            childOrgInfos.addAll(getOrgInfoIdsByOrgInfoId(orginfo.getId()));
        }
        return childOrgInfos;
    }

    @Override
    public Orginfo findSuperOrg(Orginfo orginfo) {
        if (orginfo.getVirtual()){
            Orginfo parentOrginfo = this.getOne(orginfo.getParentId());
            orginfo = findSuperOrg(parentOrginfo);
        }
        return  orginfo;
    }

}
