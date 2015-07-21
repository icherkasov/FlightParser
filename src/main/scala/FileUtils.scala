/**
 * Created by Illya on 21.07.2015.
 */

import java.io._
import java.util.zip.GZIPInputStream

import AirportTaskStarter.{AirportTraffic, FlightRow}
import scala.collection.mutable.ListBuffer
import scala.io.Source


class FileUtils {

  def readFileAsFlightRowList(fileName: String): List[FlightRow] = {
    var flightRowBuffer = new ListBuffer[FlightRow]
    for (line <- Source.fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(fileName)))).getLines().drop(1)) {
      val Array(year, quarter, month, dayOfMonth, dayOfWeek, flDate, origin, dest) = line.split(",").map(_.trim)
      val flightRow = FlightRow(year.toInt, quarter.toInt, month.toInt, dayOfMonth.toInt, dayOfWeek.toInt, flDate, origin, dest)
      flightRowBuffer += flightRow
    }
    return flightRowBuffer.toList
  }

  def writePlanesByAirportToFile(planesToAirportFilePath: String, planesToAirportMap: collection.mutable.LinkedHashMap[String, Int]) = {
    val bw = prepareWriter(planesToAirportFilePath)
    bw.write("Airport,Planes Arrived,\n")
    planesToAirportMap.foreach(x => bw.write(x._1 + "," + x._2 + ",\n"))
    bw.close()
  }

  def writeNotNullTrafficListToFile(notNullAirportsTrafficPath: String, airportsTrafficMap: collection.mutable.Map[String, AirportTraffic]) = {
    val bw = prepareWriter(notNullAirportsTrafficPath)
    bw.write("Airport,Arrived,Leaved,Difference\n")
    airportsTrafficMap.foreach(x => if (x._2.arrived - x._2.leaved != 0) bw.write(x._1 + "," + x._2.arrived + "," + x._2.leaved +","+(x._2.arrived - x._2.leaved)+ ",\n"))
    bw.close()
  }

  private def prepareWriter(filePath: String): BufferedWriter = {
    val file = new File(filePath)
    if (file.exists())
      file.delete()
    val bw = new BufferedWriter(new FileWriter(file, true))
    return bw
  }

}
