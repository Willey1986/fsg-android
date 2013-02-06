<?php
$drivers          =$_GET['d'];
$teams            =$_GET['t'];
$blacklistDevices =$_GET['bld'];
$blacklistTags    =$_GET['blt'];
$driverPics       =$_GET['dp'];

$my_date       = date('d.m.Y H:i:s');

$up_dir    = "./fsg_updates/";
$up_bin_drivers       = $up_dir . "update_drivers.fsg";
$up_bin_teams         = $up_dir . "update_teams.fsg";
$up_bin_black_devices = $up_dir . "update_black_devices.fsg";
$up_bin_black_tags    = $up_dir . "update_black_tags.fsg";
$up_bin_driver_pics   = $up_dir . "update_driver_pics.fsg";
	
	if($drivers==1){
		$command = "dtnsend -g dtn://fsg.dtn/broadcast  $up_bin_drivers";
		$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");
	}
	
	if($teams==1){
		$command = "dtnsend -g dtn://fsg.dtn/broadcast  $up_bin_teams";
		$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");
	}
	
	if($blacklistDevices==1){
		$command = "dtnsend -g dtn://fsg.dtn/broadcast  $up_bin_black_devices";
		$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");	
	}
	
	if($blacklistTags==1){
		$command = "dtnsend -g dtn://fsg.dtn/broadcast  $up_bin_black_tags";
		$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");

	}

	if($driverPics==1){ 
		$command = "dtnsend -g dtn://fsg.dtn/broadcast  $up_bin_driver_pics";
		$pid = exec("nohup $command > /dev/null 2>&1 & echo $!");

	}
	if($drivers==1 || $teams==1 || $blacklistDevices==1 || $blacklistTags==1 || $driverPics==1){
		echo($my_date);
	}
	


?>
