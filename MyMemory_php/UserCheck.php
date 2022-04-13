<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	$statement = mysqli_prepare($con, "SELECT u_id FROM UserTable WHERE u_id = ?");

	mysqli_stmt_bind_param($statement, "s", $u_id);
	mysqli_stmt_execute($statement);

	$response["newID"] = true;

	while(mysqli_stmt_fetch($statement)) {
		$response["newID"] = false;
		$response["u_id"] = $u_id;
	}

	echo json_encode($response);
?>