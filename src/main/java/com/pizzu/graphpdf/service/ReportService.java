package com.pizzu.graphpdf.service;


import com.pizzu.graphpdf.entity.Book;
import com.pizzu.graphpdf.repository.BookRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportService {

    @Autowired
    private BookRepository repository;


    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {

        String path = "C:\\Users\\Pizzuto\\Desktop\\Exercise Training\\Report";

        List<Book> bookList = repository.findAll();

        File file = ResourceUtils.getFile("classpath:LibraryGroupedBy.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bookList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy","Pizzu");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\library.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\library.pdf");
        }

        return "Created in path " + path;
    }
}
