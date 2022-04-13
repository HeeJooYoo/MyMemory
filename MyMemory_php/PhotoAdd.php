<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	if(mysqli_connect_errno($con)) {echo "DB 접속 실패";} else {echo "DB 접속 성공";}

	$photo_id = $_POST["photo_id"];
	$photo_img = $_POST["photo_img"];
	$photo_txt = $_POST["photo_txt"];
	$album_id = $_POST["album_id"];

	$statement = mysqli_prepare($con, "INSERT INTO PhotoTable(photo_id, photo_img, photo_txt, album_id) VALUES (?,?,?,?)");
	mysqli_stmt_bind_param($statement, "ssss", $photo_id, $photo_img, $photo_txt, $album_id);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = false;

	echo json_encode($response);
?>