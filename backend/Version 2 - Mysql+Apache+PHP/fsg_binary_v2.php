<?php
mb_internal_encoding("UTF-8");
$mysql_hostname ="localhost";
$mysql_username ="fsgbackend";
$mysql_password ="cowteam12345";
$mysql_database ="fsgbackend";

$new_version =$_GET['ver'];
$option      =$_GET['opt'];

$db = mysql_connect($mysql_hostname, $mysql_username, $mysql_password);
if (!$db){
	//echo("Öffnen der Datenbank gescheitert");
	echo("7"."[BRK]"."l7"."[BRK]"."7"); // Mit 7 als Antwort passiert drüben nichts...

}else{
	$up_dir = "./fsg_updates/";

	if($option==1){          //drivers
		$up_bin_drivers       = $up_dir . "update_drivers.fsg";
		$up_txt_drivers       = $up_dir . "update_drivers.txt";
		make_bin_file(11, false, $new_version, $up_txt_drivers, $up_bin_drivers);
		$query = "UPDATE `fsgbackend`.`versions_server` SET `drivers` = '".$new_version."';";
		$result = mysql_query($query);
		mysql_close($db);
		echo("1"."[BRK]".$new_version);
		
	}else if($option==2){    //teams
		$up_bin_teams         = $up_dir . "update_teams.fsg";
		$up_txt_teams         = $up_dir . "update_teams.txt";
		make_bin_file(12, false, $new_version, $up_txt_teams, $up_bin_teams);
		$query = "UPDATE `fsgbackend`.`versions_server` SET `teams` = '".$new_version."';";
		$result = mysql_query($query);
		mysql_close($db);
		echo("2"."[BRK]".$new_version);
		
	}else if($option==3){    //black_devices
		$up_bin_black_devices = $up_dir . "update_black_devices.fsg";
		$up_txt_black_devices = $up_dir . "update_black_devices.txt";
		make_bin_file(14, false, $new_version, $up_txt_black_devices, $up_bin_black_devices);
		$query = "UPDATE `fsgbackend`.`versions_server` SET `blacklist_devices` = '".$new_version."';";
		$result = mysql_query($query);
		mysql_close($db);
		echo("3"."[BRK]".$new_version);
		
	}else if($option==4){    //black_tags
		$up_bin_black_tags    = $up_dir . "update_black_tags.fsg";
		$up_txt_black_tags    = $up_dir . "update_black_tags.txt";
		make_bin_file(13, false, $new_version, $up_txt_black_tags, $up_bin_black_tags);
		$query = "UPDATE `fsgbackend`.`versions_server` SET `blacklist_tags` = '".$new_version."';";
		$result = mysql_query($query);
		mysql_close($db);
		echo("4"."[BRK]".$new_version);
		
	}else if($option==5){    //driver_pics
		$up_bin_driver_pics   = $up_dir . "update_driver_pics.fsg";
		$up_txt_driver_pics   = $up_dir . "update_driver_pics.zip";
		make_bin_file(17, true,  $new_version, $up_txt_driver_pics, $up_bin_driver_pics);
		$query = "UPDATE `fsgbackend`.`versions_server` SET `driver_pics` = '".$new_version."';";
		$result = mysql_query($query);
		mysql_close($db);
		echo("5"."[BRK]".$new_version);
		
	}

}




function make_bin_file($msg_type, $is_a_zipfile, $version, $source_txt_file, $target_bin_file){
	$msg_type_byte_array = pack('V', $msg_type);          # FSG-Protokoll Header (1 of 3)
	
	$req_type_byte_array = pack('V', 0);                  # FSG-Protokoll Header (2 of 3)
	
	$version_byte_array  = pack('V', $version);           # FSG-Protokoll Header (3 of 3)
	
	if($is_a_zipfile){
		$source_text     = file_get_contents($source_txt_file);
	}else{
		$source_text     = file_get_contents_utf8($source_txt_file);
	}

	$handle3 = fopen($target_bin_file, "wb");
	fwrite($handle3, $msg_type_byte_array[0]);
	fwrite($handle3, $req_type_byte_array[0]);
	fwrite($handle3, $version_byte_array[3]);
	fwrite($handle3, $version_byte_array[2]);
	fwrite($handle3, $version_byte_array[1]);
	fwrite($handle3, $version_byte_array[0]);
	fwrite($handle3, $source_text);
	fclose($handle3);
}


# Diese Methode ist noetig, um Sonderzeichen auf der Clientseite zu 
# erhalten. Die Standardmethode (file_get_contents() ) kann mit 
# Sonderzeichen nicht umgehen,so dass auf der Client-Seite die
# Namen mit Sonderzeichen nicht richtig dargestellt werden
function file_get_contents_utf8($fn) {
     $content = file_get_contents($fn);
      return mb_convert_encoding($content, 'UTF-8',
          mb_detect_encoding($content, 'UTF-8, ISO-8859-1', true));
}


?>
