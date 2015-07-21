/**
 * Created by Illya on 20.07.2015.
 */

import java.io._
import java.util.zip.GZIPInputStream
import scala.collection.mutable.ListBuffer
import scala.io.Source

object ScalaAirportTask {
  val sourceZipName: String = "palnes_log.csv.gz"
  var planesToAirportFilePath: String = "Planes arrived to airports.csv"

  case class FlightRow(year: Int, quater: Int, month: Int, dayOfMonth: Int, dayOfWeek: Int, flDate: String, origin: String, dest: String)

  case class AirportTraffic(arrived: Int , leaved: Int)

  def main(args: Array[String]) {
    val flightList: List[FlightRow] = readFileAsFlightRowList(sourceZipName)
   // val planesArrivedByAirportMap = collection.mutable.LinkedHashMap(getPlanesAmountArrivedByAirportMap(flightList).toSeq.sortBy(_._1): _*)
   // writePlanesByAirportToFile(planesArrivedByAirportMap)
    val ariportsTrafficList :collection.mutable.Map[String, AirportTraffic] = getAirportTrafficList(flightList)
    ariportsTrafficList.foreach((e:(String,AirportTraffic )) =>if(e._2.arrived - e._2.leaved !=0) println(e._1+"  "+e._2.arrived+"  "+ e._2.leaved))
  }

  def getAirportTrafficList(flightRowsList: List[FlightRow]) : collection.mutable.Map[String, AirportTraffic]= {
    val airportTrafficMap = collection.mutable.Map[String, AirportTraffic]()
    for (flight <- flightRowsList) {
      if (airportTrafficMap.keySet.contains(flight.dest)) {
        var traffic: AirportTraffic = airportTrafficMap.getOrElse(flight.dest, new AirportTraffic(0,0))
        traffic.arrived += 1
        airportTrafficMap.update(flight.dest, traffic)
      }
      else if (airportTrafficMap.keySet.contains(flight.origin)) {
        var traffic: AirportTraffic = airportTrafficMap.getOrElse(flight.origin, new AirportTraffic(0,0))
        traffic.arrived += 1
        airportTrafficMap.update(flight.origin, traffic)
      }

    }
    return airportTrafficMap
  }

  def readFileAsFlightRowList(fileName: String): List[FlightRow] = {
    var flightRowBuffer = new ListBuffer[FlightRow]
    for (line <- Source.fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(fileName)))).getLines().drop(1)) {
      val Array(year, quarter, month, dayOfMonth, dayOfWeek, flDate, origin, dest) = line.split(",").map(_.trim)
      val flightRow = FlightRow(year.toInt, quarter.toInt, month.toInt, dayOfMonth.toInt, dayOfWeek.toInt, flDate, origin, dest)
      flightRowBuffer += flightRow
    }
    return flightRowBuffer.toList
  }


  def getPlanesAmountArrivedByAirportMap(flightRowsList: List[FlightRow]): collection.mutable.Map[String, Int] = {
    val plainsByAirport = collection.mutable.Map[String, Int]()
    for (flight <- flightRowsList) {
      if (plainsByAirport.keySet.contains(flight.dest)) {
        val plainsAmount: Int = plainsByAirport.getOrElse(flight.dest, 0) + 1
        plainsByAirport.update(flight.dest, plainsAmount)
      }
      else {
        plainsByAirport.update(flight.dest, 1)
      }
    }
    return plainsByAirport
  }

  def writePlanesByAirportToFile(planesToAirportMap: collection.mutable.LinkedHashMap[String, Int]) = {
    val file = new File(planesToAirportFilePath)
    if (file.exists())
      file.delete()
    val bw = new BufferedWriter(new FileWriter(file, true))
    bw.write("Airport;Planes Arrived;\n")
    for ((k, v) <- planesToAirportMap) {
      bw.write(k + ";" + v + ";\n")
    }
    bw.close()
  }

}

