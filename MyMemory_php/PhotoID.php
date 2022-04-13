<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$album_id = $_POST["album_id"];
	$photo_id = $_POST["photo_id"];

	$statement = mysqli_prepare ($con, "SELECT COUNT(photo_img) FROM PhotoTable WHERE album_id = ?");

	mysqli_stmt_bind_param($statement, "s", $album_id);
	mysqli_stmt_execute($statement);
	$cnt = null;
	mysqli_stmt_bind_result($statement, $cnt);
	
	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)){
		$response["success"] = true;
		$response["count"] = $cnt;
	}

	echo json_encode($response);
?>