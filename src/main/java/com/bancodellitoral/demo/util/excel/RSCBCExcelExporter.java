package com.bancodellitoral.demo.util.excel;

import com.bancodellitoral.demo.dto.RSCBC;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Servicio para exportar datos RSCBC a Excel usando Apache POI SXSSF
 */
public class RSCBCExcelExporter {

    private static final int WINDOW_SIZE = 1000; // Filas en memoria
    SXSSFWorkbook workbook;

    /**
     * Genera archivo Excel con los datos RSCBC
     *
     * @param fileName  nombre del archivo a generar
     * @param rscbcList lista de datos RSCBC
     * @throws IOException si hay error en la escritura del archivo
     */
    public void exportToExcel(String fileName, List<RSCBC> rscbcList) throws IOException {
        System.out.println("exportToExcel");
        workbook = new SXSSFWorkbook(WINDOW_SIZE);

        // Configurar compresión de archivos temporales
        workbook.setCompressTempFiles(true);

        SXSSFSheet sheet = workbook.createSheet("Reporte RSCBC");

        // Crear headers
        createHeaders(sheet);

        // Procesar datos
        int rowNum = 1;
        for (RSCBC rscbc : rscbcList) {
            Row row = sheet.createRow(rowNum++);
            populateRow(row, rscbc);

            // Flush cada 5000 registros para liberar memoria
            if (rowNum % 5000 == 0) {
                sheet.flushRows();
                System.out.println("Procesadas " + (rowNum - 1) + " filas");
            }
        }

        System.out.println("Total registros: " + (rowNum - 1));
    }

    public void writeOutputStream(OutputStream outputStream) throws IOException {
        // escribe el outputStream
        workbook.write(outputStream);
        // limpia los archivos temporales
        workbook.dispose();
    }

    /**
     * Crea la fila de encabezados
     */
    private void createHeaders(SXSSFSheet sheet) {
        Row headerRow = sheet.createRow(0);

        String[] headers = {
                "ID",
                "Fecha",
                "ID Línea",
                "ID Producto",
                "Descripción",
                "ID Bodega",
                "Referencia",
                "Talla",
                "Color",
                "Cantidad Ordenada",
                "Precio Sin IVA",
                "Precio Total Con IVA",
                "Total Con IVA",
                "ID Factura",
                "Fecha Entrega",
                "ID Transporte",
                "ID Vendedor",
                "Comentario",
                "Estado Orden",
                "Referencia Transporte",
                "ID Cliente",
                "Nombre Cliente",
                "Dirección Cliente",
                "País Cliente",
                "Ciudad Cliente",
                "ID Tipo Orden",
                "ID Moneda",
                "ID Tipo Pago"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    /**
     * Llena una fila con los datos del objeto RSCBC
     */
    private void populateRow(Row row, RSCBC rscbc) {
        int colNum = 0;
        createCellWithValue(row, colNum++, rscbc.getId());
        createCellWithValue(row, colNum++, rscbc.getIdBodega());
    }

    /**
     * Crea una celda con valor, manejando valores null
     */
    private void createCellWithValue(Row row, int columnIndex, String value) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value : "");
    }
}