<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$photo_id = $_POST["photo_id"];
	$statement = mysqli_prepare($con, "SELECT photo_img, photo_txt FROM PhotoTable WHERE photo_id = ?");

	mysqli_stmt_bind_param($statement, "s", $photo_id);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $photo_img, $photo_txt);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)) {
		$response["success"] = true;
		$response["photo_img"] = $photo_img;
		$response["photo_txt"] = $photo_txt;
	}

	echo json_encode($response);
?>