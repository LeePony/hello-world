package com.ztesoft.zsmart.bss.selfcare.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.export.ExportColumn;
import com.ztesoft.zsmart.web.export.ExportConfig;
import com.ztesoft.zsmart.web.export.ExportPage;
import com.ztesoft.zsmart.web.export.interf.IExport;

public class XLSExporter {

    /**
     * ExportConfig对象
     */
    protected ExportConfig config;

    /**
     * HSSFWorkbook对象
     */
    protected HSSFWorkbook wb;

    /**
     * sheet对象
     */
    protected Sheet sheet;

    /**
     * 记录行数
     */
    protected int rowCount = 0;

    /**
     * 规定每个sheet的最大行数
     */
    protected final static int MAX_ROW = 60000;

    /**
     * 输出流
     */
    protected OutputStream os;

    /**
     * 日志对象
     */
    private static ZSmartLogger logger = ZSmartLogger.getLogger(XLSExporter.class);

    /**
     * 初始化
     * 
     * @param config 配置对象
     */
    public void init(ExportConfig config,OutputStream os) {
        this.config = config;
        this.os = os;
    }

    /**
     * 开始输出
     * 
     * @throws BaseAppException if has error
     * @throws IOException 
     */
    public void beginExport(List<DynamicDict> data) throws BaseAppException, IOException {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet();
        Row headerRow = sheet.createRow(0);
        ExportColumn[] columns = this.config.getColumns();
        for (int i = 0; i < columns.length; i++) {
            sheet.setColumnWidth((short) i, (short) (256 * 2 *(columns[i].getColumnWidth())));
            String columnName = columns[i].getColumnName();
            Cell cell = headerRow.createCell(i, Cell.CELL_TYPE_STRING);
            cell.setCellValue(columnName);
        }
        exportOneBatch(data);
        if(os!=null){
            wb.write(os);
        }
    }

    /**
     * 开始导出Excel
     * 
     * @param data 数据集
     * @param pageObject 页面记录
     * @throws BaseAppException if has error
     */
    public void exportOneBatch(List<DynamicDict> data) throws BaseAppException {

        ExportColumn[] columns = this.config.getColumns();
        int currentRowNum = sheet.getLastRowNum() + 1;
        for (DynamicDict rowData : data) {
            if ((rowCount == MAX_ROW)) {
                rowCount = 0;
                sheet = wb.createSheet();
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columns.length; i++) {
                    sheet.setColumnWidth((short) i, (short) (256 * 2 * (columns[i].getColumnWidth())));
                    String columnName = columns[i].getColumnName();
                    Cell cell = headerRow.createCell(i, Cell.CELL_TYPE_STRING);
                    cell.setCellValue(columnName);
                }
                currentRowNum = 1;
            }
            rowCount++;
            Row row = sheet.createRow(currentRowNum++);
            for (int i = 0; i < columns.length; i++) {
                String key = columns[i].getDataKey();
                String cellData = rowData.getString(key);
                Cell cell = null;
                if (columns[i].isNumeric() && StringUtil.isNotEmpty(cellData)) {
                    // 将所有","去掉
                    cellData = cellData.replaceAll(",", "");
                    cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Double.valueOf(cellData));
                    // 如果有小数
                    if (cellData.indexOf(".") != -1) {
                        String formatStr = "0.";
                        for (int j = cellData.indexOf(".") + 1; j < cellData.length(); j++) {
                            formatStr += "0";
                        }
                        CellStyle cellStyle = wb.createCellStyle();
                        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(formatStr));
                        cell.setCellStyle(cellStyle);
                    }
                }
                else {
                    cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    cell.setCellValue(cellData);
                }

            }

        }
    }
}
