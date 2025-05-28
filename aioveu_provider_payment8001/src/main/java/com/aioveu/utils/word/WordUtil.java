package com.aioveu.utils.word;

import com.aioveu.dto.AgreementDTO;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @description word邮件模板工具类
 * @author: 雒世松
 * @date: 2025/11/16 15:05
 */
@Slf4j
public class WordUtil {

    /**
     * 根据指定的参数值、模板，生成 word 文档
     * @param param    需要替换的变量
     * @param template 模板
     */
    public static XWPFDocument generateWord(Map<String, String> param, String template) {
        XWPFDocument doc = null;
        try {
            log.info("模板:{}", template);
            OPCPackage pack = POIXMLDocument.openPackage(template);
            doc = new XWPFDocument(pack);
            if (param != null && param.size() > 0) {
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                //处理段落 替换里面的文字
                processParagraphs(paragraphList, param);

                /**
                 * 替换表格中的指定文字
                 */
                Iterator<XWPFTable> itTable = doc.getTablesIterator();
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
                                    for (Entry<String, String> e : param.entrySet()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 处理段落中文本，替换文本中定义的变量；
     * @param paragraphList 段落列表
     * @param param  需要替换的变量及变量值
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, String> param) {
        if (paragraphList != null && !paragraphList.isEmpty()) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (StringUtils.isNotEmpty(text)) {
                        boolean isSetText = false;
                        for (Entry<String, String> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if (text.contains(key)) {
                                isSetText = true;
                                String value = entry.getValue();
                                if (key.contains("甲方章")) {
                                    // 处理图片插入
                                    try {
                                        if (value.startsWith("http")) {
                                            byte[] imageBytes = FileUtil.imageUrlToByteArray(value);
                                            run.addPicture(new ByteArrayInputStream(imageBytes), XWPFDocument.PICTURE_TYPE_PNG, "seal", Units.toEMU(60), Units.toEMU(60));
                                        } else {
                                            File imageFile = new File(value);
                                            if (imageFile.exists()) {
                                                FileInputStream imageStream = new FileInputStream(imageFile);
                                                byte[] imageBytes = IOUtils.toByteArray(imageStream);
                                                int format = getImageFormat(imageFile);
                                                // 获取图片原始尺寸
                                                BufferedImage image = ImageIO.read(imageFile);
                                                int width = image.getWidth();
                                                int height = image.getHeight();
                                                // 按比例缩放图片
                                                int maxWidth = 100;
                                                if (width > maxWidth) {
                                                    double ratio = (double) maxWidth / width;
                                                    width = maxWidth;
                                                    height = (int) (height * ratio);
                                                }
                                                run.addPicture(new ByteArrayInputStream(imageBytes), format, imageFile.getName(), Units.toEMU(width), Units.toEMU(height));
                                                imageStream.close();
                                            }
                                        }
                                    } catch (IOException | InvalidFormatException e) {
                                        e.printStackTrace();
                                    }
                                    text = text.replace(key, "");
                                } else {
                                    text = text.replace(key, value);
                                }
                            }
                        }
                        if (isSetText) {
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }

    private static int getImageFormat(File imageFile) {
        String fileName = imageFile.getName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return XWPFDocument.PICTURE_TYPE_JPEG;
        } else if (fileName.endsWith(".png")) {
            return XWPFDocument.PICTURE_TYPE_PNG;
        } else if (fileName.endsWith(".gif")) {
            return XWPFDocument.PICTURE_TYPE_GIF;
        }
        return XWPFDocument.PICTURE_TYPE_JPEG;
    }

    /**
     * 设置单元格水平位置和垂直位置
     *
     * @param xwpfTable
     * @param verticalLoction    单元格中内容垂直上TOP，下BOTTOM，居中CENTER，BOTH两端对齐
     * @param horizontalLocation 单元格中内容水平居中center,left居左，right居右，both两端对齐
     */
    public static void setCellLocation(XWPFTable xwpfTable, String verticalLoction, String horizontalLocation) {
        List<XWPFTableRow> rows = xwpfTable.getRows();
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                CTTc cttc = cell.getCTTc();
                CTP ctp = cttc.getPList().get(0);
                CTPPr ctppr = ctp.getPPr();
                if (ctppr == null) {
                    ctppr = ctp.addNewPPr();
                }
                CTJc ctjc = ctppr.getJc();
                if (ctjc == null) {
                    ctjc = ctppr.addNewJc();
                }
                //水平居中
                ctjc.setVal(STJc.Enum.forString(horizontalLocation));
                //垂直居中
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.valueOf(verticalLoction));
            }
        }
    }

    /**
     * 设置表格位置
     *
     * @param xwpfTable
     * @param location  整个表格居中center,left居左，right居右，both两端对齐
     */
    public static void setTableLocation(XWPFTable xwpfTable, String location) {
        CTTbl cttbl = xwpfTable.getCTTbl();
        CTTblPr tblpr = cttbl.getTblPr() == null ? cttbl.addNewTblPr() : cttbl.getTblPr();
        CTJc cTJc = tblpr.addNewJc();
        cTJc.setVal(STJc.Enum.forString(location));
    }



    /**
     * 在定位的位置插入表格；
     * @param key 定位的变量值
     * @param doc 需要替换的DOC
     * @param tableList 数据
     */
    public static void insertTab(String key, XWPFDocument doc, List<Map<String, Object>> tableList) {
        if (tableList==null || tableList.size()==0){
            return;
        }
        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        if (text.equals(key)) {
                            XmlCursor cursor = paragraph.getCTP().newCursor();
                            XWPFTable table = doc.insertNewTbl(cursor);

                            XWPFTableRow titleRow = table.getRow(0);
                            titleRow.getCell(0).setText("场馆名称");
                            XWPFTableCell cell12 = titleRow.createCell();
                            cell12.setText("地址");
                            titleRow.addNewTableCell().setText("合作内容");
                            titleRow.addNewTableCell().setText("周末及节假日(分钟/人)");
                            titleRow.addNewTableCell().setText("工作日晚上(分钟/人)");
                            titleRow.addNewTableCell().setText("普通时段价格(分钟/人)");

                            for (Map<String, Object> item : tableList){
                                XWPFTableRow dataRow = table.createRow();
                                dataRow.getCell(0).setText(item.get("name").toString());
                                dataRow.getCell(1).setText(item.get("address").toString());
                                if (item.get("categoryId").equals(1)){
                                    dataRow.getCell(2).setText("场馆健身");
                                }else if (item.get("categoryId").equals(12)){
                                    dataRow.getCell(2).setText("游泳");
                                }else {
                                    dataRow.getCell(2).setText("其他");
                                }
                                dataRow.getCell(3).setText(item.get("busyPrice")+ "元");
                                dataRow.getCell(4).setText(item.get("workNightPrice")+ "元");
                                dataRow.getCell(5).setText(item.get("idlePrice") + "元");
                            }
                            // 设置表格整体居中显示
                            setTableLocation(table,"center");
                            setCellLocation(table,"CENTER","center");
                        }

                    }

                }

            }

        }
    }

    public static void createFile(String templatePath) throws Exception {
        OssUtil.uploadSingleImage("doc/23.docx", testAgreement(templatePath));
    }

    /**
     * 创建协议到存储服务器
     * @param templatePath 模板路径
     * @param param 参数
     * @param outFile 输出文件名
     * @return
     * @throws Exception
     */
    public static boolean createAgreement2Oss(String templatePath, Map<String, String> param,
                                              String outFile) throws Exception {
        XWPFDocument doc = WordUtil.generateWord(param, templatePath);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        // doc should be a XWPFDocument
        doc.write(b);
        return OssUtil.uploadSingleImage(outFile, b.toByteArray());
    }

    /**
     * 创建word协议到文件
     * @param templatePath
     * @param param
     * @param outFile
     * @return
     * @throws Exception
     */
    public static boolean createAgreement2File(String templatePath, Map<String, String> param, String outFile) {
        XWPFDocument document = WordUtil.generateWord(param, templatePath);
        try (FileOutputStream out = new FileOutputStream(outFile)) {
            // 将文档写入到指定的文件中
            document.write(out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static byte[] testAgreement(String templatePath) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("name", "张三");
        param.put("phone", "17621190028");
        param.put("orderName", "双打三人对战课");
        param.put("couponNumber", "11张");
        param.put("validDate", "激活后30天有效");
        param.put("signDate", "2023年1月3日");

        XWPFDocument doc = WordUtil.generateWord(param, templatePath + "tm_11.docx");

//        String fileOutPath = templatePath + "company_" + 12 + ".docx";
//        FileOutputStream fopts = new FileOutputStream(fileOutPath);
//        doc.write(fopts);

//        OssUtil.uploadSingleImage("doc/22.docx", new FileInputStream(fileOutPath));
//        return null;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        // doc should be a XWPFDocument
        doc.write(b);
        return b.toByteArray();
    }

    public static String createAgreement(AgreementDTO agreementDTO, String templatePath) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("companyName", agreementDTO.getCompanyName());
        param.put("companyName1", getBlackStr(agreementDTO.getCompanyName(), 70));
        param.put("companyAddress", getBlackStr(agreementDTO.getCompanyAddress(), 70));
        param.put("username", agreementDTO.getFullName());
        param.put("username1", getBlackStr("", 53));
        param.put("idCard", agreementDTO.getIdCard());
        param.put("idCard1", getBlackStr(agreementDTO.getIdCard(), 69));
        param.put("tel", getBlackStr(agreementDTO.getTel(), 71));
        param.put("kaishi", DateFormatUtils.format(agreementDTO.getStartTime(), "yyyy年MM月dd日"));
        param.put("startTime1", getBlackStr(param.get("kaishi").toString(), 69));
        param.put("jieshu", DateFormatUtils.format(agreementDTO.getEndTime(), "yyyy年MM月dd日"));
        param.put("weChatId", agreementDTO.getWeChatId());
        param.put("email", agreementDTO.getEmail());

        XWPFDocument doc = WordUtil.generateWord(param, templatePath + "agreements.docx");
        WordUtil.insertTab("table", doc, agreementDTO.getList());
        param.put("table", "");
        WordUtil.processParagraphs(doc.getParagraphs(), param);
        String fileOutPath = templatePath + "company_" + agreementDTO.getCompanyId() + ".docx";
        FileOutputStream fopts = new FileOutputStream(fileOutPath);
        doc.write(fopts);
        return fileOutPath;
    }

    private static String getBlackStr(String text, int number) throws Exception {
        if (text==null){
            return "";
        }
        int length = number - new String(text.getBytes("gb2312"),"iso-8859-1").length();
        StringBuffer black = new StringBuffer();
        for (int i=0; i<length; i++){
            black.append(" ");
        }
        return text + black.toString();
    }

    /**
     * docx文档转换为PDF
     *
     * @param pdfPath PDF文档存储路径
     * @throws Exception 可能为Docx4JException, FileNotFoundException, IOException等
     */
    public static void convertDocxToPdf(String docxPath, String pdfPath) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(docxPath);
            fileOutputStream = new FileOutputStream(pdfPath);
            WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(file);
            setFontMapper(mlPackage);
            Docx4J.toPDF(mlPackage, Files.newOutputStream(new File(pdfPath).toPath()));
        }catch (Exception e){
            e.printStackTrace();
            log.error("docx文档转换为PDF失败");
        }finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    private static void setFontMapper(WordprocessingMLPackage mlPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        //加载字体文件（解决linux环境下无中文字体问题）
        if(PhysicalFonts.get("SimSun") == null) {
            System.out.println("加载本地SimSun字体库");
//          PhysicalFonts.addPhysicalFonts("SimSun", WordUtils.class.getResource("/fonts/SIMSUN.TTC"));
        }
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
        fontMapper.put("等线", PhysicalFonts.get("SimSun"));
        fontMapper.put("等线 Light", PhysicalFonts.get("SimSun"));
        fontMapper.put("华文琥珀", PhysicalFonts.get("STHupo"));
        fontMapper.put("华文隶书", PhysicalFonts.get("STLiti"));
        fontMapper.put("华文新魏", PhysicalFonts.get("STXinwei"));
        fontMapper.put("华文彩云", PhysicalFonts.get("STCaiyun"));
        fontMapper.put("方正姚体", PhysicalFonts.get("FZYaoti"));
        fontMapper.put("方正舒体", PhysicalFonts.get("FZShuTi"));
        fontMapper.put("华文细黑", PhysicalFonts.get("STXihei"));
        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("新細明體", PhysicalFonts.get("SimSun"));
        //解决宋体（正文）和宋体（标题）的乱码问题
        PhysicalFonts.put("PMingLiU", PhysicalFonts.get("SimSun"));
        PhysicalFonts.put("新細明體", PhysicalFonts.get("SimSun"));
        //宋体&新宋体
        PhysicalFont simsunFont = PhysicalFonts.get("SimSun");
        fontMapper.put("SimSun", simsunFont);
        //设置字体
        mlPackage.setFontMapper(fontMapper);
    }


    /**
     * 测试用方法
     */
    public static void main(String[] args) throws Exception {
        convertDocxToPdf("D://Test/PO17458014966406999.docx", "D://Test/PO17458014966406999.pdf");
    }

}