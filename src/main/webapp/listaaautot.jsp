<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Listaa autot</title>
</head>
<body>
<table id="listaus">
	<thead>
		<tr>
			<th colspan="5" class="oikealle"><span id="uusiAuto">Lisää uusi auto</span></th>
		</tr>
		<tr>
			<th class="oikealle">Hakusana:</th>
			<th colspan="3"><input type="text" id="hakusana"></th>
			<th><input type="button" value="hae" id="hakunappi"></th>
		</tr>			
		<tr>
			<th>Rekisterinumero</th>
			<th>Merkki</th>
			<th>Malli</th>
			<th>Vuosi</th>
			<th></th>							
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<script>
$(document).ready(function(){
	$("#uusiAuto").click(function() {
		document.location="lisaaauto.jsp";
	});
	
	haeAutot();
	$("#hakunappi").click(function(){		
		haeAutot();
	});
	$(document.body).on("keydown", function(event){
		  if(event.which==13){ //Enteriä painettu, ajetaan haku
			  haeAutot();
		  }
	});
	$("#hakusana").focus();//viedään kursori hakusana-kenttään sivun latauksen yhteydessä
});	

function haeAutot(){
	$("#listaus tbody").empty();
	$.ajax({
		url:"autot/" + $("#hakusana").val(), 
		type:"GET", 
		dataType:"json", 
		success:function(result) {//Funktio palauttaa tiedot json-objektina		
		$.each(result.autot, function(i, field) {  
        	var htmlStr;
        	htmlStr += "<tr id='rivi_"+field.rekno+"'>";
        	htmlStr += "<td>"+field.rekno+"</td>";
        	htmlStr += "<td>"+field.merkki+"</td>";
        	htmlStr += "<td>"+field.malli+"</td>";
        	htmlStr += "<td>"+field.vuosi+"</td>";
        	htmlStr += "<td><span class='poista' onclick=poista('"+field.rekno+"')>Poista</span></td>";
        	htmlStr += "</tr>";
        	$("#listaus tbody").append(htmlStr);
        });	
    }});
}

function poista(rekno){
	if (confirm("Poista auto " + rekno +"?")) {
		$.ajax({
			url:"autot/" + rekno, 
			type:"DELETE", 
			dataType:"json", 
			success:function(result) {
				//result on joko {"response:1"} tai {"response:0"}
				if (result.response == 0) {
	        		$("#ilmo").html("Auton poisto epäonnistui.");
	        	} else if (result.response == 1) {
	        		//Värjätään poistetun asiakkaan rivi
	        		$("#rivi_" + rekno).css("background-color", "red"); 
	        		alert("Auton " + rekno +" poisto onnistui.");
					haeAutot();        	
			}
	    }});
	}
	console.log(rekno);
}
</script>
</body>
</html>