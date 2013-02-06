#!/usr/bin/php -q
<?php
mb_internal_encoding("UTF-8");
$up_dir = "./fsg_updates/";

$up_bin_drivers       = $up_dir . "update_drivers.fsg";
$up_bin_teams         = $up_dir . "update_teams.fsg";
$up_bin_black_tags    = $up_dir . "update_black_tags.fsg";
$up_bin_black_devices = $up_dir . "update_black_devices.fsg";
$up_bin_tag_single    = $up_dir . "update_tag_single.fsg";
$up_bin_tag_multi     = $up_dir . "update_tag_multi.fsg";
$up_bin_driver_pics   = $up_dir . "update_driver_pics.fsg";

$up_txt_drivers       = $up_dir . "update_drivers.txt";
$up_txt_teams         = $up_dir . "update_teams.txt";
$up_txt_black_tags    = $up_dir . "update_black_tags.txt";
$up_txt_black_devices = $up_dir . "update_black_devices.txt";
$up_txt_tag_single    = $up_dir . "update_tag_single.txt";
$up_txt_tag_multi     = $up_dir . "update_tag_multi.txt";
$up_txt_driver_pics   = $up_dir . "update_driver_pics.zip";

$ver_drivers       = $up_dir . "version_drivers.txt";
$ver_teams         = $up_dir . "version_teams.txt";
$ver_black_tags    = $up_dir . "version_black_tags.txt";
$ver_black_devices = $up_dir . "version_black_devices.txt";
$ver_tag_single    = $up_dir . "version_tag_single.txt";
$ver_tag_multi     = $up_dir . "version_tag_multi.txt";
$ver_driver_pics   = $up_dir . "version_driver_pics.txt";

make_bin_file(11, false, $ver_drivers,         $up_txt_drivers,         $up_bin_drivers);
make_bin_file(12, false, $ver_teams,           $up_txt_teams,           $up_bin_teams);
make_bin_file(13, false, $ver_black_tags,      $up_txt_black_tags,      $up_bin_black_tags);
make_bin_file(14, false, $ver_black_devices,   $up_txt_black_devices,   $up_bin_black_devices);
make_bin_file(15, false, $ver_tag_single,      $up_txt_tag_single,      $up_bin_tag_single);
make_bin_file(16, false, $ver_tag_multi,       $up_txt_tag_multi,       $up_bin_tag_multi);
make_bin_file(17, true,  $ver_driver_pics,     $up_txt_driver_pics,     $up_bin_driver_pics);


echo ("Sieben (7) binaere Dateien erstellt." . "\n");


function make_bin_file($msg_type, $is_a_zipfile, $ver_file, $source_txt_file, $target_bin_file){
	$msg_type_byte_array = pack('V', $msg_type);          # FSG-Protokoll Header (1 of 3)
	
	$req_type_byte_array = pack('V', 0);                  # FSG-Protokoll Header (2 of 3)
	
	$version_text        = file_get_contents($ver_file);  # FSG-Protokoll Header (3 of 3)
	$version_byte_array  = pack('V', $version_text);      # FSG-Protokoll Header (3 of 3)
	
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
