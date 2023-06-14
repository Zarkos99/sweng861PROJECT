package sweng861;

import java.util.ArrayList;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;
/**
 * This class collects overall flight data for organization and use anywhere in
 * the system besides the Output Accordion (CellValueFactory doesn't seem to
 * have the capability to allow for nested object fields without some very ugly
 * code given that I couldn't find a way to functionatize it)
 */
public class FlightData {
  private String origin_location;
  private String destination_location;
  private String flight_id;
  private Integer number_of_bookable_seats;
  private ArrayList<FlightSegmentData> flight_segments;

  public FlightData() { flight_segments = new ArrayList<FlightSegmentData>(); }

  public String getOriginLocation() { return origin_location; }
  public String getDestinationLocation() { return destination_location; }
  public Integer getNumberOfBookableSeats() { return number_of_bookable_seats; }
  public ArrayList<FlightSegmentData> getFlightSegments() {
    return flight_segments;
  }

  public void grabFlightData(FlightOfferSearch query_data) {
    number_of_bookable_seats = query_data.getNumberOfBookableSeats();

    for (SearchSegment segment : query_data.getItineraries()[0].getSegments()) {
      FlightSegmentData segment_data = new FlightSegmentData();
      segment_data.grabFlightSegmentData(segment);
      segment_data.grabFlightData(query_data);
      flight_segments.add(segment_data);
    }

    origin_location = flight_segments.get(0).getDepartureLocation();
    destination_location =
        flight_segments.get(flight_segments.size() - 1).getArrivalLocation();
  }
}
;