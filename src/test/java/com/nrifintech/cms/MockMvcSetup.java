package com.nrifintech.cms;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nrifintech.cms.controllers.CartController;
import com.nrifintech.cms.controllers.CartControllerTest;
import com.nrifintech.cms.errorcontroller.ErrorController;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

public class MockMvcSetup {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected ObjectWriter objectWriter = objectMapper.writer();
    protected static String prefix="";

    public static MockMvc setUp(String pref,Object testClass, Object... controllers) {
        System.out.println("Setting up mocks for " + testClass.getClass().getSimpleName() + " .");
        MockitoAnnotations.openMocks(testClass);
        prefix = pref;
        return MockMvcBuilders.standaloneSetup(controllers).setControllerAdvice(new ErrorController()).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> _class)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, _class);
    }

    protected String prefix(String route){
        return prefix+route;
    }
}
