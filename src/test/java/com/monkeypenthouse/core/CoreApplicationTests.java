package com.monkeypenthouse.core;

import com.monkeypenthouse.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest
class CoreApplicationTests {

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
	}

}
