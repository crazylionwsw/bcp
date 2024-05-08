package com.fuze.wechat.repository;

import com.fuze.wechat.domain.LoanQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanQueryRepository extends MongoRepository<LoanQuery, String> {

    LoanQuery findOneById(String id);
}
