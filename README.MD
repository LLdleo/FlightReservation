## PoLYmer Flight Reservation System
### Usage
There are two ways to use this Flight Reservation System(prototype), which includes the function of search airports and search connectingLegs.
#### Command Line
Use class driver.Driver with parameters.  
For example, run: java driver.Driver -a list -l departing -t PoLYmer -c BOS -d 2020_05_10
####  HTTP request
Run entry.HTTPServer, then do http request.  
For example, request: http://127.0.0.1:88/?action=list&listType=departing&teamName=PoLYmer&airport=BOS&day=2020_05_10 