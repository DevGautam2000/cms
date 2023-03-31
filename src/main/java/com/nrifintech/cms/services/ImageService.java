package com.nrifintech.cms.services;

import java.io.*;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.nrifintech.cms.types.ImageFileDecider;

@Service
public class ImageService {

    public String uploadImage(String imageName , String type ,  String imageBase64  , int deciderFlag) throws IOException{
        byte[] image = Base64.getDecoder().decode(imageBase64);
        
        String path = "src\\main\\resources\\static\\assets\\" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "\\" +type + "_" + imageName + ".webp";
        File file = new File(path);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(image);
        String url = "http://localhost:8080/content/assets/" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "/" +  type + "_" + imageName + ".webp";
        return(url);
    } 

    public boolean deleteImage(String imageName , String type , int deciderFlag) throws IOException{
        String path = "src\\main\\resources\\static\\assets\\" + ImageFileDecider.values()[deciderFlag].toString().toLowerCase() + "\\" +type + "_" + imageName + ".webp";
        File file = new File(path);
        return file.delete();
    }
}
