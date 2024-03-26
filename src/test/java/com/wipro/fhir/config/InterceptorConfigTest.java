package com.wipro.fhir.config;

import com.wipro.fhir.utils.http.HTTPRequestInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@ExtendWith(MockitoExtension.class)
class InterceptorConfigTest {

    @Mock
    private HTTPRequestInterceptor mockRequestInterceptor;

    private InterceptorConfig interceptorConfigUnderTest;

    @BeforeEach
    void setUp() {
        interceptorConfigUnderTest = new InterceptorConfig();
        interceptorConfigUnderTest.requestInterceptor = mockRequestInterceptor;
    }

    @Test
    void testAddInterceptors() {
        // Setup
        final InterceptorRegistry registry = new InterceptorRegistry();

        // Run the test
        interceptorConfigUnderTest.addInterceptors(registry);

        // Verify the results
    }
}
