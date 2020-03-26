flightList = {};

$(document).ready(function(){
    $("#search").click(function(){
        var airport = $("#departureAirport").val();
        var date = $("#departureDate").val();
        URL = "http://127.0.0.1:8080/?action=list&listType=departing&teamName=PoLYmer&airport="+airport+"&day="+date;
        $.ajax({
            url: URL,
            type: "get",
            dataType: "json",
            async: false,
            data: "",
            success: function(data){
                if(data.statusCode == '200'){
                    flightList = eval(data.body);
                    window.localStorage.setItem("flightList",flightList);
                    window.location.href = "Flights.html";
                    }
                }
        });
    })
});