package com.nrifintech.cms.types;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
100 Continue
101 Switching Protocols
102 Processing


2×× Success
200 OK
201 Created
202 Accepted
203 Non-authoritative Information
204 No Content
205 Reset Content
206 Partial Content
207 Multi-Status
208 Already Reported
226 IM Used

3×× Redirection
300 Multiple Choices
301 Moved Permanently
302 Found
303 See Other
304 Not Modified
305 Use Proxy
307 Temporary Redirect
308 Permanent Redirect

4×× Client Error

400 Bad Request
401 Unauthorized
402 Payment Required
403 Forbidden
404 Not Found
405 Method Not Allowed
406 Not Acceptable
407 Proxy Authentication Required
408 Request Timeout
409 Conflict
410 Gone
411 Length Required
412 Precondition Failed
413 Payload Too Large
414 Request-URI Too Long
415 Unsupported Media Type
416 Requested Range Not Satisfiable
417 Expectation Failed
418 I'm a teapot
421 Misdirected Request
422 Unprocessable Entity
423 Locked
424 Failed Dependency
426 Upgrade Required
428 Precondition Required
429 Too Many Requests
431 Request Header Fields Too Large
444 Connection Closed Without Response
451 Unavailable For Legal Reasons
499 Client Closed Request


5×× Server Error
500 Internal Server Error
501 Not Implemented
502 Bad Gateway
503 Service Unavailable
504 Gateway Timeout
505 HTTP Version Not Supported
506 Variant Also Negotiates
507 Insufficient Storage
508 Loop Detected
510 Not Extended
511 Network Authentication Required
599 Network Connect Timeout Error
 */

public class Response extends ResponseEntity<Object> {
	
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	private static class JsonEntity{
		private Object message;
		private Integer status=404;
	}
	
	
	

	public Response(Object body, HttpStatus status) {
		super(body, status);
	}

	public static Response set(Object body, HttpStatus status) {
		return new Response(body, status);
	}
	
	public static Response setMsg(Object body, HttpStatus status) {
		return new Response(new Response.JsonEntity(body,status.value()), status);
	}
	
	public static Response setErr(Object body, HttpStatus status) {
		return new Response(new Response.JsonEntity(body,status.value()), status);
	}

	public static Response setErr(@Nullable Object body, @Nullable MultiValueMap<String, String> headers,HttpStatus status) {
		return new Response(new Response.JsonEntity(body,status.value()), status);
	}
	
	public Response(@Nullable Object body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
		super(body, headers, status);
	}

	public static Response set(@Nullable Object body, @Nullable MultiValueMap<String, String> headers,
			HttpStatus status) {
		return new Response(body,headers, status);
	}

	
}
