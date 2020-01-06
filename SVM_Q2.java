package lsda3;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.regression.LabeledPoint;

import scala.Tuple2;

public final class SVM_Q2 
{
	// Define compile pattern for separators like TAB and SPACE
	private static final Pattern TAB = Pattern.compile("\t");
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void main(String[] args) throws Exception 
	{
		// Turn off INFO Logging by Spark.
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);

		System.out.println("***********************Computing Q2 Result***************************");
		// Setting configuration for Spark to use 4 cores of processor and 6 Giga bytes of memory
		SparkConf sparkConf = new SparkConf().setAppName("SVM Algorthim").setMaster("local[4]").set("spark.executor.memory", "6g");

		// Declare Java version of SparkContext that returns JavaRDDs and works with Java collections
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);

		// Read the text file from specified location
		JavaRDD<String> data = ctx.textFile(".\\src\\lsda3\\imdb_labelled.txt");

		// Convert string data input to LabeledPoint - Function call to parseLabeledPoint
		JavaRDD<LabeledPoint> parsedData = parseLabeledPoint(data);

		// Split RDD data into two sets - 60% training data, 40% testing data.
		JavaRDD<LabeledPoint> training = parsedData.sample(false, 0.6, 11L);

		// store the training data in cache memory
		training.cache();

		// subtract the training data from RDD data to get test data
		JavaRDD<LabeledPoint> test = parsedData.subtract(training);

		int iterations = 300;

		// Run training algorithm for specified number of iterations to build the model.
		SVMModel model = SVMWithSGD.train(training.rdd(), iterations);

		// Clear the default threshold.
		model.clearThreshold();

		// Map predicted values for each feature with the label in test set.
		JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test.map(p -> new Tuple2<>(model.predict(p.features()), p.label()));

		System.out.println("PART A:\n");
		System.out.println("Test data prediction values for 40% test data: ");
		// Print labels of the predicted value
		scoreAndLabels.foreach(f -> {System.out.println(f._2);});
		
		// Get evaluation metrics.
		BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));

		// Calculate Area Under ROC
		double auROC = metrics.areaUnderROC();

		System.out.println("\nPART B");
		System.out.println("\nAREA UNDER ROC (AUROC) : " + auROC);

		// release memory and processors by stopping and closing JavaSparkContext
		ctx.stop();
		ctx.close();
	}

	// Function to convert String data to LabeledPoint RDD
	public static JavaRDD<LabeledPoint> parseLabeledPoint(JavaRDD<String> data)
	{
		// Define HashingTF to convert set of terms into fixed-length feature vectors
		final HashingTF tf = new HashingTF(10000);

		// Split each row by TAB and process the line
		JavaRDD<LabeledPoint> parsedData = data.map(line -> {
			String[] features = TAB.split(line);
			// Split Features[0] - text input by space and transform to vector
			// features[1] contains the labels
			return new LabeledPoint(Double.parseDouble(features[1]), tf.transform(Arrays.asList(SPACE.split(features[0]))));
		});
		return parsedData;
	}
}