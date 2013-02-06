<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="de">
    <head>
		<link rel="stylesheet" type="text/css" href="bluedream.css" />
		
		<script>
			function startStopDtntrigger(haveToStop){

				if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp=new XMLHttpRequest();
				}else {// code for IE6, IE5
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
				
				xmlhttp.onreadystatechange=function(){
					if (xmlhttp.readyState==4 && xmlhttp.status==200){
						data = xmlhttp.responseText.split ( "[BRK]" );

						
						
						
						if(data[0]=="abbruch" && data[1]=="ledgreen.png"){
							window.location.reload();
	
						}else if(data[0]=="abbruch" && data[1]=="ledred.png"){
							window.location.reload();

						}else if(data[1]=="ledgreen.png"){
							window.location.reload();
							document.getElementById("dtntriggerStatusText").innerHTML= data[0];
							//document.getElementById("dtntriggerStatusImage").src = data[1];
							//document.getElementById("dtntriggerPid").innerHTML = data[2];
							//document.getElementsByName("startDtntriggerButton")[0].disabled = true;
							//document.getElementsByName("stopDtntriggerButton")[0].disabled = false;
							//document.getElementsByName("driversVersionButton")[0].disabled = true;
							//document.getElementsByName("teamsVersionButton")[0].disabled = true;
							//document.getElementsByName("blacklistDevicesVersionButton")[0].disabled = true;
							//document.getElementsByName("blacklistTagsVersionButton")[0].disabled = true;
							//document.getElementsByName("driverPicsVersionButton")[0].disabled = true;
						}else{
							window.location.reload();
							document.getElementById("dtntriggerStatusText").innerHTML= data[0];
							//document.getElementById("dtntriggerStatusImage").src = data[1];
							//document.getElementById("dtntriggerPid").innerHTML = data[2];
							//document.getElementsByName("startDtntriggerButton")[0].disabled = false;
							//document.getElementsByName("stopDtntriggerButton")[0].disabled = true;
							//document.getElementsByName("driversVersionButton")[0].disabled = false;
							//document.getElementsByName("teamsVersionButton")[0].disabled = false;
							//document.getElementsByName("blacklistDevicesVersionButton")[0].disabled = false;
							//document.getElementsByName("blacklistTagsVersionButton")[0].disabled = false;
							//document.getElementsByName("driverPicsVersionButton")[0].disabled = false;
						}
						
					}
				}
				
				if (haveToStop=="0"){
					xmlhttp.open("GET","start_dtntrigger.php?",true);
				}else{
					xmlhttp.open("GET","stop_dtntrigger.php?",true);
				}
				xmlhttp.send();
			}
		</script>
		
		<!-- ###########################  -->
		<!-- ###########################  -->
		<!-- ###########################  -->
		
		<script>
			function saveVersion(option){

				if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp=new XMLHttpRequest();
				}else {// code for IE6, IE5
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
				
				xmlhttp.onreadystatechange=function(){
					if (xmlhttp.readyState==4 && xmlhttp.status==200){
						data = xmlhttp.responseText.split ( "[BRK]" );
						if(data[0]==1){
							document.getElementById("driversVersion").innerHTML= data[1];
						}else if(data[0]==2){
							document.getElementById("teamsVersion").innerHTML= data[1];
						}else if(data[0]==3){
							document.getElementById("blacklistDevicesVersion").innerHTML= data[1];
						}else if(data[0]==4){
							document.getElementById("blacklistTagsVersion").innerHTML= data[1];
						}else if(data[0]==5){
							document.getElementById("driverPicsVersion").innerHTML= data[1];
						}
					}
				}
				
				
				if (option=="1"){
					new_version = document.getElementById("driversVersionId").value;
					xmlhttp.open("GET","fsg_binary_v2.php?ver="+new_version + "&opt=1",true);   
				}else if (option=="2"){
					new_version = document.getElementById("teamsVersionId").value;
					xmlhttp.open("GET","fsg_binary_v2.php?ver="+new_version + "&opt=2",true);
				}else if (option=="3"){
					new_version = document.getElementById("blacklistDevicesVersionId").value;
					xmlhttp.open("GET","fsg_binary_v2.php?ver="+new_version + "&opt=3",true);
				}else if (option=="4"){
					new_version = document.getElementById("blacklistTagsVersionId").value;
					xmlhttp.open("GET","fsg_binary_v2.php?ver="+new_version + "&opt=4",true);
				}else if (option=="5"){
					new_version = document.getElementById("driverPicsVersionId").value;
					xmlhttp.open("GET","fsg_binary_v2.php?ver="+new_version + "&opt=5",true);
				}
				xmlhttp.send();
			}
		</script>
		
		<!-- ###########################  -->
		<!-- ###########################  -->
		<!-- ###########################  -->
		
		<script>
			function sendDataToAllClients(){

				if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp=new XMLHttpRequest();
				}else {// code for IE6, IE5
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
				
				xmlhttp.onreadystatechange=function(){
					if (xmlhttp.readyState==4 && xmlhttp.status==200){
						data = xmlhttp.responseText;
						document.getElementById("statusSendDataToClients").innerHTML= data;
					}
				}
				
				if(document.getElementById("driversCheckbox").checked){
					drivers = 1;
				}else{
					drivers = 0;
				}
				if(document.getElementById("teamsCheckbox").checked){
					teams = 1;
				}else{
					teams = 0;
				}
				if(document.getElementById("blacklistDevicesCheckbox").checked){
					blacklistDevices = 1;
				}else{
					blacklistDevices = 0;
				}
				if(document.getElementById("blacklistTagsCheckbox").checked){
					blacklistTags = 1;
				}else{
					blacklistTags = 0;
				}
				if(document.getElementById("driverPicsCheckbox").checked){
					driverPics = 1;
				}else{
					driverPics = 0;
				}
				xmlhttp.open("GET","send_to_all_clients.php?d="+drivers + 
					"&t="+teams+ "&bld="+blacklistDevices+ "&blt="+blacklistTags+ "&dp="+driverPics,true); 
				xmlhttp.send();
			}
		</script>
		

		<center><img src="cow200x200.png" alt="Team Kuh"></center>
        <title>FSG Backend</title>
    </head>
    <body>
        <center><h1>FSG Backend</h1></center>
        <center><h2>Android Labor 2012/2013 - feat. cow team</h2></center>


<?php
	//////////////////////////
	// Datenbankverbindung
	//////////////////////////
	$mysql_hostname ="localhost";
	$mysql_username ="fsgbackend";
	$mysql_password ="cowteam12345";
	$mysql_database ="fsgbackend";
	$db = mysql_connect($mysql_hostname, $mysql_username, $mysql_password);
	if (!$db){
		echo("Öffnen der Datenbank gescheitert");
	}
	mysql_select_db($mysql_database);

?>

<br><br><br>
<center><h3>dtntrigger starten und beenden</h3></center>
<center><table border="1" width="1000" >
	<!-- ###########################  -->
	<!-- Tabellenkopf Zeile  -->
	<!-- ###########################  -->
	<tr align="center" valign="middle">
		<th height="40">Dienst</th>
		<th height="40" colspan="2">Aktueller Zustand? (l&auml;uft / gestoppt)</th>
		<th height="40">PID</th>
		<th height="40">Aktion 1</th>
		<th height="40">Aktion 2</th>
	</tr>
	
	<!-- ###########################  -->
	<!-- dtnd Zeile  -->
	<!-- ###########################  -->

	<!-- ###########################  -->
	<!-- dtntrigger Zeile  -->
	<!-- ###########################  -->
	<tr align="center" valign="middle">
		<td height="75">dtntrigger</td>
		
		<?php 
			$query = "SELECT `is_running`,`pid` FROM `dtntrigger`;";
			$result = mysql_query($query);
			$row = mysql_fetch_array($result);
			$isRunning= $row["is_running"];
			$pid= $row["pid"];
		?>
		
		<td height="75"><?php if ($isRunning==0){ ?><img id="dtntriggerStatusImage" src="ledred.png"/><?php }else{ ?><img id="dtntriggerStatusImage" src="ledgreen.png"/><?php } ?>
		</td>
		
		<td height="75"><div id="dtntriggerStatusText"><?php if ($isRunning==0){ ?>gestoppt<?php }else{ ?>l&auml;uft<?php } ?></div>
		</td>
		
		<td height="75"><div id="dtntriggerPid"><?php echo($pid);?></div></td>
		
		<td height="75"><div><button name="startDtntriggerButton" type="button" class="btn" value="starten" onclick="startStopDtntrigger(0);" <?php if ($isRunning==1){ echo("disabled"); } ?> >
				  <p><img src="start.png"><br><b>starten</b></p></button></div>
		</td>
		
		<td height="75"><div><button name="stopDtntriggerButton" type="button" class="btn" value="stoppen" onclick="startStopDtntrigger(1);" <?php if ($isRunning==0){ echo("disabled"); } ?> >
				  <p><img src="stop.png" ><br><b>stoppen</b></p></button></div>
		</td>
	</tr>	
	
</table></center>






<br><br><br>
<center><h3>Versionnummer setzen - FSG-Dateien f&uuml;r die Updates neu erzeugen</h3></center>
<center><form action="index.php"><table border="1" width="1000" >
	<!-- ###########################  -->
	<!-- Tabellenkopf Zeile  -->
	<!-- ###########################  -->
	<tr align="center" valign="middle">
		<th height="40">Datentyp</th>
		<th height="40">Aktuelle Version</th>
		<th height="40">Versionsnummer &auml;ndern</th>
		<th height="40">Aktion</th>
	</tr>
	
	<?php 
		$query = "SELECT `drivers`,`teams`,`blacklist_devices`,`blacklist_tags`,`driver_pics` FROM `versions_server`;";
		$result = mysql_query($query);
		$row = mysql_fetch_array($result);
		$driversVersion= $row["drivers"];
		$teamsVersion= $row["teams"];
		$blacklistDevicesVersion= $row["blacklist_devices"];
		$blacklistTagsVersion= $row["blacklist_tags"];
		$driverPicsVersion= $row["driver_pics"];
	?>
		
	<tr align="center" valign="middle">
		<td height="50">Fahrer</td>
		<td height="50"><div id="driversVersion"><?php echo($driversVersion);?></div></td>
		<td height="50"><input id="driversVersionId" type="text" size="10" maxlength="30"
								style="text-align:center" 
								value="<?php echo($driversVersion);?>"></td>
		<td height="50">
			<div>
				<button name="driversVersionButton" type="button" class="btn"
								value="speichern" onclick="saveVersion(1);"
					<?php if ($isRunning==1){ echo("disabled"); }?> >
					<p><img class="btntext" src="save_version.png"><b> Version speichern + FSG-Datei erzeugen</b></p>
				</button>
			</div>
		</td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Teams</td>
		<td height="50"><div id="teamsVersion"><?php echo($teamsVersion);?></div></td>
		<td height="50"><input id="teamsVersionId" type="text" size="10" maxlength="30"
								style="text-align:center" 
								value="<?php echo($teamsVersion);?>"></td>
		<td height="50">
			<div>
				<button name="teamsVersionButton" type="button" class="btn"
								value="speichern" onclick="saveVersion(2);"
					<?php if ($isRunning==1){ echo("disabled"); }?> >
					<p><img class="btntext" src="save_version.png"><b> Version speichern + FSG-Datei erzeugen</b></p>
				</button>
			</div>
		</td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Ger&auml;te-Blacklist</td>
		<td height="50"><div id="blacklistDevicesVersion"><?php echo($blacklistDevicesVersion);?></div></td>
		<td height="50"><input id="blacklistDevicesVersionId" type="text" size="10" maxlength="30"
								style="text-align:center" 
								value="<?php echo($blacklistDevicesVersion);?>"></td>
		<td height="50">
			<div>
				<button name="blacklistDevicesVersionButton" type="button" class="btn"
								value="speichern" onclick="saveVersion(3);"
					<?php if ($isRunning==1){ echo("disabled"); }?> >
					<p><img class="btntext" src="save_version.png"><b> Version speichern + FSG-Datei erzeugen</b></p>
				</button>
			</div>
		</td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">B&auml;nder-Blacklist</td>
		<td height="50"><div id="blacklistTagsVersion"><?php echo($blacklistTagsVersion);?></div></td>
		<td height="50"><input id="blacklistTagsVersionId" type="text" size="10" maxlength="30"
								style="text-align:center" 
								value="<?php echo($blacklistTagsVersion);?>"></td>
		<td height="50">
			<div>
				<button name="blacklistTagsVersionButton" type="button" class="btn"
								value="speichern" onclick="saveVersion(4);"
					<?php if ($isRunning==1){ echo("disabled"); }?> >
					<p><img class="btntext" src="save_version.png"><b> Version speichern + FSG-Datei erzeugen</b></p>
				</button>
			</div>
		</td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Fahrerbilder</td>
		<td height="50"><div id="driverPicsVersion"><?php echo($driverPicsVersion);?></div></td>
		<td height="50"><input id="driverPicsVersionId" type="text" size="10" maxlength="30"
								style="text-align:center" 
								value="<?php echo($driverPicsVersion);?>"></td>
		<td height="50">
			<div>
				<button name="driverPicsVersionButton" type="button" class="btn"
								value="speichern" onclick="saveVersion(5);"
					<?php if ($isRunning==1){ echo("disabled"); }?> >
					<p><img class="btntext" src="save_version.png"><b> Version speichern + FSG-Datei erzeugen</b></p>
				</button>
			</div>
		</td>
	</tr>
</table></form></center>



<br><br><br>
<center><h3>Daten an alle Clients senden</h3></center>
<center><form action="index.php"><table border="1" width="800" >
	<!-- ###########################  -->
	<!-- Tabellenkopf Zeile  -->
	<!-- ###########################  -->
	<tr align="center" valign="middle">
		<th height="40">Datentyp</th>
		<th height="40">Auswahl</th>
		<th height="40">Zuletzt gesendet</th>
		<th height="40">Aktion</th>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Fahrer</td>
		<td height="50"><input type="checkbox" id="driversCheckbox" /></td>
		
		<td height="50" rowspan="5">
			<div id="statusSendDataToClients" >
				///
			</div>
		</td>
		
		<td height="50" rowspan="5">
			<div>
				<button name="sendDataToClients" type="button" class="btn"
								value="senden" onclick="sendDataToAllClients();" >
					<p><img src="send_data.png"><br><b>Daten an die Clients senden</b></p>
				</button>
			</div>
		</td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Teams</td>
		<td height="50"><input type="checkbox" id="teamsCheckbox" /></td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Ger&auml;te-Blacklist</td>
		<td height="50"><input type="checkbox" id="blacklistDevicesCheckbox" /></td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">B&auml;nder-Blacklist</td>
		<td height="50"><input type="checkbox" id="blacklistTagsCheckbox" /></td>
	</tr>
	
	<tr align="center" valign="middle">
		<td height="50">Fahrerbilder</td>
		<td height="50"><input type="checkbox" id="driverPicsCheckbox" /></td>
	</tr>
</table></form></center>

<br><br><br>


    </body>
</html>
