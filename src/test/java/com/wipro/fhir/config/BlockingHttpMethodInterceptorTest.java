package com.wipro.fhir.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlockingHttpMethodInterceptorTest {

    private BlockingHttpMethodInterceptor blockingHttpMethodInterceptorUnderTest;

    @BeforeEach
    void setUp() {
        blockingHttpMethodInterceptorUnderTest = new BlockingHttpMethodInterceptor();
    }

    @Test
    void testPreHandle() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the test
        final boolean result = blockingHttpMethodInterceptorUnderTest.preHandle(request, response, "handler");

        // Verify the results
        assertThat(result).isFalse();
    }


    @Test
    void testPostHandle() throws Exception {
        blockingHttpMethodInterceptorUnderTest.postHandle(new MockHttpServletRequest(), new MockHttpServletResponse(),
                "handler", new ModelAndView("viewName", "modelName", "modelObject"));
    }

    @Test
    void testAfterCompletion() throws Exception {
        blockingHttpMethodInterceptorUnderTest.afterCompletion(new MockHttpServletRequest(),
                new MockHttpServletResponse(), "handler", new Exception("message"));
    }
}
