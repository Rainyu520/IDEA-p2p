/**
 * ClassName:Test
 * Package:PACKAGE_NAME
 * Description
 *
 * @date ：${Date}
 */

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 *Author：Rainyu
 *2019/9/20
 */

public class Test {
    public static void main(String[] args) throws DocumentException {
        String xmlString = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><bookstore><book><title lang=\"en\">Harry Potter</title><author>J K. Rowling</author><year>2005</year><price>29.99</price></book></bookstore>";
        //Document对象

        Document document = DocumentHelper.parseText (xmlString);

          //title[1]
        Node node = document.selectSingleNode ("bookstore/book[1]/title");
        String text = node.getText ();
        System.out.println (text);
    }
}
