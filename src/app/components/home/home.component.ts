import { Component, OnInit } from '@angular/core';
import { FlightsService} from '../../services/flights.service'
import { AirportsService } from '../../services/airports.service'
import { FormGroup, FormControl, Validators} from '@angular/forms'
import { Observable } from 'rxjs';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public flights;
  airports;//: String[] = ["ATL","ANC","AUS","BWI","BOS","CLT","MDW","ORD","CVG","CLE","CMH","DFW","DEN","DTW","FLL","RSW","BDL","HNL","IAH","HOU","IND","MCI","LAS","LAX","MEM","MIA","MSP","BNA","MSY","JFK","LGA","EWR","OAK","ONT","MCO","PHL","PHX","PIT","PDX","RDU","SMF","SLC","SAT","SAN","SFO","SJC","SNA","SEA","STL","TPA","IAD","DCA"];
  flightForm: FormGroup;
  validMessage: String = "";
  constructor(private flightsService: FlightsService, private airportsService: AirportsService) { }

  ngOnInit() {
    this.getAirports();
    this.flightForm = new FormGroup({
      dateType: new FormControl('departing', Validators.required),
      airport: new FormControl('', Validators.required),
      date: new FormControl('', Validators.required)
    })
  }
  getAirports(){
    this.airportsService.getAirports().subscribe(
      data => { this.airports = data},
      err => console.error(err),
      () => console.log('airports retrieved:')
    );
  }
  searchFlights(){
    if(this.flightForm.valid){
      this.validMessage = "Your search has begun";
      this.flightsService.getFlights(this.flightForm.controls['airport'].value, this.flightForm.controls['date'].value, this.flightForm.controls['dateType'].value).subscribe(
        data => {
          this.flights = data;
          //this.flightForm.reset();
          return true;
        },
        error => {
          return Observable.throw(error);
        }
      )
    }
    else{
      this.validMessage = "Please fill out the form before searching";
    }
  }
}
