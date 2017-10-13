/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo.repo;




import com.csye6225.demo.pojo.Tasks;
import com.csye6225.demo.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TasksRepository extends CrudRepository<Tasks,Long> {

    Tasks findByTaskId(String id);
    Tasks findTasksByTaskId(String taskId);

    List<Tasks> findTasksByUser(User user);

    @Modifying
    @Transactional
    @Query(value="update Tasks set description = ?1 where taskId=?2")
    void updateTaskDescription( String description, String taskId );


}