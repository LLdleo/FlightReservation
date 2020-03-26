$(document).ready(function(){
    displayFlights();
});

function displayFlights(){
    $("#flightbody").html("");
    flightList = window.localStorage.getItem("flightList");
    flightLen = flightList.length;
    for(var i = 0; i < flightLen; i++){
        $('#flightbody').append("<tr id='flight"+flightList[i].Number+"'>"
            +"<td>"+flightList[i].Number+"</td>"
            +"<td>"+flightList[i].Airplane+"</td>"
            +"<td>"+flightList[i].DepartureCode+"</td>"
            +"<td>"+flightList[i].DepartureTime+"</td>"
            +"<td>"+flightList[i].ArrivalCode+"</td>"
            +"<td>"+flightList[i].ArrivalTime+"</td>"
        );
    }
}
