package com.agibank.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.junit.jupiter.api.extension.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportListener implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void beforeAll(ExtensionContext context) {
        if (extent == null) {

            String timestamp = new SimpleDateFormat("dd-MM-yyyy_HHmm").format(new Date());
            String reportPath = "target/reports/Relatorio_" + timestamp + ".html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            
            spark.config().setEncoding("UTF-8");
            spark.config().setReportName("Relatório de Automação - Agibank");
            spark.config().setDocumentTitle("Resultados dos Testes");
           
            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Usuário", System.getProperty("user.name"));
            extent.setSystemInfo("Sistema Operacional", System.getProperty("os.name"));
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        ExtentTest extentTest = extent.createTest(context.getDisplayName());
        test.set(extentTest);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {

            Throwable cause = context.getExecutionException().get();

            test.get().fail("Falha no teste: " + cause.getMessage());
            test.get().fail(cause);
        } else {
            test.get().pass("Teste executado com sucesso.");
        }
        extent.flush(); 
    }
}