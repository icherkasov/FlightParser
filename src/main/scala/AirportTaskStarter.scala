import java.io.IOException

/**
 * Created by Illya on 20.07.2015.
 */

object AirportTaskStarter {
  val sourceZipName: String = "palnes_log.csv.gz"
  var planesToAirportFilePath: String = "Planes arrived to airports.csv"
  var notNullAirportsTrafficPath: String = "Airports with not null leave-arrive difference.csv"

  case class FlightRow(year: Int, quater: Int, month: Int, dayOfMonth: Int, dayOfWeek: Int, flDate: String, origin: String, dest: String)

  case class AirportTraffic(var arrived: Int, var leaved: Int)

  def main(args: Array[String]) {
    println("*****Starting planes log parser*****")
    val fileUtils: FileUtils = new FileUtils
    val parser: FlightRowsParser = new FlightRowsParser
    try {
      val flightList = fileUtils.readFileAsFlightRowList(sourceZipName)
      val airportsNames = parser.getAirportsNames(flightList)
      val airportsTrafficMap = collection.mutable.LinkedHashMap(parser.getAirportTrafficList(flightList, airportsNames).toSeq.sortBy(_._1): _*)

      fileUtils.writePlanesByAirportToFile(planesToAirportFilePath, airportsTrafficMap)
      fileUtils.writeNotNullTrafficListToFile(notNullAirportsTrafficPath, airportsTrafficMap)
      println("*****Log parser completed. Results saved*****")
    } catch {
      case ioe: IOException => println("Source archive can be missing. Can't proceed. Exception caught: " + ioe);
      case e: Exception => println("Exception caught: " + e);
    }
  }


}

