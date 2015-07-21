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
    val fileUtils : FileUtils = new FileUtils
    val parser :FlightRowsParser = new FlightRowsParser

    val flightList = fileUtils.readFileAsFlightRowList(sourceZipName)

    val planesArrivedByAirportMap = collection.mutable.LinkedHashMap(parser.getPlanesAmountArrivedByAirportMap(flightList).toSeq.sortBy(_._1): _*)
    val airportsTrafficMap = parser.getAirportTrafficList(flightList, planesArrivedByAirportMap)

    fileUtils.writePlanesByAirportToFile(planesToAirportFilePath,planesArrivedByAirportMap)
    fileUtils.writeNotNullTrafficListToFile(notNullAirportsTrafficPath,airportsTrafficMap)
  }










}

