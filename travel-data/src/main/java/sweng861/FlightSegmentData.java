package sweng861;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;

/**
 * This class collects data on segments of flights for use in the
 * OutputAccordion. The overall flight info is redundant with FlightData but is
 * necessary to be accessed in the OutputAccordion tables.
 */
public class FlightSegmentData {
  private String departure_location;
  private String arrival_location;
  private String departure_date;
  private String arrival_date;

  public String getDepartureLocation() { return departure_location; }
  public String getArrivalLocation() { return arrival_location; }
  public String getDepartureDate() { return departure_date; }
  public String getArrivalDate() { return arrival_date; }
  // Overall flight info
  private Integer number_of_bookable_seats;

  public Integer getNumberOfBookableSeats() { return number_of_bookable_seats; }

  public FlightSegmentData() {}

  public void grabFlightData(FlightOfferSearch query_data) {
    number_of_bookable_seats = query_data.getNumberOfBookableSeats();
  }

  public void grabFlightSegmentData(SearchSegment segment) {
    departure_location = segment.getDeparture().getIataCode();
    arrival_location = segment.getArrival().getIataCode();
    departure_date = segment.getDeparture().getAt();
    arrival_date = segment.getArrival().getAt();
  }
}
;
