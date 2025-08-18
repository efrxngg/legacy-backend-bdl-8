package com.bancodellitoral.demo.servlet;

import com.bancodellitoral.demo.dto.RSCBC;
import com.bancodellitoral.demo.service.GenerateExcelReport;
import com.bancodellitoral.demo.util.excel.SimpleExcelExporterV2;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "reporteria", value = "/reporteria")
public class ReporteriaServlet extends HttpServlet {

    @Inject
    private GenerateExcelReport generateExcelReport;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doGet");
        long startTime = System.currentTimeMillis();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=reporte.xlsx");


        try (ServletOutputStream out = response.getOutputStream()) {
            try {
                SimpleExcelExporterV2<RSCBC> excelExporter = generateExcelReport.generate();
                excelExporter.writeOutputStream(out);
                excelExporter.dispose();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("time: " + (System.currentTimeMillis() - startTime));
    }

    public void init() {
    }

    public void destroy() {
    }
}