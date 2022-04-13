<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$album_id = $_POST["album_id"];
	
	$statement = mysqli_prepare($con, "SELECT photo_id, photo_img, photo_txt FROM PhotoTable WHERE album_id = ?");
	
	mysqli_stmt_bind_param($statement, "s", $album_id);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $photo_id, $photo_img, $photo_txt);

	$response = array();
	
	while(mysqli_stmt_fetch($statement)) {
		array_push($response, array("photo_id"=> $photo_id, "photo_img"=>$photo_img, "photo_txt"=>$photo_txt));
	}

	echo json_encode(array("photoData"=>$response));
	//mysqli_close($con);
?>