package com.ducetech.framework.util;


import com.ducetech.app.model.ScheduleInfo;
import com.ducetech.app.model.ShiftSetting;
import com.ducetech.app.model.WorkflowContent;
import com.ducetech.app.model.vo.WorkflowVO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * pdf工具类
 */
public class PdfUtil {
    /**
     * 获得中文字体
     *
     * @return
     * @throws Exception
     */
    public static Font getPdfChineseFont() throws Exception {
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);
        return fontChinese;
    }

    /**
     * 获取格式换的pdf
     *
     * @param content
     * @return
     */
    public static PdfPCell getFormatPdfCell(String content) {
        PdfPCell pdfCell = pdfCell = new PdfPCell(); //表格的单元格
        Paragraph paragraph = null;
        try {
            paragraph = new Paragraph(content, getPdfChineseFont());
            pdfCell.setPhrase(paragraph);

            pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfCell;
    }


    /**
     * 导出西直门排版表pdf
     * @param map
     * @param shiftList
     * @param workflowMap
     * @param userIdSet
     * @param ouputStream
     * @throws Exception
     */
    public static void printXiZhiMenPdf(Map<String, List<ScheduleInfo>> map, List<ShiftSetting> shiftList, Map<String, List<WorkflowVO>> workflowMap, Set<String> userIdSet, OutputStream ouputStream) throws Exception {
        //生成压缩包
        ZipOutputStream zip = new ZipOutputStream(ouputStream);

        BaseColor baseColor = new BaseColor(240,240,240);

        for (String userId : userIdSet) {
            List<ScheduleInfo> scheduleInfoList = map.get(userId);

            String userName = "";
            if (scheduleInfoList != null && scheduleInfoList.size() > 0) {
                userName = scheduleInfoList.get(0).getUserName();
            }

            //pdf
            Document document = new Document();
            //实例化一个压缩实体
            ZipEntry entry = new ZipEntry(userName + ".pdf");
            //将压缩实体放入压缩包
            zip.putNextEntry(entry);
            //将excel内容写进压缩实体
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, baos);
            //设置A4
            document.setPageSize(PageSize.A4);
            //打开
            document.open();
            document.add(new Paragraph(userName,getPdfChineseFont()));
            //pdf的宽度
            float[] widths = {100, 150, 100, 100, 200};
            //同时指定了行数
            PdfPTable table = new PdfPTable(widths);
            table.setTotalWidth(650);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell pdfCell;
            pdfCell = PdfUtil.getFormatPdfCell("日期");
            table.addCell(pdfCell);
            pdfCell = PdfUtil.getFormatPdfCell("星期");
            table.addCell(pdfCell);
            pdfCell = PdfUtil.getFormatPdfCell("上班");
            pdfCell.setBackgroundColor(baseColor);
            table.addCell(pdfCell);
            pdfCell = PdfUtil.getFormatPdfCell("班次");
            table.addCell(pdfCell);
            pdfCell = PdfUtil.getFormatPdfCell("工作时间");
            table.addCell(pdfCell);

            int num = 0;
            for (int i = 0; i < scheduleInfoList.size(); i++) {
                ScheduleInfo scheduleInfo = scheduleInfoList.get(i);
                int flag = shiftList.size() + 1;
                flag = num % flag;

                pdfCell = PdfUtil.getFormatPdfCell(DateUtil.formatDate(scheduleInfo.getScheduleDate(), DateUtil.XIZHIMENDATE));
                table.addCell(pdfCell);

                pdfCell = PdfUtil.getFormatPdfCell(scheduleInfo.getScheduleWeek());
                table.addCell(pdfCell);

                //班次
                if (scheduleInfo.getShiftCode() != null && scheduleInfo.getSerialNumber() != null) {
                    pdfCell = PdfUtil.getFormatPdfCell(scheduleInfo.getSerialNumber());
                    String color = scheduleInfo.getShiftColor();
                    //转为RGB码
                    int r = Integer.parseInt((color.substring(0, 2)), 16);   //转为16进制
                    int g = Integer.parseInt((color.substring(2, 4)), 16);
                    int b = Integer.parseInt((color.substring(4, 6)), 16);
                    pdfCell.setBackgroundColor(new BaseColor(r,g,b));
                    //pdfCell.setBackgroundColor(baseColor);
                    table.addCell(pdfCell);
                } else {
                    pdfCell = PdfUtil.getFormatPdfCell("休");
                    pdfCell.setBackgroundColor(baseColor);
                    table.addCell(pdfCell);
                }

                ShiftSetting shiftSetting = null;
                if (flag == shiftList.size()) {
                    pdfCell = PdfUtil.getFormatPdfCell("");
                    table.addCell(pdfCell);

                    pdfCell = PdfUtil.getFormatPdfCell("");
                    table.addCell(pdfCell);
                } else {
                    shiftSetting = shiftList.get(flag);
                    //班次
                    pdfCell = PdfUtil.getFormatPdfCell(shiftSetting.getShiftName() + "(" + shiftSetting.getShiftCode() + ")");
                    String color = shiftSetting.getShiftColor();
                    //转为RGB码
                    int r = Integer.parseInt((color.substring(0, 2)), 16);   //转为16进制
                    int g = Integer.parseInt((color.substring(2, 4)), 16);
                    int b = Integer.parseInt((color.substring(4, 6)), 16);
                    pdfCell.setBackgroundColor(new BaseColor(r,g,b));
                    table.addCell(pdfCell);

                    //工作时间
                    Integer shiftStart = shiftSetting.getStartAt();
                    Integer shiftEnd = shiftSetting.getEndAt();
                    if (shiftStart != null && shiftEnd != null) {
                        pdfCell = PdfUtil.getFormatPdfCell(DateUtil.minuToTime2(shiftStart) + "---" + DateUtil.minuToTime2(shiftEnd));
                        table.addCell(pdfCell);

                    } else {
                        pdfCell = PdfUtil.getFormatPdfCell("");
                        table.addCell(pdfCell);
                    }
                }
                num++;

                pdfCell = PdfUtil.getFormatPdfCell("");
                StringBuilder sb = new StringBuilder(" ");
                PdfPCell pdfCell1 = PdfUtil.getFormatPdfCell("");
                StringBuilder sb1 = new StringBuilder(" ");

                if (scheduleInfo.getShiftId() != null) {
                    List<WorkflowVO> settinhWorkflowVOList = null;

                    if (workflowMap != null && workflowMap.size() > 0) {
                        settinhWorkflowVOList = workflowMap.get(scheduleInfo.getShiftId());
                    }

                    List<WorkflowContent> workflowContentList = null;

                    for (WorkflowVO wfvo : settinhWorkflowVOList) {
                        String siSerialNumber = scheduleInfo.getSerialNumber();
                        if (siSerialNumber != null) {
                            siSerialNumber = siSerialNumber.substring(scheduleInfo.getShiftCode().length());
                        }

                        String wfvoSerialNumber = wfvo.getSerialNumber();
                        if (siSerialNumber != null && wfvoSerialNumber != null && siSerialNumber.equals(wfvoSerialNumber)) {
                            workflowContentList = wfvo.getWorkflowContentList();
                            break;
                        }
                    }

                    if (workflowContentList != null) {
                        for (WorkflowContent workflowContent : workflowContentList) {
                            Integer startTime = workflowContent.getStartTime();
                            Integer endTime = workflowContent.getEndTime();
                            String start = "";
                            String end = "";
                            if (startTime != null && endTime != null) {
                                start = DateUtil.minuToTime2(startTime);
                                end = DateUtil.minuToTime2(endTime);
                            }
                            String content = workflowContent.getContent();
                            Integer rowsNum = workflowContent.getRowsNum();
                            if (rowsNum != null && rowsNum == 0) {
                                sb.append(start + "-" + end + " " + content + "  ");
                            } else if (rowsNum != null && rowsNum == 1) {
                                sb1.append(start + "-" + end + " " + content + "  ");
                            }
                        }
                    }

                    Paragraph paragraph = new Paragraph(sb.toString(), PdfUtil.getPdfChineseFont());
                    pdfCell.setPhrase(paragraph);
                    //占1行5列
                    pdfCell.setRowspan(1);
                    pdfCell.setColspan(5);
                    table.addCell(pdfCell);

                    Paragraph paragraph1 = new Paragraph(sb1.toString(), PdfUtil.getPdfChineseFont());
                    pdfCell1.setPhrase(paragraph1);
                    //占1行5列
                    pdfCell1.setRowspan(1);
                    pdfCell1.setColspan(5);
                    table.addCell(pdfCell1);
                } else {
                    Paragraph paragraph = new Paragraph("今日休息", PdfUtil.getPdfChineseFont());
                    pdfCell.setPhrase(paragraph);
                    //占1行5列
                    pdfCell.setRowspan(1);
                    pdfCell.setColspan(5);
                    table.addCell(pdfCell);

                    Paragraph paragraph1 = new Paragraph("无注意事项", PdfUtil.getPdfChineseFont());
                    pdfCell1.setPhrase(paragraph1);
                    //占1行5列
                    pdfCell1.setRowspan(1);
                    pdfCell1.setColspan(5);
                    table.addCell(pdfCell1);
                }
            }
            try {
                document.add(table);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.close();

            baos.writeTo(zip);
            baos.close();
        }

        zip.flush();
        zip.close();
    }

}
