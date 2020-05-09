## PoLYmer Flight Reservation System
### Usage
To use this Flight Reservation System run src.Driver.Driver and then input commands from the command line which is described below.
#### Command Line
On the command line, you will be presented with 4 different actions to choose from: (search, filter, sort, and reserve).

#####Search: 
You must do this action first in order to get search results for the other actions to work on. If you try and use
other functions, the system will redirect you to search. 

When searching you can either search for flights that depart from 
some airport and arrive at another airport based on the date the flights either depart or arrive which you specify.

Dialogs are presented specifying the available airports and how to specify these different parameters which are:

-a arrival airport code

-d departure airport code

-l "departing" if searching based on departure date. "arriving" if searching based on arrival date.

-t The date of the flight in the format yyyy_mm_dd, such as 2020_05_10 for May 10, 2020.

Optionally you can also provide both of the below options to search for a roundtrip:

-r The date of the return flight in the format yyyy_mm_dd, such as 2020_05_10 for May 10, 2020.

-rl "departing" if searching for return flights based on departure date. 
"arriving" if searching for return flights based on arrival date.

After the search criteria is entered you will be told that the search has started. Once it 
finishes (has been around 8 seconds in testing), a list of flights will appear that will
be numbered for any future reference in other actions.

Note: By default your seat type will start as coach. To change this use the filter function next.
#####  Filter
You can use filter to narrow down the flights displayed to you by the time of day window of when
a flight departs and/or arrives. Filtering also allows you to change to seat type for 
reserving a certain type of seat (coach or first class) or sorting on a price for either seat.

To do a filter you can provide any of the below parameters:

-st The seat type mode to change to. Either "coach" or "first_class". Will also filter by 
the availability of that type of seat on each type, such that if a flight does not have
at least one seat left of that type it will not be shown. Defaults to the current seat type
which starts as coach after the initial search.

-sd The start of the departure time window(format is hour:minute, such as 13:00 for 1 pm)
. Will filter for any flights that depart after or at the given time of day. Defaults to 00:00.

-ed The end of the departure time window(format is hour:minute, such as 13:00 for 1 pm)
. Will filter for any flights that depart before or at the given time of day. Defaults to 23:59.

-sa The start of the arrival time window(format is hour:minute, such as 13:00 for 1 pm)
. Will filter for any flights that arrive after or at the given time of day. Defaults to 00:00.

-ea The end of the arrival time window(format is hour:minute, such as 13:00 for 1 pm)
. Will filter for any flights that arrive before or at the given time of day. Defaults to 23:59.

Note: filter does not refresh the availability of the flights as others may reserve while you
look through results.

##### Sort
You can use the sort function to still have the same flights appear, but in a specified order.
The possible orders are ascending/descending of:

Price: Sorts the flights by the price of the current seat type. (To change use the filter action).

Arrival Time: Sorts the flights by the date and time that they arrive at the arrival airport.

Departure TIme: Sorts the flights by the date and time that they depart from the departure airport.

Travel Time: Sorts the flights by the timespan from the flights initial departure to its 
final arrival.

Note: Ascending order for times means that later times are at the end of the list.
##### Reserve
You can use the reserve function to select a currently displayed flight to reserve a seat
of the current seat type on each leg. When you select this action, the system will inform you 
of the currently selected seat type which can be changed using the filter action.

To select a flight, enter the number associated with the flight in the latest list of 
displayed flights. If you had searched for a round trip by using the options -r and -rl 
in the search action, then reserving the first time will select the outgoing trip and then
return search results for possible return flights. Reserving a second time for a round trip
will reserve a seat of the chosen seat types (seat types can be different between flights,
just not legs within a flight) for both flights at once and inform you if it successfully reserve the seats.