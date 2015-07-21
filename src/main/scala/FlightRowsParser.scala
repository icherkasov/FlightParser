/**
 * Created by Illya on 21.07.2015.
 */
import AirportTaskStarter.{AirportTraffic, FlightRow}

class FlightRowsParser {
  def getAirportsNames(flightRows: List[FlightRow]):scala.collection.mutable.Set[String] ={
    println("Getting airports names")
    var set = scala.collection.mutable.Set[String]()
    flightRows.foreach(x => (set += x.origin, set+=x.dest))
    println("Founded "+set.size+" unique airports")
    return set
  }

  def getAirportTrafficList(flightRowsList: List[FlightRow], set : scala.collection.mutable.Set[String]): collection.mutable.Map[String, AirportTraffic] = {
    println("Calculating amount of arrived and leaved planes per airport")
    val airportTrafficMap = collection.mutable.Map[String, AirportTraffic]()
    set.foreach(x => airportTrafficMap += (x -> new AirportTraffic(0, 0)))

    for (flight <- flightRowsList) {
      var trafficValue = airportTrafficMap(flight.dest)
      trafficValue.arrived += 1
      airportTrafficMap.update(flight.dest, trafficValue)
      trafficValue = airportTrafficMap(flight.origin)
      trafficValue.leaved += 1
      airportTrafficMap.update(flight.origin, trafficValue)
    }
    println("Calculation completed")
    return airportTrafficMap
  }

}
