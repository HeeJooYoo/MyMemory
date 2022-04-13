<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$album_id = $_POST["album_id"];
	$album_img = $_POST["album_img"];
	$album_title = $_POST["album_title"];
	$u_id = $_POST["u_id"];

	$statement = mysqli_prepare ($con, "INSERT INTO AlbumTable(album_id, album_img, album_title, u_id) VALUES (?,?,?,?)");

	mysqli_stmt_bind_param($statement, "ssss", $album_id, $album_img, $album_title, $u_id);
	mysqli_stmt_execute($statement);
	
	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>