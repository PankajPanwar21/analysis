package com.bbby.spark.analysis.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.elasticsearch.spark.streaming.api.java.JavaEsSparkStreaming;
import org.spark_project.guava.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bbby.spark.analysis.configuration.SparkConfiguration;
import com.bbby.spark.analysis.util.Constants;

@Service
public class ConsumerService {

	private BufferedWriter bufferedWriter;
	private static Map<String, Long> orderCountMap = new HashMap<>();
	static {
		orderCountMap.put("totalCount", null);
		orderCountMap.put("salesCount", null);
		orderCountMap.put("returnCount", null);
	}

	@Autowired
	JavaSparkContext javaSparkContext;
	@Autowired
	SparkSession sparkSession;
	

	@KafkaListener(topics = Constants.TOPIC_DATA, groupId = "2")
	public void consumeData(String message) throws IOException {
		System.out.println("received message");
		System.out.println(String.format("#### -> Consumed message -> %s", message));
		// fileConsumer(Constants.FILE_PATH,message);
		doAnalystics(message);
	}

	/*
	 * private void fileConsumer(String filePath,String message) throws IOException
	 * { this.bufferedWriter = new BufferedWriter(new FileWriter(filePath,true));
	 * bufferedWriter.write(message); bufferedWriter.newLine();
	 * bufferedWriter.flush();
	 * 
	 * }
	 */

	private void doAnalystics(String message) {
		final DataFrameReader dataFrameReader = sparkSession.read();
		dataFrameReader.option("header", "false").schema(getHeader());
		final Dataset<Row> sucessfulLoadedTransactionsFrame = dataFrameReader.csv(Constants.FILE_PATH);
		long count = sucessfulLoadedTransactionsFrame.count();
		System.out.println("total succesful transactions loaded :" + count);

		long successfulSales = sucessfulLoadedTransactionsFrame
				.filter((sucessfulLoadedTransactionsFrame.col("txnType").contains("20"))).count();
		System.out.println("total succesful sales  :" + successfulSales);

		long successfulReturn = sucessfulLoadedTransactionsFrame
				.filter((sucessfulLoadedTransactionsFrame.col("txnType").contains("19"))).count();
		System.out.println("total succesful return  :" + successfulReturn);
		Long totVal = orderCountMap.get("totalCount") == null ? orderCountMap.put("totalCount", count)
				: orderCountMap.put("totalCount", orderCountMap.get("totalCount") + count);
		Long salesVal = orderCountMap.get("salesCount") == null ? orderCountMap.put("salesCount", successfulSales)
				: orderCountMap.put("salesCount", orderCountMap.get("salesCount") + successfulSales);
		Long returnVal = orderCountMap.get("returnCount") == null ? orderCountMap.put("returnCount", successfulReturn)
				: orderCountMap.put("returnCount", orderCountMap.get("returnCount") + successfulReturn);

		System.out.println("***************** ORDER ANLYSIS ***************");
		Set<Entry<String, Long>> orderSet = orderCountMap.entrySet();
		orderSet.forEach(k -> {
			System.out.println(k.getKey() + " " + k.getValue());
		});

		
		JavaStreamingContext jssc = new JavaStreamingContext(javaSparkContext, Seconds.apply(60L));
		JavaRDD<Map<String, ?>> javaRDD = javaSparkContext.parallelize(ImmutableList.of(orderCountMap));
		Queue<JavaRDD<Map<String, ?>>> microbatches = new LinkedList<>();
		microbatches.add(javaRDD);
		JavaDStream<Map<String, ?>> javaDStream = jssc.queueStream(microbatches);
		JavaEsSparkStreaming.saveToEs(javaDStream, "shweta/docs");
		jssc.start();
		try {
			jssc.awaitTermination();
		} catch (Exception e) {
		} finally {
			jssc.close();
		}
	}

	private static StructType getHeader() {
		StructType schema = new StructType().add("orderId", "int", false) // false
				.add("businessDate", "String", false) // false
				.add("txnDateTime", "String", false) // false
				.add("txnId", "int", false) // false
				.add("retailerAbbreviation", "String", false) // false
				.add("storeId", "int", false) // false
				.add("cashierId", "int", false) // false
				.add("registerId", "int", false) // false
				.add("tillId", "int", false) // false
				.add("txnType", "int", false) // false
				.add("tenderType", "int", true).add("netAmt", "int", true) // false
				.add("tax1Amt", "int", false) // false
				.add("tax2Amt", "int", false) // false
				.add("tax3Amt", "int", false) // false
				.add("totalDiscountItemAmt", "double", false) // false
				.add("discountTxnAmt", "int", true).add("totalAmt", "double", false) // false
				.add("refOrderId", "int", true).add("refTxnDateTime", "String", true).add("refTxnId", "int", true)
				.add("refStoreId", "int", true).add("refCashierId", "int", true).add("refRegisterId", "int", true)
				.add("totalRingTime", "int", true).add("totalTenderTime", "int", true)
				.add("displayNote", "String", true).add("divisionNo", "int", true).add("scannedInd", "int", true)
				.add("origDivisionNo", "int", true).add("currencyCode", "int", true)
				.add("jurisdictionId", "String", false);
		return schema;
	}

}
