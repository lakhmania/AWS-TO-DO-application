
/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo.repo;

import com.csye6225.demo.pojo.Tasks;
import org.springframework.data.repository.CrudRepository;



public interface TasksRepository extends CrudRepository<Tasks, String> {

    Tasks findByTaskId(String id);


}
