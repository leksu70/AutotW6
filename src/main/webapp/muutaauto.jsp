<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1"><script src="scripts/main.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Muuta autoa</title>
</head>
<body>
<form id="tiedot">
	<table>
		<thead>

			<tr>
				<th colspan="5" class="oikealle"><span id="takaisin">Takaisin listaukseen</span></th>
			</tr>
			<tr>
				<th>RekNo</th>
				<th>Merkki</th>
				<th>Malli</th>
				<th>Vuosi</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="text" name="rekNo" id="rekNo"></td>
				<td><input type="text" name="merkki" id="merkki"></td>
				<td><input type="text" name="malli" id="malli"></td>
				<td><input type="text" name="vuosi" id="vuosi"></td>
				<td><input type="submit" id="tallenna" value="Hyväksy"></td>
		</tbody>
	</table>
	<input type="hidden" name="vanharekno" id="vanharekno">
</form>
<span id="ilmo"></span>
<script>
$(document).ready(function() {
	$("#takaisin").click(function(){
		document.location="listaaautot.jsp";
	});
	
	// Haetaan muutettavan auton tiedot. Kutsutaan backin GET-metodia
	// GET /autot/haeyksi/rekno
	var rekno = requestURLParam("rekno"); // Funktio löytyy script-hakemistosta
	$.ajax({
		url:"autot/haeyksi/" + rekno,
		type:"GET",
		dataType:"json",
		success:function(result) {
			$("#vanharekno").val(result.rekno);
			$("#rekNo").val(result.rekno);
			$("#merkki").val(result.merkki);
			$("#malli").val(result.malli);
			$("#vuosi").val(result.vuosi);
		}
	});
	
	// Formin tarkastus.
	$("#tiedot").validate({
		rules:	{
			rekNo:	{
				required: true,
				minlength: 3
			},
			merkki:	{
				required: true,
				minlength: 2
			},
			malli:	{
				required: true,
				minlength: 1
			},
			vuosi:	{
				required: true,
				minlength: 4,
				maxlength: 4,
				min: 1900,
				max: new Date().getFullYear() + 1	// Auto voi olla ensivuoden mallia
			}
		},
		messages:	{
			rekNo:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt"
			},
			merkki:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt"
			},
			malli:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt"
			},
			vuosi:	{
				required:	"Puuttuu",
				number:		"Ei kelpaa",
				minlength:	"Liian lyhyt",
				maxlength:	"Liian pitkä",
				min:	"Liian pieni",
				max:	"Liian suuri"
			}
		},
		submitHandler: function(form) {
			paivitaTiedot();
		}
	});
});

// funktio tietojen päivittämistä varten. Kutsutaan backin PUT-metodia ja välitetään kutsun mukana uudet tiedot json-stringinä.
//PUT /autot/
function paivitaTiedot(){
	// muutetaan lomakkeen tiedot json-stringiksi:
	var formJsonStr = formDataJsonStr($("#tiedot").serializeArray());
	$.ajax({
		url:"autot",
		data:formJsonStr, 
		type:"PUT", 
		dataType:"json",
		success:function(result) {	//result on joko {"response:1"} tai {"response:0"}
			if (result.response == 0) {
				$("#ilmo").html("Auton päivittäminen epäonnistui.");
			} else if (result.response == 1) {
				$("#ilmo").html("Auton päivittäminen onnistui.");
				$("#rekNo").val(""); // , "#", "#", "#"
				$("#merkki").val("");
				$("#malli").val("");
				$("#vuosi").val("");
			}
		}
	});
};
</script>
</body>
</html>