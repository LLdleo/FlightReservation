
package driver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

import airport.Airport;
import airport.Airports;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import dao.ServerInterface;

import leg.SeatTypeEnum;
import org.apache.commons.cli.*;
import reservation.ServerLockException;
import reservation.Trip;
import search.*;
import utils.Saps;

/**
 * Old command line entry point to the system used for the prototype.
 *
 * @author Jackson Powell
 * @version 1.0
 * @since 2020-03-12
 */
public class Driver {
    private static search.Flights availableFlights;
    private static search.Flights displayedFlights;
    private static boolean isRoundTrip;
    private static boolean hasOutgoingFlightBeenChosen;
    private static Scanner scan = new Scanner(System.in);
    private static SearchCriteria criteria;
    private static FilterCriteria filterCriteria;
    private static LocalDate returnDate;
    private static boolean isRetDep;
    private static search.Flight selectedOutgoingFlight;
    private static SeatTypeEnum outSeatType;

    /**
     * Entry point for CS509 sample code driver
     * <p>
     * This driver will provide a command-line interface for the system.
     */
    public static void main(String[] args) {
        while (true) { // Loop through everything. Each loop is one complete interaction.
            System.out.println("Select your next action: search, filter, sort, reserve, or restart");
            String answer = scan.nextLine();
            switch (answer.toLowerCase()) {
                case "search":
                    availableFlights = search();
                    if(availableFlights != null) {
                        if (isRoundTrip && hasOutgoingFlightBeenChosen) {
                            Flights inter = availableFlights;
                            availableFlights = new Flights();
                            availableFlights.addAll(availableFlights.stream().filter(flight -> flight.getDepartureTime().getGmtTime().isAfter(selectedOutgoingFlight.getArrivalTime().getGmtTime())).collect(Collectors.toList()));
                        }
                        LocalTime start = LocalTime.of(0, 0);
                        LocalTime end = LocalTime.of(23, 59);
                        filterCriteria = new FilterCriteria(SeatTypeEnum.COACH, start, end, start, end);
                        displayedFlights = availableFlights.filter(filterCriteria);
                        if (displayedFlights.size() > 0)
                            System.out.println(displayedFlights);
                        else {
                            if (availableFlights.size() > 0)
                                System.out.println("No flights match your search criteria that have coach seats available (some flights have first class seating available), please try again with different criteria or try filtering by first class seating.");
                            else {
                                System.out.println("No flights match your search criteria, please try again with different criteria");
                                availableFlights = null;
                            }
                        }
                    }
                    break;
                case "filter":
                    if (availableFlights != null) {
                        displayedFlights = filter();
                        if(displayedFlights.size() > 0)
                        	System.out.println(displayedFlights);
                        else{
							System.out.println("No flights match your filter criteria, please try again with different criteria.");
						}
                    } else {
                        System.out.println("You must search first.");
                    }
                    break;
                case "sort":
                    if (availableFlights != null) {
                        System.out.println("Which sort would you like to perform? \nprice, travel time, arrival time, or departure time");
                        SortTypeEnum sortType = null;
                        while (sortType == null) {
                            String input = scan.nextLine();
                            switch (input.toLowerCase()) {
                                case "price":
                                    sortType = filterCriteria.getSeatType() == SeatTypeEnum.COACH ? SortTypeEnum.COACH_PRICE : SortTypeEnum.FIRST_CLASS_PRICE;
                                    break;
                                case "travel time":
                                    sortType = SortTypeEnum.TRAVEL_TIME;
                                    break;
                                case "arrival time":
                                    sortType = SortTypeEnum.ARR_TIME;
                                    break;
                                case "departure time":
                                    sortType = SortTypeEnum.DEP_TIME;
                                    break;
                                default:
                                    System.out.println("Must input one of:\nprice, travel time, arrival time, or departure time");
                            }
                        }
                        boolean keepGoing = true;
                        boolean isAscending = true;
                        while (keepGoing) {
                            System.out.println("Would you like the list to be sorted in ascending order(y/n)");
                            String in = scan.nextLine();
                            if (in.equalsIgnoreCase("y")) {
                                keepGoing = false;
                            } else if (in.equalsIgnoreCase("n")) {
                                isAscending = false;
                                keepGoing = false;
                            } else {
                                System.out.println("You did not enter y or n. You entered: " + in);
                            }
                        }
                        availableFlights.sort(sortType, isAscending);
                        displayedFlights = availableFlights.filter(filterCriteria);
                        System.out.println(displayedFlights);
                    } else {
                        System.out.println("You must search first.");
                    }
                    break;
                case "reserve":
                    if (availableFlights != null) {
                        System.out.println("Which flight would you like to reserve a seat on. Your seat setting is currently " +
                                filterCriteria.getSeatType().toString() + ". If you would like to change it, then enter anything outside the range of flights and " +
                                "then filter by a different seat type. To reserve a seat, enter the flight number displayed with each flight.");
                        System.out.println("Which flight would you like to reserve?");
                        String input = scan.nextLine();
                        try {
                            Integer index = Integer.parseInt(input);
                            if (index < 0 || index >= displayedFlights.size()) {
                                System.out.println("You did not enter a valid flight so it has not been reserved");
                            } else {
                                if (isRoundTrip) {
                                    if (hasOutgoingFlightBeenChosen) {
                                        search.Flight toReserve = displayedFlights.get(index);
                                        System.out.println("Are you sure (y/n) you would like to reserve a " + filterCriteria.getSeatType().toString() +
                                                " seat on each leg of the return flight: \n" + toReserve.toString() +
                                                "\n and a " + outSeatType.toString() + " seat on each leg of the outgoing flight: \n" + selectedOutgoingFlight.toString());
                                        if (scan.nextLine().equalsIgnoreCase("y")) {
                                            Trip toReserveTrip = new Trip(selectedOutgoingFlight.convertBack(), toReserve.convertBack(), outSeatType, filterCriteria.getSeatType());
                                            boolean keepGoing = true;
                                            while (keepGoing) {
                                                try {
                                                    boolean success = toReserveTrip.reserveSeats();
                                                    keepGoing = false;
                                                    if (success) {
                                                        System.out.println("Your reservation was successful");
                                                        Driver.availableFlights = null;
                                                        Driver.displayedFlights = null;
                                                        Driver.hasOutgoingFlightBeenChosen = false;
                                                        Driver.isRoundTrip = false;
                                                        criteria = null;
                                                        filterCriteria = null;
                                                        returnDate = null;
                                                        isRetDep = false;
                                                        selectedOutgoingFlight = null;
                                                        outSeatType = null;
                                                    } else {
                                                        System.out.println("Your reservation was successful. Please search again to refresh your results for current availability.");
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println(e.toString());
                                                    System.out.println("Would you like to try again? (y/n)");
                                                    keepGoing = scan.nextLine().equalsIgnoreCase("y");
                                                }
                                            }
                                        } else {
                                            System.out.println("You have stopped trying to reserve a flight");
                                        }
                                    } else {
                                        search.Flight toselect = displayedFlights.get(index);
                                        System.out.println("Are you sure (y/n) you would like to select a " + filterCriteria.getSeatType().toString() +
                                                " seat on each leg for the outgoing flight: \n" + toselect.toString());
                                        if (scan.nextLine().equalsIgnoreCase("y")) {
                                            hasOutgoingFlightBeenChosen = true;
                                            outSeatType = filterCriteria.getSeatType();
                                            selectedOutgoingFlight = toselect;
                                            System.out.println("You have successfully selected the outgoing flight. The search for the return flight has begun and will take approximately 8 seconds");
                                            boolean keepGoing = true;
                                            while (keepGoing) {
                                                try {
                                                    criteria = new SearchCriteria(criteria.getArrivalAirportCode(), criteria.getDepartureAirportCode(), returnDate, isRetDep);
                                                    SearchOneWayTripFlights searcher = new SearchOneWayTripFlights(criteria);
                                                    availableFlights = new Flights();
                                                    availableFlights.addAll(searcher.search().stream().filter(flight -> flight.getDepartureTime().getGmtTime().isAfter(selectedOutgoingFlight.getArrivalTime().getGmtTime())).collect(Collectors.toList()));
                                                    LocalTime start2 = LocalTime.of(0, 0);
                                                    LocalTime end2 = LocalTime.of(23, 59);
                                                    filterCriteria = new FilterCriteria(SeatTypeEnum.COACH, start2, end2, start2, end2);
                                                    displayedFlights = availableFlights.filter(filterCriteria);
                                                    System.out.println(displayedFlights);
                                                    keepGoing = false;
                                                } catch (Exception e) {
                                                    System.out.println("There was an issue searching for return flights. Would you like to try again (y/n)");
                                                    keepGoing = scan.nextLine().equalsIgnoreCase("y");
                                                }
                                            }
                                        } else {
                                            System.out.println("You have stopped trying to select a flight");
                                        }
                                    }
                                } else {
                                    search.Flight toReserve = displayedFlights.get(index);
                                    System.out.println("Are you sure (y/n) you would like to reserve a " + filterCriteria.getSeatType().toString() +
                                            " seat on each leg of the flight: \n" + toReserve.toString());
                                    if (scan.nextLine().equalsIgnoreCase("y")) {
                                        Trip toReserveTrip = new Trip(toReserve.convertBack(), filterCriteria.getSeatType());
                                        boolean keepGoing = true;
                                        while (keepGoing) {
                                            try {
                                                boolean success = toReserveTrip.reserveSeats();
                                                keepGoing = false;
                                                if (success) {
                                                    System.out.println("Your reservation was successful");
                                                } else {
                                                    System.out.println("Your reservation was not successful. Please search again to refresh your results for current availability.");
                                                }
                                            } catch (Exception e) {
                                                System.out.println(e.toString());
                                                System.out.println("Would you like to try again? (y/n)");
                                                keepGoing = scan.nextLine().equalsIgnoreCase("y");
                                            }
                                        }
                                    } else {
                                        System.out.println("You have stopped trying to reserve a flight");
                                    }
                                }
                            }
                        }
                        catch (Exception e){
                            System.out.println("There was an issue processing your request. Please try again with different input.");
                        }
                    } else {
                        System.out.println("You must search first.");
                    }
                    break;
                case "restart":
                    Driver.availableFlights = null;
                    Driver.displayedFlights = null;
                    Driver.hasOutgoingFlightBeenChosen = false;
                    Driver.isRoundTrip = false;
                    criteria = null;
                    filterCriteria = null;
                    returnDate = null;
                    isRetDep = false;
                    selectedOutgoingFlight = null;
                    outSeatType = null;
                    break;
                default:
                    System.out.println("Must choose one of search, filter, sort, reserve, or restart");
            }
        }
    }

    /**
     * Get user input for search parameters and return results.
     *
     * @return search results based on user input.
     */
    private static Flights search() {
    	filterCriteria = null;
        System.out.println("Available airports:");
        try {
            Airports airports = ServerInterface.INSTANCE.getAirports(Saps.TEAMNAME);
            for (Airport air : airports) {
                System.out.println(air.toString());
            }
            Options searchOptions = new Options();

            Option depAirportOption = new Option("d", "depAirport", true, "departure airport code");
            depAirportOption.setRequired(true);
            searchOptions.addOption(depAirportOption);

            Option arrAirportOption = new Option("a", "arrAirport", true, "arrival airport code");
            arrAirportOption.setRequired(true);
            searchOptions.addOption(arrAirportOption);

            Option dayOption = new Option("t", "date", true, "date of departure or arrival (ex. 2020_05_10");
            dayOption.setRequired(true);
            searchOptions.addOption(dayOption);

            Option day2Option = new Option("r", "returnDate", true, "date of departure or arrival for return flight(ex. 2020_05_10");
            day2Option.setRequired(false);
            searchOptions.addOption(day2Option);

            Option listTypeOption = new Option("l", "listType", true, "departing|arriving");
            listTypeOption.setRequired(true);
            searchOptions.addOption(listTypeOption);

            Option listTypeOption2 = new Option("rl", "returnListType", true, "departing|arriving for date 2");
            listTypeOption2.setRequired(false);
            searchOptions.addOption(listTypeOption2);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd = null;

            boolean keepGoing = true;
            formatter.printHelp("utility-name", searchOptions);
            while (keepGoing) {

                String[] args= scan.nextLine().split(" ");
                try {
                    cmd = parser.parse(searchOptions, args);
                    keepGoing = false;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                    formatter.printHelp("utility-name", searchOptions);
                }
            }
            String depAir = cmd.getOptionValue("depAirport").toUpperCase();
            String arrAir = cmd.getOptionValue("arrAirport").toUpperCase();
            String date = cmd.getOptionValue("date");
            String listType = cmd.getOptionValue("listType");
            Airport depAirport = airports.getAirportByCode(depAir);
            Airport arrAirport = airports.getAirportByCode(arrAir);
            LocalDate outDate = getDateFromString(date);
            boolean isOutDep = listType.equalsIgnoreCase("departing");
            if (cmd.hasOption("returnDate") && cmd.hasOption("returnListType")) {
                String retDate = cmd.getOptionValue("returnDate");
                String retList = cmd.getOptionValue("returnListType");
                LocalDate ret = getDateFromString(retDate);
                if(ret.isBefore(outDate)){
                    System.out.println("Return date cannot occur before date of outgoing flight. Please try different search criteria");
                    return null;
                }
                isRetDep = retList.equalsIgnoreCase("departing");
                isRoundTrip = true;
                returnDate = ret;
            }
            if (depAirport != null && arrAirport != null && outDate != null) {
                criteria = new SearchCriteria(depAir, arrAir, outDate, isOutDep);
                SearchOneWayTripFlights search = new SearchOneWayTripFlights(criteria);
                System.out.println("Your search has been started.");
                return search.search();
            }
            System.out.println("There was an issue processing your request. Please restart");
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

        return null;
    }

    /**
     * Get user input for filter criteria and return which flights match this criteria.
     *
     * @return flights that match the user-input filter criteria.
     */
    private static Flights filter() {
        Options filterOptions = new Options();

        Option startDepartureOption = new Option("sd", "startDeparture", true, "start of departure time (ex. 00:00");
        startDepartureOption.setRequired(false);
        filterOptions.addOption(startDepartureOption);

        Option startArrivalOption = new Option("sa", "startArrival", true, "start of arrival time (ex. 23.59)");
        startArrivalOption.setRequired(false);
        filterOptions.addOption(startArrivalOption);

        Option endDepartureOption = new Option("ed", "endDeparture", true, "end of departure time (ex. 23:59");
        endDepartureOption.setRequired(false);
        filterOptions.addOption(endDepartureOption);

        Option endArrivalOption = new Option("ea", "endArrival", true, "end of arrival time (ex. 23:59");
        endArrivalOption.setRequired(false);
        filterOptions.addOption(endArrivalOption);

        Option seatTypeOption = new Option("st", "seatType", true, "coach|first_class");
        seatTypeOption.setRequired(false);
        filterOptions.addOption(seatTypeOption);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        formatter.printHelp("utility-name", filterOptions);
        boolean keepGoing = true;
        while (keepGoing) {
            String[] args = scan.nextLine().split(" ");
            try {
                cmd = parser.parse(filterOptions, args);
                keepGoing = false;
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                formatter.printHelp("utility-name", filterOptions);
            }
        }

        LocalTime sat;
        LocalTime sdt;
        LocalTime eat;
        LocalTime edt;
        SeatTypeEnum seatTypeEnum;
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 59);

        if (cmd.hasOption("startArrival")) {
            String sa = cmd.getOptionValue("startArrival");
            sat = getTimeFromString(sa, start);
        } else {
            sat = start;
        }
        if (cmd.hasOption("startDeparture")) {
            String sd = cmd.getOptionValue("startDeparture");
            sdt = getTimeFromString(sd, start);
        } else {
            sdt = start;
        }
        if (cmd.hasOption("endArrival")) {
            String ea = cmd.getOptionValue("endArrival");
            eat = getTimeFromString(ea, end);
        } else {
            eat = end;
        }
        if (cmd.hasOption("endDeparture")) {
            String ed = cmd.getOptionValue("endDeparture");
            edt = getTimeFromString(ed, end);
        } else {
            edt = end;
        }
        if (cmd.hasOption("seatType")) {
            String st = cmd.getOptionValue("seatType");
            seatTypeEnum = st.equalsIgnoreCase("first_class") ? SeatTypeEnum.FIRSTCLASS : SeatTypeEnum.COACH;
        } else {
            if (filterCriteria != null){
                seatTypeEnum = filterCriteria.getSeatType();
            }
            else{
                seatTypeEnum = SeatTypeEnum.COACH;
            }
        }
        filterCriteria = new FilterCriteria(seatTypeEnum, sdt, edt, sat, eat);
        return availableFlights.filter(filterCriteria);
    }

    private static LocalDate getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Saps.SERVER_DATE_FORMAT);
        return LocalDate.parse(date, formatter);
    }

    /**
     * Return the LocalTime associated with a string in the form of hh:mm.
     *
     * @param time The string representation of the time in the form of hh:mm.
     * @param defaultTime The time to default to if a string is not provided in the valid format.
     * @return The localTime associated with a string or the defaultTime if a string is not valid.
     */
    private static LocalTime getTimeFromString(String time, LocalTime defaultTime) {
        String[] args = time.split(":");
        if (args.length != 2) {
            return defaultTime;
        }try {
            Integer hour = Integer.parseInt(args[0]);
            Integer minute = Integer.parseInt(args[1]);
            return LocalTime.of(hour, minute);
        }
        catch (Exception e){
            return defaultTime;
        }
    }
}
