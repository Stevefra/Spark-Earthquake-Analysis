import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object EarthquakeAnalysis {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Earthquake Co-occurrence Analysis")
      .getOrCreate()

    import spark.implicits._

    val path = "gs://bucket-spark-stev/earthquakes.csv"

    try {

      val dfRaw = spark.read.option("header", "true").option("inferSchema", "true").csv(path).repartition(16)

      // pullito
      val dfPulito = dfRaw
        .withColumn("lat_round", round(col("latitude"), 1))
        .withColumn("lon_round", round(col("longitude"), 1))
        .withColumn("solo_data", to_date(col("date")))
        .select("lat_round", "lon_round", "solo_data")
        .dropDuplicates()

      // Analisa
      val dfA = dfPulito.as("dfA")
      val dfB = dfPulito.as("dfB")

      val resultatoFinale = dfA.join(dfB,
          col("dfA.solo_data") === col("dfB.solo_data") && (
            col("dfA.lat_round") < col("dfB.lat_round") ||
              (col("dfA.lat_round") === col("dfB.lat_round") && col("dfA.lon_round") < col("dfB.lon_round"))
            )
        )
        .groupBy(
          col("dfA.lat_round").as("lat1"), col("dfA.lon_round").as("lon1"),
          col("dfB.lat_round").as("lat2"), col("dfB.lon_round").as("lon2")
        )
        .agg(
          count("*").as("frequenza"),
          sort_array(collect_list("dfA.solo_data")).as("date_eventi")
        )
        .orderBy(col("frequenza").desc)

      resultatoFinale.count()
      resultatoFinale.show(100,truncate=false)
    } catch {
      case e: Exception => println(s"ERRORE ${e.getMessage}")
    } finally {
      spark.stop()
    }
  }
}
