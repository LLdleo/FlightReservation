/**
 * 
 */
package driver;

import java.util.Collections;

import airport.Airport;
import airport.Airports;
import leg.ConnectingLeg;
import leg.ConnectingLegs;
import dao.ServerInterface;

import org.apache.commons.cli.*;

/**
 * @author Lidian Lin
 * @since 2020-03-12
 * @version 1.0
 * @deprecated This entry point does not support current operations search, filter, sort, and reservation operations.
 */
public class Driver {

	/**
	 * Entry point for CS509 sample code driver
	 * 
	 * This driver will retrieve the list of airports from the CS509 server and print the list 
	 * to the console sorted by 3 character airport code
	 * 
	 * @param args is the arguments passed to java vm in format of "CS509.sample teamName" where teamName is a valid team
	 */
	public static void main(String[] args) {
		Options options = new Options();

		// list, resetDB, lockDB, unlockDB, buyTickets
		Option actionOption = new Option("a", "action", true, "action");
		actionOption.setRequired(true);
		options.addOption(actionOption);

		// airports, airplanes, departing, arriving
		Option listTypeOption = new Option("l", "listType", true, "list type");
		listTypeOption.setRequired(true);
		options.addOption(listTypeOption);

		Option teamNameOption = new Option("t", "team", true, "team name");
		teamNameOption.setRequired(true);
		options.addOption(teamNameOption);

		Option airportCodeOption = new Option("c", "airport", true, "airport code");
		airportCodeOption.setRequired(false);
		options.addOption(airportCodeOption);

		Option dayOption = new Option("d", "day", true, "day");
		dayOption.setRequired(false);
		options.addOption(dayOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
		}

		String action = cmd.getOptionValue("action");
		String listType = cmd.getOptionValue("listType");
		String teamName = cmd.getOptionValue("team");
		String airportCode = cmd.getOptionValue("airport");
		String day = cmd.getOptionValue("day");

//		if (args.length != 1) {
//			System.err.println("usage: CS509.sample teamName");
//			System.exit(-1);
//			return;
//		}
		
//		String teamName = args[0];
		// Try to get a list of airports
try {

	if (action.equals("list")) {
		if (listType.equals("airports")) {
			Airports airports = ServerInterface.INSTANCE.getAirports(teamName);
			Collections.sort(airports);
			for (Airport airport : airports) {
				System.out.println(airport.toString());
			}
		} else if (listType.equals("departing") || listType.equals("arriving")) {
			ConnectingLegs connectingLegs = ServerInterface.INSTANCE.getLegs(teamName, listType, airportCode, day);
			Collections.sort(connectingLegs);
			for (ConnectingLeg connectingLeg : connectingLegs) {
				System.out.println(connectingLeg.toString());
			}
		}
	}
}
catch (Exception e){
	e.printStackTrace();
}

	}
}
