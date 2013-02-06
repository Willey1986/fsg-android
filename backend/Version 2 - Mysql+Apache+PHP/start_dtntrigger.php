<?php
$mysql_hostname ="localhost";
$mysql_username ="fsgbackend";
$mysql_password ="cowteam12345";
$mysql_database ="fsgbackend";



// Datenbankverbindung und anschliessende Operationen
$db = mysql_connect($mysql_hostname, $mysql_username, $mysql_password);
if (!$db){
	//echo("Öffnen der Datenbank gescheitert");
	echo("Datenbankfehler, der Dienst konnte nicht gestartet werden!<br>Der Dienst ist gestoppt.".
			"[BRK]"."ledred.png"."[BRK]"."0");
	
}else{
	mysql_select_db($mysql_database);
	
	$query = "SELECT `is_running` FROM `dtntrigger`;";
	$result = mysql_query($query);
	$row = mysql_fetch_array($result);
	$isRunning= $row["is_running"];
	
	if ($isRunning==1){
		echo("abbruch"."[BRK]"."ledgreen.png"."[BRK]"."0");
		
	}else{
		$command = "dtntrigger fsg php fsg_backend_v2.php";
		$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");

		$query = "UPDATE `fsgbackend`.`dtntrigger` SET `is_running` = '1',`pid` = '".$pid."';";
		$result = mysql_query($query);
		mysql_close($db);
		echo("l&auml;uft"."[BRK]"."ledgreen.png"."[BRK]".$pid);
			
	}
	

}


?>
