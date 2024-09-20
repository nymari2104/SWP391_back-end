package com.SWP391.KoiManagement.configuration;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageResizer {

    public static String resizeAndConvertImageToBase64(MultipartFile imageFile, int width, int height) throws IOException {
        //đọc ảnh từ MultipartFile
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        //resize bàng thumbnails
        BufferedImage resizedImage = Thumbnails.of(originalImage).size(width, height).asBufferedImage();

        //lấy type của ảnh
        String fileType = imageFile.getContentType();
        String formatName = fileType.substring(fileType.lastIndexOf("/") + 1);

        //chuyền ảnh đã resize thành chuỗi Base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, formatName, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        //chuyển đổi ảnh thành base64
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return "data:" + fileType + ";base64," + base64Image;
    }
}
