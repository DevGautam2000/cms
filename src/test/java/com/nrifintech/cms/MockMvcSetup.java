package com.nrifintech.cms;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nrifintech.cms.errorcontroller.ErrorController;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

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

    public static Tuple tupleOf(Object a, Object b, Class<?> _class){
        return  new Tuple(){
 
             @Override
             public <X> X get(TupleElement<X> tupleElement) {
                 return null;
             }
     
             @Override
             public <X> X get(String s, Class<X> aClass) {
                 return null;
             }
     
             @Override
             public Object get(String s) {
                 return null;
             }
     
             @Override
             public <X> X get(int i, Class<X> aClass) {
                 Map<Integer , Object> tuple = new HashMap<>();
                 if(aClass.getName().equals(_class.getName())){
                     tuple.put(0, a);
                     tuple.put(1, b);
                 }
                 return (X) tuple.get(i);
             }
     
             @Override
             public Object get(int i) {
                Map<Integer , Object> tuple = new HashMap<>();
                
                     tuple.put(0, a);
                     tuple.put(1, b);
                
                 return tuple.get(i);
             }
     
             @Override
             public Object[] toArray() {
                 return new Object[0];
             }
     
             @Override
             public List<TupleElement<?>> getElements() {
                 return null;
             }
         };
     }

     public static Tuple tupleOf(Object a, Object b ,Object c, Class<?> _class){
        return  new Tuple(){
 
             @Override
             public <X> X get(TupleElement<X> tupleElement) {
                 return null;
             }
     
             @Override
             public <X> X get(String s, Class<X> aClass) {
                 return null;
             }
     
             @Override
             public Object get(String s) {
                 return null;
             }
     
             @Override
             public <X> X get(int i, Class<X> aClass) {
                 Map<Integer , Object> tuple = new HashMap<>();
                 if(aClass.getName().equals(_class.getName())){
                     tuple.put(0, a);
                     tuple.put(1, b);
                     tuple.put(2, c);
                 }
                 return (X) tuple.get(i);
             }
     
             @Override
             public Object get(int i) {
                Map<Integer , Object> tuple = new HashMap<>();
                
                     tuple.put(0, a);
                     tuple.put(1, b);
                     tuple.put(2, c);
                
                 return tuple.get(i);
             }
     
             @Override
             public Object[] toArray() {
                 return new Object[0];
             }
     
             @Override
             public List<TupleElement<?>> getElements() {
                 return null;
             }
         };
     }
}
