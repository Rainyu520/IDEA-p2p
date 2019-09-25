package com.bjpowernode.pay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101300673634";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFTStln7fCu9GrHkn+NZbBJZ66MHF5TAQngZV+tsCrDU6Tmgw4xiNd59487sLKzAozqvJxNCIsocfhf0T83WFa20t2yAEqxZ1ewwYZjIa1rSnauo8P0kR+1uWqGTZuNhWxL3s6qDfP8P+xq+3cQ5mAu98c6PVyAhmdZSElpB41n6QUyRGJ8cwg1Vmq9Qz/qBfQie459sH5bRzKOe0wRbHOqo5dnpmgnOGLaE4Drp7h5jxIPHD649ZZGefIHeC++2W8mroQfmcQwZRnTc6pg/XDjVImI/vdJrmKktqDASucvzTeG+BONgRafEkUSfb6UXYFhztoqNSwISX6Dbvh7vKHAgMBAAECggEAIJzquyhCazBMcdMtQzaVNZggp2jmukhw08GOc7v+k1rkGJSd3N4NFRJ/euTnX3pgHJf8TNn2ankCUBc1J7B5+yKf6eWw82f5wo9wVJve8V/2V39BNRu6ilXFRsJ4YKp20CzPt2Akl4lc5Xks7rDTQ32zbn1IX6ZH4gw1+eGXSoLxpRlmdJZpDwn4wYCqj7sAMiWQxyEVetNpUmSvFq3ASLyK5rVZTGAipN7USNm0LihVli6aKaH6K3LfsZReZYVlVOi9VvY0CvkZu7n5pPwfBEvxCkQUiIhfhyx0+QcKuzoRsgqWwRhbAxA0KPB9FV27D+jTteUdMFPTmsE2mRs78QKBgQDmYMNpOMgXSrCC/+S15ddsc7AJk/aw4lFmBlm3IQ4G5f/NZs+M+gwb0kNZe8+df6zI1R7bcqQneZyZRgH3aviJG74iSXa4TjtE1vMP3SYnMYpd/trqyniHdfpTbGDseeWnVQv0MmPv1J7kRWbDbUqrSOkAtAYkA4NJQ1s8n0eXHQKBgQCUIHqZoOI3ScD1RIHLS6sN82o+Xd4lJSTodGWJQLsVvvt76CXfcGrqnueQLFJkR53hs/+ofwSkyqcu87j8bOO/DeioxSc/XJxlhR53q4s/D4WAv1QSDZa5et/s9cyrB9KLhZvmbgc2qusrP4ikNbYHWYjPFS9l1ZHCzrJ5uyLq8wKBgA4kTHqOEMJVRGaWPLzpwgKXS2wbGB6dle+UXjd+oPTNc5lxQTZfNxUSIC5DCmiZqMqfQoioDemaboF+Kaq9d0o7WL2UEuSXcM31/++uXKQ1R1Ajz95v1VxDYr1yPrTlerJ95wDzguz6+VH77KX8dIP1MtBnPKVDTfDWYKhlAqkFAoGALDY5gatEee4YmtxUxUITjzltJ6pKyNl6V6uONtZMQORdGZGj5Dogn2DIiv6/pNC/bgOUtk0bLLiUB+6U8zrpCzhyjhUybrYMtDx0aJGaITtXxKLWzACkUT48ofSHnzDZhe1eJ/wFSSrgwGrZNPIXH8B4lh6jpAiH1hZtrOnPNUMCgYBsm6WRKuXcgPl1jTjDSKwc7GDESZGM8co/FUEsNNwwC4QUmFzLJUCBpg+hxXakAGPAGAfte/zIdgMAg3qCH2U+Tk8bU+oH9dxo5iiiGjIHpwATbOEFcEJisKP5OwPKtdlN0aY+ZhVUIqdR9PeqXbf/oLSwWQ2FXNtq7noNOYVwnA==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkNYkDC+DFgcadscIk3tIZ9qBr/yegY2/kYw5RcxwBoCiFn/WkarOzp1WksQL1JabdDYnsewTBseUwoUiWHaxn5VwtffnplCgE3Gjc4nYBdmFLRzaLbtgVHi1k6GDGbDPe6ZRegFmjCzso1hIVzR4HRS4QaBPXsVyz/MQm37qBCC+E3tLYUUAqUp1zVyIblOXnYB6YU2wg6/8jg3UIt/Pm6wUDjul/ASGrIa9KL2JxzWViBlWh1n6Kd5Lrg/Pmo8O2bxvVuPVkp1aBy5KkKSOTnnCqk8zwO5TrEcQYHNv1+NtE6AzHSHU9EOolwJMRQyKqDs30C9KVm325s7k5n8eEwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8082/p2p/loan/alipayBack";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

