package com.nrifintech.cms.errorcontroller;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.context.request.WebRequest;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.errorhandler.UserIsEnabledException;
import com.nrifintech.cms.types.Response;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;

@RunWith(MockitoJUnitRunner.class)
public class ErrorControllerTest extends MockMvcSetup {

    @MockBean
    private HttpStatus httpStatus;

    @MockBean
    private HttpHeaders httpHeaders;

    @MockBean
    private WebRequest webRequest;

    @InjectMocks
    private ErrorController errorController;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp("NOT SPECIFIED", this, errorController);
    }

    private Response.JsonEntity createException(Response response) throws IOException {
        return mapFromJson(
                mapToJson(response.getBody()), Response.JsonEntity.class);
    }

    @Test
    public void testHandleNotFoundException() throws IOException {
        NotFoundException notFoundException = new NotFoundException("Entity");
        Response.JsonEntity res =
                createException(
                        errorController
                                .handleNotFoundException(notFoundException));


        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat(notFoundException.getMessage(), is(res.getMessage()));
    }

    @Test
    public void testHandleHttpMessageNotReadable() throws IOException {
        httpStatus = HttpStatus.NOT_FOUND;
        HttpMessageNotReadableException httpMessageNotReadableException =
                new HttpMessageNotReadableException(ErrorMessages.PAYLOADNOTFOUND);
        Response.JsonEntity res =
                createException(
                        errorController
                                .handleHttpMessageNotReadable(
                                        httpMessageNotReadableException, httpHeaders, httpStatus, webRequest));


        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat(httpMessageNotReadableException.getMessage(), is(res.getMessage()));

    }

    @Test
    public void testHandleMissingPathVariable() throws IOException, NoSuchMethodException {
        httpStatus = HttpStatus.NOT_FOUND;
        Method method = errorController.getClass().getMethod("handleNotFoundException", NotFoundException.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        MissingPathVariableException missingPathVariableException =
                new MissingPathVariableException("variableName", parameter);
        Response.JsonEntity res =
                createException(
                        errorController
                                .handleMissingPathVariable(
                                        missingPathVariableException, httpHeaders, httpStatus, webRequest));


        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat(ErrorMessages.PATHVARIABLENOTFOUND, is(res.getMessage()));

    }

    @Test
    public void testHandleHttpRequestMethodNotSupported() throws IOException {

        httpStatus = HttpStatus.METHOD_NOT_ALLOWED;

        HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException =
                new HttpRequestMethodNotSupportedException("GET");
        Response.JsonEntity res =
                createException(
                        errorController
                                .handleHttpRequestMethodNotSupported(
                                        httpRequestMethodNotSupportedException
                                        , httpHeaders, httpStatus, webRequest));


        assertThat(HttpStatus.METHOD_NOT_ALLOWED.value(), is(res.getStatus()));
        assertThat(httpRequestMethodNotSupportedException.getMessage(), is(res.getMessage()));
    }

    @Test
    public void testHandleAccessDeniedException() throws IOException {

        Exception exception = new AccessDeniedException("Access is denied");

        Response.JsonEntity res =
                createException(
                        errorController.handleAccessDeniedException(exception));


        assertThat(HttpStatus.FORBIDDEN.value(), is(res.getStatus()));
        assertThat(exception.getMessage(), is(res.getMessage()));

    }

    @Test
    public void testExpiredJwtException() {

        httpStatus = HttpStatus.UNAUTHORIZED;
        Header header = new DefaultHeader();
        Claims claims = new DefaultClaims();

        Exception exception = new ExpiredJwtException(header, claims, "Invalid token");


        Mockito.lenient()
                        .doAnswer((Answer<Void>) invocation -> null)
                                .when(mock(ErrorController.class)).expiredJwtException(exception, webRequest);


        assertThat(HttpStatus.UNAUTHORIZED.value(), is(httpStatus.value()));
        assertThat(exception.getMessage(), is("Invalid token"));

    }

    @Test
    public void testJwtException() throws IOException {


        Exception exception = new JwtException("Access is denied");

        Response.JsonEntity res =
                createException(
                        errorController.jwtException(exception, webRequest));


        assertThat(HttpStatus.FORBIDDEN.value(), is(res.getStatus()));
        assertThat(exception.getMessage(), is(res.getMessage()));

    }

    @Test
    public void testUserNameNotFoundException() throws IOException {

        Exception exception = new UsernameNotFoundException("username not found");

        Response.JsonEntity res =
                createException(
                        errorController.userNameNotFoundException(exception, webRequest));


        assertThat(HttpStatus.UNAUTHORIZED.value(), is(res.getStatus()));
        assertThat(exception.getMessage(), is(res.getMessage()));
    }

    @Test
    public void testUserIsDisabledException() throws IOException {

        Exception exception = new UserIsDisabledException();

        Response.JsonEntity res =
                createException(
                        errorController.userIsDisabledException(exception, webRequest));


        assertThat(HttpStatus.UNAUTHORIZED.value(), is(res.getStatus()));
        assertThat(exception.getMessage(), is(res.getMessage()));
    }

    @Test
    public void testUserIsEnabledException() throws IOException {

        Exception exception = new UserIsEnabledException("User is disabled");

        Response.JsonEntity res =
                createException(
                        errorController.userIsEnabledException(exception, webRequest));


        assertThat(HttpStatus.UNAUTHORIZED.value(), is(res.getStatus()));
        assertThat(exception.getMessage(), is(res.getMessage()));
    }

    @Test
    public void testMalformedToken() {
        httpStatus = HttpStatus.BAD_REQUEST;
        Exception exception = new UserIsEnabledException("Invalid token");

        Mockito.lenient()
                .doAnswer((Answer<Void>) invocation -> null)
                .when(mock(ErrorController.class)).malformedToken(exception, webRequest);


        assertThat(HttpStatus.BAD_REQUEST.value(), is(httpStatus.value()));
        assertThat(exception.getMessage(), is("Invalid token"));
    }
}