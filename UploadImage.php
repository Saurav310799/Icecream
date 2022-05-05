<?php

header("Content-Type: application/json");
header("Acess-Control-Allow-Origin: *");
header("Acess-Control-Allow-Methods: POST"); // here is define the request method

//include 'dbconfig.php'; // include database connection file


 $connection = mysqli_connect("localhost","user","pass","homeinformation") or die("Error " . mysqli_error($connection));



if(!$connection){
	die("Connection failed: " . mysqli_connect_error());
}
else{
   
}


$data = json_decode(file_get_contents("php://input"), true); // collect input parameters and convert into readable format

$fileName  =  $_FILES['sendimage']['name'];
$tempPath  =  $_FILES['sendimage']['tmp_name'];
$fileSize  =  $_FILES['sendimage']['size'];

	
$fname=$_POST["fname"];
$price=$_POST["price"];

		
if(empty($fileName))
{
	$errorMSG = json_encode(array("message" => "please select image", "status" => false));	
	echo $errorMSG;
}
else
{
	$upload_path = 'resources/'; // set upload folder path 
	
	$fileExt = strtolower(pathinfo($fileName,PATHINFO_EXTENSION)); // get image extension
		
	// valid image extensions
	$valid_extensions = array('jpeg', 'jpg', 'png', 'gif'); 
					
	// allow valid image file formats
	if(in_array($fileExt, $valid_extensions))
	{				
		//check file not exist our upload folder path
		if(!file_exists($upload_path . $fileName))
		{
			// check file size '5MB'
			if($fileSize < 5000000){
				move_uploaded_file($tempPath, $upload_path . $fileName); // move file from system temporary path to our upload folder path 
			}
			else{		
				$errorMSG = json_encode(array("message" => "Sorry, your file is too large, please upload 5 MB size", "status" => false));	
				echo $errorMSG;
			}
		}
		else
		{		
			$errorMSG = json_encode(array("message" => "Sorry, file already exists check upload folder", "status" => false));	
			echo $errorMSG;
		}
	}
	else
	{		
		$errorMSG = json_encode(array("message" => "Sorry, only JPG, JPEG, PNG & GIF files are allowed", "status" => false));	
		echo $errorMSG;		
	}
}
		
// if no error caused, continue ....
if(!isset($errorMSG))
{
 
       $image= "location_of_your_file_where_image_is_going_to_save/$fileName";
    	$sql = "INSERT INTO Icecream(icecreamflavour,price,image)VALUES ('$fname','$price','$image')";
    	
    	
if ($connection->query($sql) === TRUE) {
	//echo "record inserted successfully";
} else {
	echo "Error: " . $sql . "<br>" . $connection->error;
}

	
	echo json_encode(array("message" => "Image Uploaded Successfully", "status" => true));	
}

?>