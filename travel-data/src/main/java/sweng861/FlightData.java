package sweng861;

import java.util.ArrayList;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.Itinerary;
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
  private @Getter Double flight_price;
  private @Getter Integer number_of_bookable_seats;
  private @Getter ArrayList<FlightSegmentData> flight_segments;

  /**
   * Creates a new instance of a FlightData object.
   */
  public FlightData() { flight_segments = new ArrayList<FlightSegmentData>(); }

  /**
   * This method parses relevant data from the Amadeus query output
   * (FlightOfferSearch object)
   * @param query_data
   */
  public void grabFlightData(FlightOfferSearch query_data) {
    number_of_bookable_seats = query_data.getNumberOfBookableSeats();
    flight_id = query_data.getId();
    try {
      flight_price = Double.parseDouble(query_data.getPrice().getGrandTotal());
    } catch (NumberFormatException nfe) {
      System.out.println("Received invalid double value from Amadeus query: " +
                         query_data.getPrice().getGrandTotal());
    }

    for (Itinerary itinerary : query_data.getItineraries()) {
      for (SearchSegment segment : itinerary.getSegments()) {
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
}
;