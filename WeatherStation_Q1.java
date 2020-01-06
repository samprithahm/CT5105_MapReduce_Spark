package lsda3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class WeatherStation_Q1 implements Serializable
{
	// Universal version identifier for a Serializable class.
	private static final long serialVersionUID = 1L;

	//Setting up class attributes
	private String city;
	public List<Measurement_Q1> measurements;
	public static List<WeatherStation_Q1> stations = new ArrayList<>();

	//Setting up constructor for the WeatherStation_Q1 object
	public WeatherStation_Q1(String city, List<Measurement_Q1> measurements){
		this.city = city;
		this.measurements = measurements;
	}

	//Setting up the setters and getters for the attributes of the object WeatherStation_Q1
	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;        
	}

	//Setting up the setters and getters for the attributes of the object Measurement_Q1
	public void setMeasurements(List<Measurement_Q1> measurements){
		this.measurements = measurements;
	}

	public List<Measurement_Q1> getMeasurements(){
		return measurements;
	}


	public static long countTemperature(double t)
	{
		// Turn off INFO Logging by Spark.
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);

		// Setting configuration for Spark to use 4 cores of processor and 6 Giga bytes of memory
		SparkConf sparkConf = new SparkConf().setAppName("CountTemperature").setMaster("local[4]").set("spark.executor.memory", "6g");

		// Declare Java version of SparkContext that returns JavaRDDs and works with Java collections
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);

		// Create a distributed set of stations in WeatherStationQ1 and copy it to JavaRDD
		JavaRDD<WeatherStation_Q1> tempValues = ctx.parallelize(stations);

		// Flatten the values by iterating over each element in measurements
		JavaRDD<Measurement_Q1> temp = tempValues.flatMap(d -> d.measurements.iterator());

		// map temperature
		JavaDoubleRDD occurrence = temp.mapToDouble(d -> d.getTemperature());
		System.out.println("\nTemperatures measured by any station so far is :\n" +occurrence.collect());
		
		// Filter values which falls between range t-1 to t+1
		JavaDoubleRDD condition = occurrence.filter(x -> (x >= t-1 && x <= t+1));

		// Count the number of occurrence of such temperature
		long output = condition.count();
		
		// release memory and processors by stopping and closing JavaSparkContext
		ctx.stop();
		ctx.close();

		return output;
	}
}