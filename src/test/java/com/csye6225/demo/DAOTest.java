package com.csye6225.demo;

import com.csye6225.demo.pojo.User;
import com.csye6225.demo.repo.UserRepository;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNull;

public class DAOTest {


    @Autowired
    private UserRepository userRepository;

    @Ignore
    @Test
    public void testNormal() throws Exception
    {
        assertNull(null);
    }

}
