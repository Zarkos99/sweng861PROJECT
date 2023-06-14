package sweng861;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;

import lombok.Getter;

/**
 * This class collects data on segments of flights for use in the
 * OutputAccordion. The overall flight info is redundant with FlightData but is
 * necessary to be accessed in the OutputAccordion tables.
 */
public class FlightSegmentData {
  private @Getter String departure_location;
  private @Getter String arrival_location;
  private @Getter String departure_date;
  private @Getter String arrival_date;
  private @Getter String flight_price;

  // Overall flight info
  private @Getter Integer number_of_bookable_seats;

  public FlightSegmentData() {}

  public void grabFlightData(FlightOfferSearch query_data) {
    number_of_bookable_seats = query_data.getNumberOfBookableSeats();
    flight_price = query_data.getPrice().getGrandTotal();
  }

  public void grabFlightSegmentData(SearchSegment segment) {
    departure_location = segment.getDeparture().getIataCode();
    arrival_location = segment.getArrival().getIataCode();
    departure_date = segment.getDeparture().getAt();
    arrival_date = segment.getArrival().getAt();
  }
}
;
