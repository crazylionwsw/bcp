package com.fuze.wechat.repository;

import com.fuze.wechat.domain.PublicUser;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ZQW on 2018/5/17.
 */
public interface PublicUserRepository extends MongoRepository<PublicUser, String> {

    PublicUser findOneByOpenid(String openId);

    PublicUser findOneByCell(String cell);
}
