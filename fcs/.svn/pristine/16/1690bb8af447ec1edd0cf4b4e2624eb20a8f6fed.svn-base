package com.tcci.fc.util.time;

import java.text.SimpleDateFormat;

public class ProcessTimeHelper {
	private static long count = 0;
	private static long totalTime = 0;
	private static long startTime = 0;
	private static long endTime = 0;

	public static void start(){
		startTime = System.currentTimeMillis();
	}
	
	public static void end(){		
		endTime = System.currentTimeMillis();
	}
	
//	public static void displayProcessTime(){
//		double sec = getProcessTime() / 1000.0d;
//		System.out.println(sec + " sec.");
//	}
//	
//	public static void displayTotalTime(){
//		double sec = getTotalTime() / 1000.0d;
//		System.out.println(sec + "sec.");
//	}
	
//	public static void displayAverageTime(){
//		double sec = getAverageTime() / 1000.0d;
//		System.out.println(sec + " sec.");
//	}
	
	public static long getProcessTime(){
		long time = endTime - startTime;
		
		totalTime += time;
		count++;
		
		return time;
	}
	
	public static long getTotalTime(){
		return totalTime;
	}
	
	public static double getAverageTime(){
		double avg = totalTime / count;
		return avg;
	}
	
	public static void clear(){
		count = 0;
		totalTime = 0;
		startTime = 0;
		endTime = 0;
	}
        public static String getProcessSeconds(){
            long time =getProcessTime();
            String format = "m 'minute(s)' s 'second(s)'";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String stSpendTime = sdf.format(time);
            return stSpendTime;
        }
}
