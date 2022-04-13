<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_name= $_POST["u_name"];
	$u_email = $_POST["u_email"];
	$statement = mysqli_prepare($con, "SELECT u_id FROM UserTable WHERE u_name = ? AND u_email = ?");

	mysqli_stmt_bind_param($statement, "ss", $u_name, $u_email);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $u_id);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)) {
		$response["success"] = true;
		$response["u_id"] = $u_id;
	}

	echo json_encode($response);
?>