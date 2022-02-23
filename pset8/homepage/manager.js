const Activities = [];
const Beaches = [];
let Hotels;


function addActivity(id){
    Activities.push(document.getElementById(id).value)

    document.getElementById(id).disabled = true;
    document.getElementById("activitySelect").innerHTML =  "Activity Selected!";
}

function addBeach(beach, id){
    Beaches.push(beach)

    document.getElementById(id).disabled = true;
    document.getElementById("beachSelect").innerHTML = "Beach Selected!";
}

function setHotels(hotel, id){
    Hotels = hotel;

    document.getElementById(id).disabled = true;
    document.getElementById("hotelSelect").innerHTML = "Hotel Selected!";
}

function getItinerary(){
    let string = "Please input at  one activity, hotel, and beach";
    if (getActivity().length != 0 || getBeach().length != 0 || getHotel() != undefined){
        string = "Itinerary: You will stay at " + getHotel().toString() + " and visit "+getBeach().toString() + ". Activities include: "+ getActivity().toString()
    }

    alert(string)
}


function getActivity(){
    return localStorage.getItem("activity", String(Activities))
}
function getBeach(){
    return localStorage.getItem("beach", String(Beaches))
}
function getHotel(){
    return localStorage.getItem("hotel", String(Hotels))
}


function setActivity(){
    localStorage.setItem("activity", Activities);

}
function setBeach(){
    localStorage.setItem("beach", Beaches);
}
function setHotel(){
    localStorage.setItem("hotel", Hotels);
}