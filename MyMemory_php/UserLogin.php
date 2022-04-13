<?php
	$con = mysqli_connect("localhost", "root", "apmsetup", "mymemory");

	$u_id = $_POST["u_id"];
	$u_pw = $_POST["u_pw"];
	$statement = mysqli_prepare ($con, "SELECT u_id FROM UserTable WHERE u_id = ? and u_pw = ?");

	mysqli_stmt_bind_param($statement, "ss", $u_id, $u_pw);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = false;

	while (mysqli_stmt_fetch($statement)) {
		$response["success"] = true;
		$response["u_id"] = $u_id;
	}
	echo json_encode($response);
?>