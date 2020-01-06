package lsda3;

import java.util.Arrays;
import java.util.List;

public final class Test_Q1 {
	public static void main(String[] args) throws Exception 
	{
		// Creates a series of MeasurementQ1 object
		Measurement_Q1 m1 = new Measurement_Q1(1, 2.0);
		Measurement_Q1 m2 = new Measurement_Q1(13, 8.1);
		Measurement_Q1 m3 = new Measurement_Q1(12, 9.5);
		// create a list and populate the list
		List<Measurement_Q1> mesearements = Arrays.asList(m1,m2,m3);
		
		// Creates a series of MeasurementQ1 object
		Measurement_Q1 m4 = new Measurement_Q1(3, 8.6);
		Measurement_Q1 m5 = new Measurement_Q1(22, 11.8);
		Measurement_Q1 m6 = new Measurement_Q1(18, 14.5);
		
		// create a list and populate the list
		List<Measurement_Q1> measure = Arrays.asList(m4,m5,m6);
		
		// Creates the WeatherStationQ1 object
		WeatherStation_Q1 WS1 = new WeatherStation_Q1("Galway", mesearements);
		WeatherStation_Q1 WS2 = new WeatherStation_Q1("Leixlip", measure);

		// create a list and populate the list
		WeatherStation_Q1.stations = Arrays.asList(WS1, WS2);

		System.out.println("**************************Computing Q1 Result*****************************");
		// call countTemperature which returns the number of times temperature t has been approximately measured
		long result = WeatherStation_Q1.countTemperature(8.8);
		System.out.println("\nNumber of times temperature 8.8 has been approximately measured :\n" +result);
		System.out.println();
	}
}
