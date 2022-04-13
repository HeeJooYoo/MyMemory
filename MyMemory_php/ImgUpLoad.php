<?php
	$data = $_POST["data1"]; // newImage

    $file_path = $data."/"; // newImage/

	if(is_dir($data)) {
		echo "폴더가 존재합니다.";
	} else {
		echo "폴더가 존재하지 않습니다.";
		@chmod($data,0777);
		@mkdir($data,0777);
	}

		$file_name = basename( $_FILES['uploaded_file']['name']);
    	$file_path = $file_path.$file_name; // newImage/이름.jpg
	
    	if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        	echo "file upload success";
			echo $file_path;
    	} else{
        	echo "fail upload fail";
    	}
?>