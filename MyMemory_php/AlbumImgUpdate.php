<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$album_id = $_POST["album_id"];
	$album_img = $_POST["album_img"];

	$statement = mysqli_prepare ($con, "UPDATE AlbumTable SET album_img = ? WHERE album_id = ?");
	mysqli_stmt_bind_param($statement, "ss", $album_img, $album_id);
	mysqli_stmt_execute($statement);
	
	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>