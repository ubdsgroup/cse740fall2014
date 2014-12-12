package com.ckd.ts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains functions to extract K monthly values which are continuous from a given time series
 * Usage:- $JAVA_HOME/bin/java -cp TimeSeriesExtractor.jar com.ckd.ts.TimeSeriesExtractor [input directory] [output directory] [K]
 * Input Directory path is optional and the default value is "data"
 * Output Directory path is optional and the default value is "result"
 * Value of K is optional and default value is 10.
 * 
 * It assumes input directory to have one file per key with filename as the key,
 * containing lines in the format (Timestamp, value) in each line.
 * 
 * The output file has filename as key and contains a line of K values seperated by comma.
 * 
 * @author biplap
 *
 */
public class TimeSeriesExtractor {
	public static String DATA_DIR = "data";
	public static String RESULT_DIR = "result";
	public static int K = 10;
	
	public static void main(String []args) {
		try {
			if (args.length > 2) {
				K = Integer.parseInt(args[2]);
			}
			
			if (args.length > 1) {
				RESULT_DIR = args[1];
			}
			if (args.length > 0) {
				DATA_DIR = args[0];
			}
			int len = RESULT_DIR.length();
			if (RESULT_DIR.charAt(len-1) != '/'){
				RESULT_DIR += "/";
			}
			
			System.out.println("Data dir:- " + DATA_DIR);
			System.out.println("Result dir:- " + RESULT_DIR);
			
			File dataDir = new File(DATA_DIR);
			File resultDir = new File(RESULT_DIR);
			if (resultDir.exists() == false) {
				resultDir.mkdir();
			}
			File[] files = dataDir.listFiles();
			TimeSeriesExtractor tsExtractor = new TimeSeriesExtractor();
			ArrayList<TimeVal> timeSeries = null;
			ArrayList<TimeVal> kTimeSeries = null;
			for (int i=0;i<files.length;i++) {
				timeSeries = tsExtractor.extractTimeSeriesFromFile(files[i]);
				kTimeSeries = tsExtractor.extractKTimeValue(timeSeries, K);
				if (kTimeSeries.size() < K) {
					continue;
				}
				tsExtractor.writeTimeSeries(kTimeSeries, RESULT_DIR+files[i].getName());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extracts a time series from a given file
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ArrayList<TimeVal> extractTimeSeriesFromFile(File file) throws IOException, ParseException {
		ArrayList<TimeVal> timeSeries = new ArrayList<TimeVal>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		boolean wasLast = false;
		String str = null;
		String []strArr = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (!wasLast) {
			TimeVal tv = new TimeVal();
			str = br.readLine();
			if (str == null) {
				wasLast = true;
				break;
			}
			strArr = str.split(",");
			if (strArr.length < 2) {
				continue;
			}
			Date date = df.parse(strArr[0]);
			float val = Float.parseFloat(strArr[1]);
			tv.setDate(date);
			tv.setValue(val);
			timeSeries.add(tv);
		}
		br.close();
		return timeSeries;
	}
	
	/**
	 * Extracts K continue month values from a given time series.
	 * It creates a window of lenght K months and searches for the window which is most dense
	 * After finding start of the most dense window, it uses most recent reading to fill the value
	 * @param timeSeries
	 * @param k
	 * @return
	 */
	public ArrayList<TimeVal> extractKTimeValue(ArrayList<TimeVal> timeSeries, int k) {
		ArrayList<TimeVal> kTimeSeries = new ArrayList<TimeVal>();
		if (k <= timeSeries.size()) {
			Calendar firstDate = Calendar.getInstance();
			Calendar lastDate = Calendar.getInstance();
			firstDate.setTime(timeSeries.get(0).getDate());
			lastDate.setTime(timeSeries.get(timeSeries.size()-1).getDate());
			
			Calendar windowStart = Calendar.getInstance();
			windowStart.setTime(timeSeries.get(0).getDate());
			Calendar windowEnd = Calendar.getInstance();
			windowEnd.setTime(timeSeries.get(0).getDate());
			windowEnd.add(Calendar.MONTH, k);
			
			int maxWindowStartIndex = 0;
			int maxRecordings = 0;
			int recordings = 0;
			int listCount = 0;
			
			while (windowStart.after(lastDate)==false && windowEnd.after(lastDate)==false) {
				recordings = 0;
				Calendar seriesDate = Calendar.getInstance();
				for (int i=0;i<timeSeries.size();i++) {
					seriesDate.setTime(timeSeries.get(i).getDate());
					if (seriesDate.before(windowStart) == false && seriesDate.after(windowEnd)==false) {
						recordings++;
					}
				}
				if (recordings > maxRecordings) {
					maxRecordings = recordings;
					maxWindowStartIndex = listCount;
				}
				
				listCount++;
				windowStart.setTime(timeSeries.get(listCount).getDate());
				windowEnd.setTime(timeSeries.get(listCount).getDate());
				windowEnd.add(Calendar.MONTH, k);
			}
			
			Calendar maxStart = Calendar.getInstance();
			maxStart.setTime(timeSeries.get(maxWindowStartIndex).getDate());
			TimeVal tv = new TimeVal();
			tv.setDate(timeSeries.get(maxWindowStartIndex).getDate());
			tv.setValue(timeSeries.get(maxWindowStartIndex).getValue());
			
			kTimeSeries.add(tv);
			
			for (int i=1;i<k;i++) {
				maxStart.add(Calendar.MONTH, 1);
				tv = new TimeVal();
				Date current = new Date(maxStart.getTimeInMillis());
				tv.setDate(current);
				
				for (int j=timeSeries.size()-1;j>=0;j--) {
					Date date = timeSeries.get(j).getDate();
					if (current.compareTo(date) >= 0) {
						tv.setValue(timeSeries.get(j).getValue());
						kTimeSeries.add(tv);
						break;
					}
				}
			}
		}
		return kTimeSeries;
	}
	
	/**
	 * Writes the extracted timeseries in a given directory
	 * @param timeSeries
	 * @param file
	 * @throws IOException
	 */
	public void writeTimeSeries(ArrayList<TimeVal> timeSeries, String file) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		StringBuilder sb = new StringBuilder(String.valueOf(timeSeries.get(0).getValue()));
		for (int i=1;i<timeSeries.size();i++) {
			sb.append(",");
			sb.append(String.valueOf(timeSeries.get(i).getValue()));
		}
		bw.write(sb.toString()+"\n");
		bw.close();
	}
}
