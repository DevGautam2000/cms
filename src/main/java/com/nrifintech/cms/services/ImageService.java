package com.nrifintech.cms.services;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;
import com.nrifintech.cms.types.ImageFileDecider;

@Service
public class ImageService {

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
     
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

    public String uploadImage(String imageName , String type ,  String imageBase64  , int deciderFlag) throws IOException, NoSuchAlgorithmException{
        byte[] image = Base64.getDecoder().decode(imageBase64);
        
        String path = "src\\main\\resources\\static\\assets\\" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "\\" +  toHexString(getSHA(type + "_" + imageName)) + ".webp";
        File file = new File(path);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(image);
        String url = "http://localhost:8080/content/assets/" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "/" +  toHexString(getSHA(type + "_" + imageName)) + ".webp";
        return(url);
    } 

    public boolean deleteImage(String imageName , String type , int deciderFlag) throws IOException, NoSuchAlgorithmException{
        String path = "src\\main\\resources\\static\\assets\\" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "\\" + toHexString(getSHA(type + "_" + imageName)) + ".webp";
        File file = new File(path);
        return file.delete();
    }
    //toHexString(getSHA(s3))
}
