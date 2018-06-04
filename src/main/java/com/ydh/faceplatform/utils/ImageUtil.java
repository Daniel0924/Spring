package com.ydh.faceplatform.utils;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Base64;

public class ImageUtil {

    /**
     * 对本地的图片进行编码，然后返回编码后的字符串
     *
     * @param imageFile :要编码的本地图片路径
     * @return ：编码后的图片字符串
     */
    public static String encodeImageToBase64(String imageFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imageFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            System.out.println("file path not found!");
            e.printStackTrace();
        }
        String res = Base64.getEncoder().encodeToString(data);
        return res;
    }

    /**
     * 将编码之后的字符串还原为图片，并保存到指定的路径下
     * @param code
     * @param path
     * @param imgName
     */
    public static void decodeBase64ToImage(String code, String path, String imgName) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            FileOutputStream write = new FileOutputStream(new File(path + imgName));
            byte[] decoderBytes = decoder.decodeBuffer(code);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把编码字符串写到本地html文件中
     *
     * @param code
     * @throws IOException
     */
    public static void writeHTML(String code) throws IOException {
        String HTMLPath = "D:\\data\\facePicture\\code.html";
        File file = new File(HTMLPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("success create new HTML file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream = null;
        PrintWriter printWriter = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            builder.append("<br>");
            builder.append("<img src='data:image/jpg;base64,");
            builder.append(code);
            builder.append("'/>");
            fileOutputStream = new FileOutputStream(file);
            printWriter = new PrintWriter(fileOutputStream);
            printWriter.write(builder.toString());
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            printWriter.close();
            fileInputStream.close();
            fileOutputStream.close();
            bufferedReader.close();
            inputStreamReader.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "D:\\data\\facePicture\\2.jpg";

        String pictureCode = encodeImageToBase64(path);
        writeHTML(pictureCode);
        System.out.println(pictureCode);
    }

}
