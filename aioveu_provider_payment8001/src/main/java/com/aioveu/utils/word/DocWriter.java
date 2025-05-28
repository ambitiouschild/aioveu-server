package com.aioveu.utils.word;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.util.*;

public class DocWriter {
  
    public static void searchAndReplace(String srcPath, String destPath, Map<String, String> map) {
        try {  
            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(srcPath));
            /** 
             * 替换段落中的指定文字 
             */  
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            while (itPara.hasNext()) {  
                XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                Set<String> set = map.keySet();
                Iterator<String> iterator = set.iterator();  
                while (iterator.hasNext()) {
                    List<XWPFRun> run=paragraph.getRuns();
                    for(int i=0;i<run.size();i++) {
                    	String text = run.get(i).getText(0);
                    	for (Map.Entry<String, String> e : map.entrySet()) {
                     	   if (text != null && text.contains(e.getKey())) {
                                text = text.replace(e.getKey(), e.getValue());
                                run.get(i).setText(text,0);
                              }
                        }  
                    }  
                }  
            } 
  
            /** 
             * 替换表格中的指定文字 
             */  
             Iterator<XWPFTable> itTable = document.getTablesIterator();
            while (itTable.hasNext()) {  
                XWPFTable table = (XWPFTable) itTable.next();  
                int count = table.getNumberOfRows();  
                for (int i = 0; i < count; i++) {  
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();  
                    for (XWPFTableCell cell : cells) {
                         for (XWPFParagraph p : cell.getParagraphs()) {
                             for (XWPFRun r : p.getRuns()) {
                               String text = r.getText(0);
                               for (Map.Entry<String, String> e : map.entrySet()) {
                                     if (text != null && text.contains(e.getKey())) {
                                       text = text.replace(e.getKey(), e.getValue());
                                       r.setText(text,0);
                                     }
                               }  
                             }
                          }
 
 
                    }  
                }  
            }  
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(destPath);  
            document.write(outStream);  
            outStream.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    public static void main(String[] args) throws Exception {  
        Map<String, String> map = new HashMap<String, String>();
        map.put("${name}", "张三");
        map.put("${signDate}", "2023年01月03日");
        map.put("${phone}", "17621190028");
//        map.put("${remark}", "remark info");
        String srcPath = "D:\\temp\\tm_12.docx";
        String destPath = "D:\\temp\\company_22.docx";
        searchAndReplace(srcPath, destPath, map);  
    }  
}  