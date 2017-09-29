package com.csye6225.demo.repo;

import com.csye6225.demo.pojo.User;
import com.csye6225.demo.pojo.UserSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession,Long>{

    //@Query("select u.userName from User u, UserSession us where u.userId = us.userId " +"and us.sessionId =?1")
    @Query("select u.userName from User u where u = (select us.user from UserSession us where us.sessionId = ?1)")
    String findUserNameBySessionId(@Param("sessionId") String sessionId);
}
