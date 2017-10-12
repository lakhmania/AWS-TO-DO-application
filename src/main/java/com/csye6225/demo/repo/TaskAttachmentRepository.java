package com.csye6225.demo.repo;

import com.csye6225.demo.pojo.TaskAttachments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttachmentRepository extends CrudRepository<TaskAttachments, Long> {


}
