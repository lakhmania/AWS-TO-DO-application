package com.csye6225.demo;

import com.csye6225.demo.pojo.User;
import com.csye6225.demo.repo.UserRepository;


<<<<<<< HEAD
import org.junit.Before;
=======
>>>>>>> 68a77a061700b215af0382fee1169e68802b1a72
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
