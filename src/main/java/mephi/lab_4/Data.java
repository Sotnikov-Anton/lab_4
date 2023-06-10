package mephi.lab_4;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Data {
    private ArrayList<Double> dataX = new ArrayList<>();
    private ArrayList<Double> dataY = new ArrayList<>();
    private ArrayList<Double> dataZ = new ArrayList<>();
    
    public ArrayList<Double> getDataX() {
        return dataX;
    }
    public void setDataX(ArrayList<Double> dataX) {
        this.dataX = dataX;
    }
    public ArrayList<Double> getDataY() {
        return dataY;
    }
    public void setDataY(ArrayList<Double> dataY) {
        this.dataY = dataY;
    }
    public ArrayList<Double> getDataZ() {
        return dataZ;
    }
    public void setDataZ(ArrayList<Double> dataZ) {
        this.dataZ = dataZ;
    }
    
    public double calculateMin(ArrayList<Double> data){
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (Double value : data) {
            ds.addValue(value);
        }
        return ds.getMin();
    }
    
    public double calculateMax(ArrayList<Double> data){
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (Double value : data) {
            ds.addValue(value);
        }
        return ds.getMax();
    }
    
    public double calculateMean(ArrayList<Double> data){
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (Double value : data) {
            ds.addValue(value);
        }
        return ds.getMean();
    }
    
    public double calculateGeometricMean(ArrayList<Double> data){
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (Double value : data) {
            ds.addValue(value);
        }
        return ds.getGeometricMean();
    }
    
    public double calculateSampleSize(ArrayList<Double> data){
        return calculateMax(data) - calculateMin(data);
    }
    
    public double calculateNumberOfItems(ArrayList<Double> data){
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (Double value : data) {
            ds.addValue(value);
        }
        return ds.getN();
    }
    
    public double calculateVariance(ArrayList<Double> data){
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (Double value : data) {
            ds.addValue(value);
        }
        return ds.getVariance();
    }
    
    public double calculateSD(ArrayList<Double> data){
        return Math.sqrt(calculateVariance(data));
    }
    
    public double calculateVarianceСoefficient(ArrayList<Double> data){
        return calculateSD(data) / calculateMean(data);
    }
    
    public double[] calculateConfidenceInterval(ArrayList<Double> data, double alpha){
        NormalDistribution normalDistribution = new NormalDistribution();
        double z = normalDistribution.inverseCumulativeProbability(1.0 - alpha / 2.0);
        double lowerBound = calculateMean(data) - z * calculateSD(data) / Math.sqrt(calculateNumberOfItems(data));
        double upperBound = calculateMean(data) + z * calculateSD(data) / Math.sqrt(calculateNumberOfItems(data));
        return new double[] {lowerBound, upperBound};
    }
    
    public double calculateCovariance(ArrayList<Double> data1, ArrayList<Double> data2){
        double[] x = new double[data1.size()];
        double[] y = new double[data2.size()];
        for (int i = 0; i < data1.size(); i++) {
            x[i] = (Double)data1.get(i);
            y[i] = (Double)data2.get(i);
        }
        return new Covariance().covariance(x, y);
    }
}