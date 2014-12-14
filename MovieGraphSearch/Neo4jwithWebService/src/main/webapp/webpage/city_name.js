var CONTENT_TYPE = "Content-Type";
var RESPONSE_TYPE = "application/json";
var PRE_URL = "http://localhost:8080/cse/movie";
var selectedMovie1;
var selectedMovie2;

function callRestService(){

//Create XMLHttpRequest object (this does not work in IE. For IE to work , we should use ActiveXObject instead of XMLHttpRequest)
var webRequest = new XMLHttpRequest();

var responseInfo;

/* 
open() method parameters:
method - Type of request, it can be GET or POST.
serviceUrl - Url of the webservice or page URL.
isAsync - It will be either 'true' or 'false'. Pass true to make asynchronous web service call.  Add below code if your application calls webservice asynchronously. "onreadystatechange" stores the function that will process the response.
*/

var params = "?movie1="+selectedMovie1+"&movie2="+selectedMovie2;
webRequest.open("GET", PRE_URL+params, true);
webRequest.send();

var html;
var heading = "<h3> Recommended Movies</h3><br/><br/>";
var tableHeader = "<table  id='t01'> <tr><th>Movie</th><th>Similarity Score</th></tr>";
html = heading + tableHeader;

//Callback function for web-service
webRequest.onreadystatechange = function () {
  if (webRequest.readyState == 4 && webRequest.status == 200) {
	var responseType = webRequest.getResponseHeader(CONTENT_TYPE);
	if (responseType.substring(0, RESPONSE_TYPE.length) === RESPONSE_TYPE) {
	//Reeading response from web service when the call is successful
    // safely convert the json string into a javascript object (no eval statements!)
        responseInfo = JSON.parse(webRequest.responseText);
		for (var key in responseInfo) {
			if (responseInfo.hasOwnProperty(key)) {
				var score = responseInfo[key];
				html = html + "<tr><td>"+key+"</td><td>"+score+"</td>";
			}
		}
					
		html = html + "</table> ";
		document.getElementById("recommendations").innerHTML = html;
	}
  }
};
};

function getRecommendedMovies() {
	document.getElementById("recommendations").innerHTML = "";
	var selectBox1 = document.getElementById("movie1");
	var selectBox2 = document.getElementById("movie2");
	selectedMovie1 = selectBox1.options[selectBox1.selectedIndex].value;
	selectedMovie2 = selectBox2.options[selectBox2.selectedIndex].value;
	callRestService();
};