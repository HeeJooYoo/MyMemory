<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	$album_id = $_POST["album_id"];

	$statement = mysqli_prepare ($con, "SELECT COUNT(album_title) FROM AlbumTable WHERE u_id = ?");

	mysqli_stmt_bind_param($statement, "s", $u_id);
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