package com.wipro.fhir.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

class MongoConfigTest {

    private MongoConfig mongoConfigUnderTest;

    @BeforeEach
    void setUp() {
        mongoConfigUnderTest = new MongoConfig();
        ReflectionTestUtils.setField(mongoConfigUnderTest, "database", "database");
    }
}
