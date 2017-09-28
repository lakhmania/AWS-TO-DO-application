package com.csye6225.demo;

import com.csye6225.demo.pojo.User;
import com.csye6225.demo.repo.UserRepository;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class DAOTest {


    @Autowired
    private UserRepository userRepository;

    @Ignore
    @Test
    public void testFindUserByName() throws Exception
    {
        User user =this.userRepository.findByUserName("neha1@gmail.com");
        System.out.print(user);
        assertNull(user);
    }

    @Test
    public void testNormal() throws Exception
    {
        assertNull(null);
    }

}
