package com.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=mySecretKeyForTestingPurposesOnlyNeedsToBeLongEnough32Bytes12345678"
})
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
