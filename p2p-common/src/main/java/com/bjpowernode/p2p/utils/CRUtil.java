package com.bjpowernode.p2p.utils;/**
 * ClassName:CRUtil
 * Package:com.bjpowernode.p2p.utils
 * Description
 *
 * @date ：${Date}
 */

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Rainyu
 * 2019/9/25
 */

public class CRUtil {



    public static void toStream(String msg, OutputStream response) {
        //创建一个矩阵对象
        Map<EncodeHintType, Object> map = new HashMap<> ();
        map.put (EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter ().encode (msg, BarcodeFormat.QR_CODE, 100, 100, map);
        } catch (WriterException e) {
            e.printStackTrace ();
        }


        //将矩阵对象转换为二维码
        try {
            MatrixToImageWriter.writeToStream (bitMatrix, "png", response);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
