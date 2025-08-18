package com.bancodellitoral.demo.service;

import com.bancodellitoral.demo.dto.RSCBC;
import com.bancodellitoral.demo.util.excel.CxRowMapping;
import com.bancodellitoral.demo.repository.PSRepository;
import com.bancodellitoral.demo.util.excel.SimpleExcelExporterV2;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class GenerateExcelReport {

    @Inject
    private PSRepository PSRepository;

    public SimpleExcelExporterV2<RSCBC> generate() throws SQLException, IOException {
        List<RSCBC> list = PSRepository.bigSelect();

        List<CxRowMapping<RSCBC>> cxRowMappings = new LinkedList<>();
        cxRowMappings.add(CxRowMapping.of("ID", RSCBC::getId));
        cxRowMappings.add(CxRowMapping.of("ID BODEGA", RSCBC::getIdBodega));

        SimpleExcelExporterV2<RSCBC> excelExporter = new SimpleExcelExporterV2<>();
        excelExporter.exportToExcel(cxRowMappings, list);

        return excelExporter;
    }

}
