package com.wipro.fhir.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;

class TestDiffblueTest {
    /**
     * Method under test: {@link Test#parseFeeds(ResourceRequestHandler, String)}
     */
    @org.junit.jupiter.api.Test
    void testParseFeeds() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        Test test = new Test();

        // Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}",
                test.parseFeeds(new ResourceRequestHandler(), "JaneDoe"));
    }

    /**
     * Method under test: {@link Test#parseFeeds(ResourceRequestHandler, String)}
     */
    @org.junit.jupiter.api.Test
    void testParseFeeds2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}",
                (new Test()).parseFeeds(mock(ResourceRequestHandler.class), "JaneDoe"));
    }
}
