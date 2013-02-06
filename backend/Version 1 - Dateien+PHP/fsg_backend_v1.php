#!/usr/bin/php -q
<?php
######################################################
######################################################
# Notwendige Variablendefinitionen
$sender        = $argv[1];
$temp_file     = $argv[2];
$my_date       = date('d.m.Y H:i:s');
$date_for_file = date('Y.m.d__H.i.s');
$update_dir    = "./fsg_updates/";


######################################################
######################################################
# Notwendige Konstantendefinitionen
$update_request        = 1;

$data_drivers          = 11;
$data_teams            = 12;
$data_black_tags       = 13;
$data_black_devices    = 14;
$data_tag_single       = 15;
$data_tag_multi        = 16;
$data_driver_pics      = 17;

$confirm_drivers       = 21;
$confirm_teams         = 22;
$confirm_black_tags    = 23;
$confirm_black_devices = 24;
$confirm_tag_single    = 25;
$confirm_tag_multi     = 26;
$confirm_driver_pics   = 27;


######################################################
######################################################
# Nun lesen wir den Header der Nachricht, dabei finden
# wir heraus, was fuer eine Art Nachricht es sich hier 
# handelt und ggf. die Version der Daten. Danach 
# lesen wir die eigentliche Nachricht.
$handle               = fopen( $temp_file, "rb");
$header_msg_type      = fread( $handle,1);
$header_req_type      = fread( $handle,1);
$header_version_byte1 = fread( $handle,1);
$header_version_byte2 = fread( $handle,1);
$header_version_byte3 = fread( $handle,1);
$header_version_byte4 = fread( $handle,1);
$payload_bytes_amount = filesize( $temp_file ) -6;
$payload              = fread( $handle, $payload_bytes_amount);
$msg_version_binary   = $header_version_byte1 . $header_version_byte2 . 
							$header_version_byte3 . $header_version_byte4;
$msg_version_decimal  = hexdec(bin2hex($msg_version_binary));
fclose($handle);


######################################################
######################################################
# Jetzt ein wenig Text fuer den Administrator
echo("============================================" . "\n");
echo("============================================" . "\n");
echo("Bundle empfangen von: " . $sender    . "\n");
echo("Ankunftszeit:         " . $my_date   . "\n");


######################################################
######################################################
# Fallunterscheidung - Nachrichtentyp
$msg_typ_decimal = ord($header_msg_type);
#echo("+++++++++++++++++++++++++" . "\n");
#echo("(Debug)Nach.-Typ-Nr.: " . $msg_typ_decimal     . "\n");
#echo("(Debug)Version-Nr.:   " . $msg_version_decimal . "\n");
#echo("+++++++++++++++++++++++++" . "\n");

switch($msg_typ_decimal){
	case $update_request:
		print_msg_type("Update-Anforderung");
		look_for_request_type($header_req_type, $sender );
		break;
	case $data_tag_single:
		print_msg_type("Baenderdaten (single)");
		save_data($msg_typ_decimal, $payload, $date_for_file);
		break;
	case $data_tag_multi:
		print_msg_type("Baenderdaten (multi)");
		save_data($msg_typ_decimal, $payload, $date_for_file);
		break;
	case $confirm_drivers:
		print_msg_type("Empfangsbestaetigung (Fahrerdaten)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
	case $confirm_teams:
		print_msg_type("Empfangsbestaetigung (Teamsdaten)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
	case $confirm_black_tags:
		print_msg_type("Empfangsbestaetigung (Baender-Blacklist)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
	case $confirm_black_devices:
		print_msg_type("Empfangsbestaetigung (Geraete-Blacklist)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
	case $confirm_tag_single:
		print_msg_type("Empfangsbestaetigung (Banddaten: Einzelnes Band)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
	case $confirm_tag_multi:
		print_msg_type("Empfangsbestaetigung (Banddaten: Mehrere Baender)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
	case $confirm_driver_pics:
		print_msg_type("Empfangsbestaetigung (Fahrerbilder)");
		print_msg_version($msg_version_decimal);
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $date_for_file);
		break;
		
	default:
		echo("Fehler: Nachricht mit unbekanntem Typ empfangen(Nr. " 
			.  $msg_typ_decimal  . "). Die Nachricht wird ignoriert."  . "\n");
}//switch


######################################################
######################################################
# Um etwas Code zu sparen und Änderungen zu erleichtern...
function print_msg_type($msg_type_string){
	echo("Nachrichttyp:         " . $msg_type_string   . "\n");
}

######################################################
######################################################
# Um etwas Code zu sparen und Änderungen zu erleichtern...
function print_msg_version($msg_version_decimal){
	echo("Bestaetigte Version:  " . $msg_version_decimal   . "\n");
}
	

######################################################
######################################################
# Ankommende Baenderdaten werden hiermit gespeichert.
function save_data($msg_typ, $payload, $date_for_file){
	$new_file  = "./fsg_tag_data/";
	switch($msg_typ){
		case $GLOBALS["data_tag_single"]:
			$new_file = $new_file . "single_" . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_file ($new_file, $payload);
			break;
		case $GLOBALS["data_tag_multi"]:
			$new_file = $new_file . "multi_"  . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_file ($new_file, $payload);
			break;
		default:
			echo("Fehler: Eigentlich unmoeglich hier anzukommen..." . "\n");
	}//switch
}


######################################################
######################################################
# Mit dieser Funktion speichern wir die Empfangsbestaetigungen,
# die der Server bekommt.
function save_receive_confirmation($msg_typ, $msg_version, $sender, $date_for_file){
	$new_file  = "./fsg_confirmations/";
	
	switch($msg_typ){
		case $GLOBALS["confirm_drivers"]:
			$new_file= $new_file . "drivers_"         . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_teams"]:
			$new_file= $new_file . "teams_"           . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_black_tags"]:
			$new_file= $new_file . "black_tags_" . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_black_devices"]:
			$new_file= $new_file . "black_devices_"   . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_tag_single"]:
			$new_file= $new_file . "tag_single_" . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_tag_multi"]:
			$new_file= $new_file . "tag_multi_"  . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_driver_pics"]:
			$new_file= $new_file . "driver_pics_"     . $date_for_file . "_" . rand(100000,999999) . ".txt";
			write_confirm($new_file, $msg_version, $sender);
			break;
		default:
			echo("Fehler: Eigentlich unmoeglich hier anzukommen..." . "\n");
	}//switch
}


######################################################
######################################################
# Hiermit speichern wir eine Datei mit dem uebergebenen
# Inhalt. Diese Funktion wird benutzt, um Baenderdaten
# zu speichern.
function write_file ($new_file, $payload){
	$handle2 = fopen($new_file, "w");
	fwrite($handle2, $payload);
	fclose($handle2);
}


######################################################
######################################################
# Hiermit speichern wir eine Datei mit dem uebergebenen
# Inhalt. Diese Funktion wird benutzt, um 
# Empfangsbestaetigungen zu speichern.
function write_confirm($new_file, $msg_version, $sender){
	$handle2 = fopen($new_file, "w");
	fwrite($handle2, $msg_version);
	fwrite($handle2, "\n");
	fwrite($handle2, $sender);
	fclose($handle2);
}


######################################################
######################################################
function look_for_request_type($header_req_type, $sender){
	
	$header_req_type_decimal = ord($header_req_type);
	
	$update_txt_drivers       = $GLOBALS["update_dir"] . "update_drivers.fsg";
	$update_txt_teams         = $GLOBALS["update_dir"] . "update_teams.fsg";
	$update_txt_black_tags    = $GLOBALS["update_dir"] . "update_black_tags.fsg";
	$update_txt_black_devices = $GLOBALS["update_dir"] . "update_black_devices.fsg";
	$update_txt_driver_pics   = $GLOBALS["update_dir"] . "update_driver_pics.fsg";
	
	$teamsRequest            = $header_req_type_decimal & 1;
	$driversRequest          = $header_req_type_decimal & 2;
	$driverPicsRequest       = $header_req_type_decimal & 4;
	$blacklistTagsRequest    = $header_req_type_decimal & 8;
	$blacklistDevicesRequest = $header_req_type_decimal & 16;
	
	if($teamsRequest==1){
		$output1 = shell_exec("dtnsend $sender  $update_txt_teams");
		echo($output1);
	}
	
	if($driversRequest==2){
		$output1 = shell_exec("dtnsend $sender  $update_txt_drivers");
		echo($output1);
	}
	
	if($blacklistTagsRequest==8){
		$output1 = shell_exec("dtnsend $sender  $update_txt_black_tags");
		echo($output1);		
	}
	
	if($blacklistDevicesRequest==16){
		$output1 = shell_exec("dtnsend $sender  $update_txt_black_devices");
		echo($output1);		
	}
	
	# Lassen wir als letztes, weil die Bilderdaten am groessten sind und
	# die andere Datensaetze blockieren wuerden. So erreichen die andere
	# Datensaetze schnell das Ziel und die Bilder halt danach.
	if($driverPicsRequest==4){ 
		$output1 = shell_exec("dtnsend $sender  $update_txt_driver_pics");
		echo($output1);		
	}
}


?>
