<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	
	$statement = mysqli_prepare($con, "SELECT album_id, album_img, album_title FROM AlbumTable WHERE u_id = ?");
	
	mysqli_stmt_bind_param($statement, "s", $u_id);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $album_id, $album_img, $album_title);

	$response = array();
	
	while(mysqli_stmt_fetch($statement)) {
		array_push($response, array("album_id"=> $album_id, "album_img"=>$album_img, "album_title"=>$album_title));
	}

	echo json_encode(array("albumData"=>$response));
	//mysqli_close($con);
?>