<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	$u_email = $_POST["u_email"];
	$statement = mysqli_prepare($con, "SELECT u_pw FROM UserTable WHERE u_id = ? AND u_email = ?");

	mysqli_stmt_bind_param($statement, "ss", $u_id, $u_email);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $u_pw);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)) {
		$response["success"] = true;
		$response["u_pw"] = $u_pw;
	}

	echo json_encode($response);
?>