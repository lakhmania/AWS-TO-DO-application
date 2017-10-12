/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo.repo;



import com.csye6225.demo.pojo.TaskAttachments;
import com.csye6225.demo.pojo.Tasks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskAttachmentRepository extends CrudRepository<TaskAttachments,Integer> {

   //@Query("Select ta.fileName from TaskAttachments ta where ta. = ?1")
//    @Query("Select ta.fileName from TaskAttachments ta where ta.taskId = ?1")

   //List findTaskAttachmentsByid(@Param("taskId") String taskId);

   List<TaskAttachments> findByTask(Tasks task);
}
