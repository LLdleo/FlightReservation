package search;

import leg.SeatTypeEnum;

import java.time.LocalTime;

public class FilterCriteria {
    SeatTypeEnum seatType;
    LocalTime startDep;
    LocalTime endDep;
    LocalTime startArr;
    LocalTime endArr;
    public FilterCriteria (SeatTypeEnum seatType, LocalTime startDep, LocalTime endDep, LocalTime startArr, LocalTime endArr){
        this.seatType = seatType;
        this.endArr =endArr;
        this.endDep = endDep;
        this.startArr = startArr;
        this.startDep = startDep;
    }
}
