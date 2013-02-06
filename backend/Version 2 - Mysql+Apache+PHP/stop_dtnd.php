<?php
$mysql_hostname ="localhost";
$mysql_username ="fsgbackend";
$mysql_password ="cowteam12345";
$mysql_database ="fsgbackend";

$pid = 0;

// Datenbankverbindung und anschliessende Operationen
$db = mysql_connect($mysql_hostname, $mysql_username, $mysql_password);
if (!$db){
	//echo("Öffnen der Datenbank gescheitert");
	echo("Datenbankfehler, der Dienst konnte nicht gestoppt werden. <br> Der Dienst l&auml;uft.".
			"[BRK]"."stoppen"."[BRK]"."stop.png"."[BRK]"."ledgreen.png"."[BRK]"."???");
}else{
	mysql_select_db($mysql_database);
	$query = "SELECT `pid` FROM `dtnd`";
	$result = mysql_query($query);
	$row = mysql_fetch_array($result);
	$pid= $row["pid"];                 // Herausfinden der Prozess-PID
	exec("kill $pid");                 // Stoppen des Prozesses anhand der PID
	
	// Aktuallisieren der Datenbank mit dem neuen Zustand (Aus!)
	$query = "UPDATE `fsgbackend`.`dtnd` SET `is_running` = '0',`pid` = '0';";
	$result = mysql_query($query);
	mysql_close($db);
	echo("gestoppt"."[BRK]"."starten"."[BRK]"."start.png"."[BRK]"."ledred.png"."[BRK]"."0");
}


?>
