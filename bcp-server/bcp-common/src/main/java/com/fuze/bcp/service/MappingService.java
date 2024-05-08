package com.fuze.bcp.service;

import com.fuze.bcp.bean.DataPageBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class MappingService {

    @Autowired
    private DozerBeanMapper mapper;

    public <T> T map(Object source, Class<T> target) {
        return mapper.map(source, target);
    }

    public <T> void map(Object source, T target) {
        mapper.map(source, target);
    }

    public <T,O> List<O> map(List<T> objects, Class<O> target) {
        return (List<O>) internalMap(objects, target, (List<O>) Lists.newArrayList());
    }

    public <T,O> List<O> map(List<T> objects, Class<O> target, List<O> destination) {
        return (List<O>) internalMap(objects, target, destination);
    }

    public <T,O> Set<O> map(Set<T> objects, Class<O> target, Set<O> destination) {
        return (Set<O>) internalMap(objects, target, destination);
    }

    public <T,O> Set<O> map(Set<T> objects, Class<O> target) {
        return map(objects, target, (Set<O>) Sets.newHashSet());
    }

    public <T,O> DataPageBean<O> map(Page<T> source, Class<O> target) {
        DataPageBean<O> destination = new DataPageBean<O>();
        destination.setPageSize(source.getSize());
        destination.setTotalCount(source.getTotalElements());
        destination.setTotalPages(source.getTotalPages());
        destination.setCurrentPage(source.getNumber());
        for (T t : source.getContent()) {
            destination.getResult().add(mapper.map(t, target));
        }
        return destination;
    }

    private <T, O> Collection<O> internalMap(Collection<T> objects, Class<O> target, Collection<O> destination) {
        for (T t : objects) {
            destination.add(mapper.map(t, target));
        }
        return destination;
    }

}
