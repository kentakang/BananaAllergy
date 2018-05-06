package com.kentakang.bananaallergy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageProcessor {
    public static void ImgDownload(String imgSrc) {
        try {
            URL url = new URL(imgSrc);
            String imgFile = imgSrc.substring(imgSrc.lastIndexOf('/') + 1, imgSrc.length());
            String ext = imgSrc.substring(imgSrc.lastIndexOf('.') + 1, imgSrc.length());
            BufferedImage img = ImageIO.read(url);

            ImageIO.write(img, ext, new File("../webapps/BananaAllergy/", imgFile));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkImg(String imgSrc) {
        String imgFile = imgSrc.substring(imgSrc.lastIndexOf('/') + 1, imgSrc.length());
        File img = new File("../webapps/BananaAllergy", imgFile);
        boolean isExists = img.exists();

        return isExists;
    }
}
