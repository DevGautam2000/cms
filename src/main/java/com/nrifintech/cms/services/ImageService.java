package com.nrifintech.cms.services;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.nrifintech.cms.errorhandler.ImageFailureException;
import com.nrifintech.cms.types.ImageFileDecider;

/**
 * It takes in a base64 image, converts it to a byte array, hashes the image name and type, and saves
 * it to a file.
 */
@Service
public class ImageService {

   /**
    * It takes a string as input, and returns a byte array of the SHA-256 hash of the input string
    * 
    * @param input The string to be hashed.
    * @return The SHA-256 hash of the input string.
    */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
     
   /**
    * It converts a byte array into a hexadecimal string
    * 
    * @param hash The hash to be converted to a hex string.
    * @return The hash of the input string.
    */
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }


   /**
    * It takes in the image name, type, image base64 and decider flag and returns the url of the image
    * 
    * @param imageName The name of the image
    * @param type The type of the image.
    * @param imageBase64 The base64 string of the image
    * @param deciderFlag This is an integer value that decides the folder in which the image will be
    * stored.
    * @return The URL of the image.
 * @throws ImageFailureException
    */
    public String uploadImage(String imageName , String type ,  String imageBase64  , int deciderFlag) throws IOException, NoSuchAlgorithmException, ImageFailureException{
           try {
            byte[] image = Base64.getDecoder().decode(imageBase64);
            String path = "src\\main\\resources\\static\\assets\\" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "\\" +  toHexString(getSHA(type + "_" + imageName)) + ".webp";
            File file = new File(path);
            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                outputStream.write(image);
            }
            String url = "http://localhost:8080/content/assets/" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "/" +  toHexString(getSHA(type + "_" + imageName)) + ".webp";
            return(url);
           } catch (Exception e) {
            throw new ImageFailureException("Image corrupt/not supported");
           }
    } 

   /**
    * It deletes an image from the server
    * 
    * @param imageName The name of the image file.
    * @param type The type of the image, for example, if it's a profile picture, then the type would be
    * "profile"
    * @param deciderFlag This is an enum that decides the type of image.
    * @return A boolean value.
    */
    public boolean deleteImage(String imageName , String type , int deciderFlag) throws IOException, NoSuchAlgorithmException{
        String path = "src\\main\\resources\\static\\assets\\" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "\\" + toHexString(getSHA(type + "_" + imageName)) + ".webp";
        File file = new File(path);
        return file.delete();
    }
}
