import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http'
//import { Observable } from 'rxjs/Observable'

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class FlightsService {

  constructor(private http:HttpClient) { }
  getFlights(airportCode: String, date: String, listType: String){
    console.log('/server/api/v1/flights/?teamname=PoLYmer&airport=' + airportCode + '&listType='+ listType + '&date=' + date.split("-").join("_"));
    return this.http.get('/server/api/v1/flights/?teamname=PoLYmer&airport=' + airportCode + '&listType='+ listType + '&date=' + date.split("-").join("_"));
  }
}
