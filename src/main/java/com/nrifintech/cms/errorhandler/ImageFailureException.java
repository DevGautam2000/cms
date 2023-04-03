package com.nrifintech.cms.errorhandler;

/**
 * This class is a custom exception class that extends the Exception class. It has one constructor that
 * takes a String as a parameter.
 */
public class ImageFailureException extends Exception {
    public ImageFailureException(String s){
        super(s);
    }
    
}
