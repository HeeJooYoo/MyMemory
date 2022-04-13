<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	$u_pw = $_POST["u_pw"];
	$u_name = $_POST["u_name"];
	$u_email = $_POST["u_email"];

	$statement = mysqli_prepare($con, "INSERT INTO UserTable VALUES (?,?,?,?)"); 
	mysqli_stmt_bind_param($statement, "ssss", $u_id, $u_pw, $u_name, $u_email);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = true;

	echo json_encode($response);

?>