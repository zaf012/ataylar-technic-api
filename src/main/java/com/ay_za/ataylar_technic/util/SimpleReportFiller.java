package com.ay_za.ataylar_technic.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleReportFiller {

  private String reportFileName;

  private JasperReport jasperReport;

  private JasperPrint jasperPrint;

  private JRBeanCollectionDataSource dataSource;

  private Map<String, Object> parameters;

  public SimpleReportFiller(
      String reportFileName,
      JRBeanCollectionDataSource dataSource,
      Map<String, Object> parameters) {
    this.reportFileName = reportFileName;
    this.dataSource = dataSource;
    this.parameters = parameters;
  }

  public void prepareReport() {
    this.compileReport();
    this.fillReport();
  }

  public void compileReport() {
    try (InputStream reportStream = getClass().getResourceAsStream("/jasper-reports/".concat(reportFileName))) {

      if (reportStream == null) {
        String errorMsg = "JRXML dosyası bulunamadı: /jasper-reports/" + reportFileName;
        Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.SEVERE, errorMsg);
        throw new RuntimeException(errorMsg);
      }

      Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.INFO,
          "JRXML dosyası bulundu, derleniyor: " + reportFileName);

      jasperReport = JasperCompileManager.compileReport(reportStream);

      if (jasperReport == null) {
        String errorMsg = "JasperReport derlenemedi: " + reportFileName;
        Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.SEVERE, errorMsg);
        throw new RuntimeException(errorMsg);
      }

      Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.INFO,
          "JRXML başarıyla derlendi: " + reportFileName);

    } catch (JRException ex) {
      String detailedError = "JRXML derleme hatası: " + reportFileName +
          "\nHata mesajı: " + ex.getMessage() +
          "\nHata sınıfı: " + ex.getClass().getName();

      if (ex.getCause() != null) {
        detailedError += "\nAlt hata sınıfı: " + ex.getCause().getClass().getName();
        detailedError += "\nAlt hata mesajı: " + ex.getCause().getMessage();

        // Stack trace'i string olarak alalım
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        ex.getCause().printStackTrace(pw);
        detailedError += "\n\nTam Stack Trace:\n" + sw.toString();
      }

      Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.SEVERE, detailedError, ex);

      // Console'a da yazdır
      System.err.println("=== JASPER REPORTS DERLEME HATASI ===");
      System.err.println(detailedError);
      System.err.println("=====================================");

      throw new RuntimeException("JRXML derleme hatası: " + ex.getMessage(), ex);
    } catch (Exception ex) {
      String detailedError = "JRXML okuma/derleme sırasında beklenmeyen hata: " + reportFileName;
      Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.SEVERE, detailedError, ex);

      System.err.println("=== BEKLENMEYEN HATA ===");
      ex.printStackTrace();
      System.err.println("========================");

      throw new RuntimeException(detailedError + ": " + ex.getMessage(), ex);
    }
  }

  public void fillReport() {
    try {
      if (jasperReport == null) {
        throw new RuntimeException("JasperReport null - önce compileReport() çağrılmalı");
      }
      jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    } catch (JRException ex) {
      Logger.getLogger(SimpleReportFiller.class.getName()).log(Level.SEVERE,
          "Rapor doldurma hatası", ex);
      throw new RuntimeException("Rapor doldurma hatası: " + ex.getMessage(), ex);
    }
  }

  public String getReportFileName() {
    return reportFileName;
  }

  public void setReportFileName(String reportFileName) {
    this.reportFileName = reportFileName;
  }

  public JasperReport getJasperReport() {
    return jasperReport;
  }

  public void setJasperReport(JasperReport jasperReport) {
    this.jasperReport = jasperReport;
  }

  public JasperPrint getJasperPrint() {
    return jasperPrint;
  }

  public void setJasperPrint(JasperPrint jasperPrint) {
    this.jasperPrint = jasperPrint;
  }

  public JRBeanCollectionDataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(JRBeanCollectionDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }
}
