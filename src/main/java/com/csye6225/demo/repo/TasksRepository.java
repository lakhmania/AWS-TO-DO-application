package com.csye6225.demo.repo;

import com.csye6225.demo.pojo.Tasks;
import com.csye6225.demo.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends CrudRepository<Tasks,Long> {

    Tasks findTasksByTaskId(String taskId);

    List<Tasks> findTasksByUser(User user);


}