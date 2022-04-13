<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	$statement = mysqli_prepare($con, "SELECT u_pw, u_name, u_email FROM UserTable WHERE u_id = ?");

	mysqli_stmt_bind_param($statement, "s", $u_id);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $u_pw, $u_name, $u_email);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)) {
		$response["success"] = true;
		$response["u_pw"] = $u_pw;
		$response["u_name"] = $u_name;
		$response["u_email"] = $u_email;
	}

	echo json_encode($response);
?>