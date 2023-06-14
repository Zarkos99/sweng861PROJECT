package sweng861;

import java.util.ArrayList;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;
import lombok.Getter;
/**
 * This class collects overall flight data for organization and use anywhere in
 * the system besides the Output Accordion (CellValueFactory doesn't seem to
 * have the capability to allow for nested object fields without some very ugly
 * code given that I couldn't find a way to functionatize it)
 */
public class FlightData {
  private @Getter String origin_location;
  private @Getter String destination_location;
  private @Getter String flight_id;
  private @Getter Integer number_of_bookable_seats;
  private ArrayList<FlightSegmentData> flight_segments;

  public FlightData() { flight_segments = new ArrayList<FlightSegmentData>(); }

  public ArrayList<FlightSegmentData> getFlightSegments() {
    return flight_segments;
  }

  public void grabFlightData(FlightOfferSearch query_data) {
    number_of_bookable_seats = query_data.getNumberOfBookableSeats();
    flight_id = query_data.getId();

    for (SearchSegment segment : query_data.getItineraries()[0].getSegments()) {
      FlightSegmentData segment_data = new FlightSegmentData();
      segment_data.grabFlightSegmentData(segment);
      segment_data.grabFlightData(query_data);
      flight_segments.add(segment_data);
    }

    origin_location = flight_segments.get(0).getDeparture_location();
    destination_location =
        flight_segments.get(flight_segments.size() - 1).getArrival_location();
  }
}
;