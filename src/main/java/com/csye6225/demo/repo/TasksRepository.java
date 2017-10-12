package com.csye6225.demo.repo;

import com.csye6225.demo.pojo.Tasks;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TasksRepository extends CrudRepository<Tasks, String>  {


}
