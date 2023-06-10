package mephi.lab_4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelProvider {
    private File file;
    private XSSFWorkbook wb;

    public ExcelProvider() {file = null;}
    public ExcelProvider(String filename)
    {
        init(filename);
    }
    public void init(String filename)
    {
        setFile(new File(System.getProperty("user.dir") + "\\" + filename));
    }
    public Data run()
    {
        loadFile();
        Data data = readExcel();
        close();
        return data;
    }
    public File getFile()
    {
        return this.file;
    }
    public void setFile(File file)
    {
        this.file = file;
    }
    private void loadFile() {
        try {
            wb = new XSSFWorkbook(file);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void close() {
        try {
            wb.close();
        } catch (IOException e) {
            Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private Data readExcel(){
        XSSFSheet sheet = wb.getSheetAt(7);
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            x.add(sheet.getRow(i).getCell(0).getNumericCellValue());
            y.add(sheet.getRow(i).getCell(1).getNumericCellValue());
            z.add(sheet.getRow(i).getCell(2).getNumericCellValue());
        }
        Data data = new Data();
        data.setDataX(x);
        data.setDataY(y);
        data.setDataZ(z);
        return data;
    }
    
    public void writeToExcel(Data data) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet1 = wb.createSheet("Calculations");
        XSSFRow row1 = sheet1.createRow(0);
        String[] headers = new String[]{"Geometric Mean", "Mean", "Standard Deviation", "Sample Size", "Number of Items", "Variance Coefficient", "Confidence Interval", "Variance", "Maximum", "Minimum"};
        for (int i = 1; i < headers.length + 1; i++) {
            row1.createCell(i).setCellValue(headers[i - 1]);
        }
        XSSFRow row2 = sheet1.createRow(1);
        XSSFRow row3 = sheet1.createRow(2);
        XSSFRow row4 = sheet1.createRow(3);
        row2.createCell(0).setCellValue("X");
        row3.createCell(0).setCellValue("Y");
        row4.createCell(0).setCellValue("Z");
        fillRow(row2, data.getDataX());
        fillRow(row3, data.getDataY());
        fillRow(row4, data.getDataZ());
        XSSFSheet sheet2 = wb.createSheet("Covariance");
        XSSFRow row11 = sheet2.createRow(0);
        XSSFRow row22 = sheet2.createRow(1);
        XSSFRow row33 = sheet2.createRow(2);
        XSSFRow row44 = sheet2.createRow(3);
        row11.createCell(1).setCellValue("X");
        row11.createCell(2).setCellValue("Y");
        row11.createCell(3).setCellValue("Z");
        row22.createCell(0).setCellValue("X");
        row33.createCell(0).setCellValue("Y");
        row44.createCell(0).setCellValue("Z");
        row22.createCell(1).setCellValue(data.calculateCovariance(data.getDataX(), data.getDataX()));
        row22.createCell(2).setCellValue(data.calculateCovariance(data.getDataX(), data.getDataY()));
        row22.createCell(3).setCellValue(data.calculateCovariance(data.getDataX(), data.getDataZ()));
        row33.createCell(1).setCellValue(data.calculateCovariance(data.getDataY(), data.getDataX()));
        row33.createCell(2).setCellValue(data.calculateCovariance(data.getDataY(), data.getDataY()));
        row33.createCell(3).setCellValue(data.calculateCovariance(data.getDataY(), data.getDataZ()));
        row44.createCell(1).setCellValue(data.calculateCovariance(data.getDataZ(), data.getDataX()));
        row44.createCell(2).setCellValue(data.calculateCovariance(data.getDataZ(), data.getDataY()));
        row44.createCell(3).setCellValue(data.calculateCovariance(data.getDataZ(), data.getDataZ()));
        try {
            wb.write(new FileOutputStream(new File(".\\Calculations.xlsx")));
            wb.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void fillRow(XSSFRow row, ArrayList<Double> data){
        Data d = new Data();
        row.createCell(1).setCellValue(d.calculateGeometricMean(data));
        row.createCell(2).setCellValue(d.calculateMean(data));
        row.createCell(3).setCellValue(d.calculateSD(data));
        row.createCell(4).setCellValue(d.calculateSampleSize(data));
        row.createCell(5).setCellValue(d.calculateNumberOfItems(data));
        row.createCell(6).setCellValue(d.calculateVarianceСoefficient(data));
        row.createCell(7).setCellValue("(" + d.calculateConfidenceInterval(data, 0.05)[0] + ";" + d.calculateConfidenceInterval(data, 0.05)[1] + ")");
        row.createCell(8).setCellValue(d.calculateVariance(data));
        row.createCell(9).setCellValue(d.calculateMax(data));
        row.createCell(10).setCellValue(d.calculateMin(data));
    }
}