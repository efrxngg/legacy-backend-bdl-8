package com.bancodellitoral.demo.util.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class SimpleExcelExporterV2<E> {

    private static final int WINDOW_SIZE = 1000;
    private SXSSFWorkbook workbook;

    public void exportToExcel(List<CxRowMapping<E>> cxRowMapping, List<E> dataList) {
        workbook = new SXSSFWorkbook(WINDOW_SIZE);
        workbook.setCompressTempFiles(true);

        SXSSFSheet sheet = workbook.createSheet("Reporte");

        createHeaders(sheet, cxRowMapping);

        int rowNum = 1;
        for (E item : dataList) {
            Row row = sheet.createRow(rowNum++);
            populateRow(row, item, cxRowMapping);

//            if (rowNum % 5000 == 0) {
//                sheet.flushRows();
//            }
        }
    }

    public void writeOutputStream(OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }

    public void dispose() {
        workbook.dispose();
    }

    private void createHeaders(SXSSFSheet sheet, List<CxRowMapping<E>> headers) {

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i).colName());
        }
    }

    private void populateRow(Row row, E itemRow, List<CxRowMapping<E>> columns) {
        for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
            Cell cell = row.createCell(colIdx);
            Object object = columns.get(colIdx).getCxRValue(itemRow);
            cell.setCellValue(object.toString());
        }
    }
}