import org.apache.spark.sql.SparkSession

object EarthquakeAnalysis {
    def main(args: Array[String]): Unit = {
        // Initialisation
        val spark = SparkSession.builder()
          .appName("Earthquake Analysis")
          .master("local[*]")
          .getOrCreate()

        // Lecture (vérifie bien la majuscule à "Data")
        val df = spark.read
          .option("header", "true")
          .option("inferSchema", "true")
          .csv("Data/dataset-earthquakes-trimmed.csv")

        //
