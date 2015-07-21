/**
 * Created by Illya on 21.07.2015.
 */
import AirportTaskStarter.{AirportTraffic, FlightRow}

class FlightRowsParser {

  def getPlanesAmountArrivedByAirportMap(flightRowsList: List[FlightRow]): collection.mutable.Map[String, Int] = {
    val plainsByAirport = collection.mutable.Map[String, Int]()
    flightRowsList.foreach(x => if (plainsByAirport.keySet.contains(x.dest)) {
      val plainsAmount: Int = plainsByAirport(x.dest) + 1
      plainsByAirport.update(x.dest, plainsAmount)
    }
    else
      plainsByAirport.update(x.dest, 1))
    return plainsByAirport
  }

  def getAirportTrafficList(flightRowsList: List[FlightRow], planesToAirportMap: collection.mutable.LinkedHashMap[String, Int]): collection.mutable.Map[String, AirportTraffic] = {
    val airportTrafficMap = collection.mutable.Map[String, AirportTraffic]()
    var set = scala.collection.mutable.Set[String]()
    planesToAirportMap.foreach(x => set += x._1)
    set.foreach(x => airportTrafficMap += (x -> new AirportTraffic(0, 0)))

    for (flight <- flightRowsList) {
      var trafficValue = airportTrafficMap(flight.dest)
      trafficValue.arrived += 1
      airportTrafficMap.update(flight.dest, trafficValue)
      trafficValue = airportTrafficMap(flight.origin)
      trafficValue.leaved += 1
      airportTrafficMap.update(flight.origin, trafficValue)
    }
    return airportTrafficMap
  }

}
