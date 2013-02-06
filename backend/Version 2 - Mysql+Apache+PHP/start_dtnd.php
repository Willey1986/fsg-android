<?php
$mysql_hostname ="localhost";
$mysql_username ="fsgbackend";
$mysql_password ="cowteam12345";
$mysql_database ="fsgbackend";
$pid = 0;

$command = "dtnd -D -p pid.txt -q -c virtualbox.conf";
//$pid = exec("nohup dtnd -q -c virtualbox.conf 2> /dev/null & echo $! &");
//$pid = (int) exec('nohup dtnd -q -c virtualbox.conf > /dev/null 2>&1 & echo $!', $out, $error);
$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");
//$pid = exec("nohup $command > /dev/null & "); 



//exec("ps $pid", $pState);
//if((count($pState) >= 2)){
//	echo("Prozess läuft! " . $pid);
//}


// Datenbankverbindung und anschliessende Operationen
$db = mysql_connect($mysql_hostname, $mysql_username, $mysql_password);
if (!$db || $pid==0){
	//echo("Öffnen der Datenbank gescheitert");
	exec("kill $pid"); 	// Wir schliessen den Prozess, damit die Datenbank
						// und der tatsaechlichen Zustand nicht auseinander
						// gehen. Sonst laeuft der Prozess und wir wissen
						// in der Datenbank nichts davon.
	echo("Fehler. Der Dienst konnte nicht gestartet werden!<br>Der Dienst ist gestoppt.".
			"[BRK]"."starten"."[BRK]"."start.png"."[BRK]"."ledred.png"."[BRK]".$pid);
}else{
	mysql_select_db($mysql_database);
	$query = "UPDATE `fsgbackend`.`dtnd` SET `is_running` = '1',`pid` = '".$pid."';";
	$result = mysql_query($query);
	mysql_close($db);
	echo("l&auml;uft"."[BRK]"."stoppen"."[BRK]"."stop.png"."[BRK]"."ledgreen.png"."[BRK]".$pid);
}
	
?>
