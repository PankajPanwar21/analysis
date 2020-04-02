package com.bbby.spark.analysis.configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfiguration {

@Bean
public JavaSparkContext javaSparkContext()
{
	SparkConf conf = new SparkConf().setAppName("Spark CSV Analysis Demo");
	conf.setMaster("local[5]");
    conf.set("es.nodes", "localhost");
    conf.set("es.port", "9200"); 
    conf.set("es.index.auto.create", "true");
    conf.set("es.nodes.wan.only", "false"); 
    conf.set("spark.es.net.http.auth.user", "");
    conf.set("spark.es.net.http.auth.pass", "");
    conf.set("spark.es.resource", "mp/docs");
    JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
    return javaSparkContext;
	}

@Bean
public SparkSession sparkSession()
{
	final SparkSession sparkSession =
			 SparkSession.builder().appName("Spark CSV Analysis Demo").master("local[5]")
			  .getOrCreate();
	return sparkSession;
}


}
