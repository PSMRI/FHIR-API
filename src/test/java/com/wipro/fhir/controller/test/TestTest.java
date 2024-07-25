package com.wipro.fhir.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.resource_gateway.OPConsultRecordBundleImpl;
import com.wipro.fhir.utils.http.HttpUtils;
@ExtendWith(SpringExtension.class)
class TestTest {
	
	@InjectMocks
	private Test test;
	
	@Mock
	HttpUtils httpUtils;
	
	@Mock
	private OPConsultRecordBundleImpl oPConsultRecordBundleImpl;
	
	@org.junit.jupiter.api.Test
    void testParseFeeds() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        Test test = new Test();

        // Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}",
                test.parseFeeds(new ResourceRequestHandler(), "JaneDoe"));
    }

    @org.junit.jupiter.api.Test
    void testParseFeeds2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}",
                (new Test()).parseFeeds(mock(ResourceRequestHandler.class), "JaneDoe"));
    }
}
