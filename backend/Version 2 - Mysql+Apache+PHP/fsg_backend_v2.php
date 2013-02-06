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
$msg_typ_decimal = ord($header_msg_type);

switch($msg_typ_decimal){
	case $update_request:
		look_for_request_type($header_req_type, $sender );
		break;
	case $data_tag_single:
		save_data($msg_typ_decimal, $payload, $date_for_file);
		break;
	case $data_tag_multi:
		save_data($msg_typ_decimal, $payload, $date_for_file);
		break;
	case $confirm_drivers:
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $my_date);
		break;
	case $confirm_teams:
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $my_date);
		break;
	case $confirm_black_tags:
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $my_date);
		break;
	case $confirm_black_devices:
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $my_date);
		break;
	case $confirm_driver_pics:
		save_receive_confirmation($msg_typ_decimal, $msg_version_decimal, $sender, $my_date);
		break;
	default:
		echo("Fehler: Nachricht mit unbekanntem Typ empfangen(Nr. " 
			.  $msg_typ_decimal  . "). Die Nachricht wird ignoriert."  . "\n");
}//switch


	

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
function save_receive_confirmation($msg_typ, $msg_version, $sender, $my_date){
	$new_file  = "./fsg_confirmations/";
	
	switch($msg_typ){
		case $GLOBALS["confirm_drivers"]:
			write_confirm($my_date, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_teams"]:
			write_confirm($my_date, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_black_tags"]:
			write_confirm($my_date, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_black_devices"]:
			write_confirm($my_date, $msg_version, $sender);
			break;
		case $GLOBALS["confirm_driver_pics"]:
			write_confirm($my_date, $msg_version, $sender);
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
function write_confirm($my_date, $msg_version, $sender){



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
