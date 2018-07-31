<?php

// DB connection variables. 
$servername = "localhost";
$username = "passwords";
$password = "Processes2018!";
$dbname = "password_COP4331";

$inData = getRequestInfo();

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
     returnWithError( "connection failed" );

}

//grabs the parameters from the URL
// you can change it to $_POST for more secure
$firstname = $inData["firstName"];
$lastname = $inData["lastName"];
$login = $inData["login"];
$password = $inData["password"];



$ucount_query = $conn->query("select ID from Users");//gets all IDs

$ucount = mysqli_num_rows($ucount_query);//counts the number of IDs

$ucount+=1; // increments by 1. Good for adding users sequentially
$id = $ucount;
$insert = "INSERT INTO Users (ID, DateCreated, DateLastLoggedIn, FirstName, LastName, Login, Password) VALUES (" . $ucount .", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, \"" . $firstname ."\", \"" . $lastname ."\", \"" . $login ."\", sha1(\"" . $password ."\"))";

if ($conn->query($insert) === TRUE) {
    //echo "me record created successfully";
    returnWithInfo($firstName, $lastName, $id );

} else {
    //echo "Error: " . $insert . "<br>" . $conn->error;
    returnWithError( "User already exists" );

}

    function returnWithInfo( $firstName, $lastName, $id )
	{
		$retValue = '{"id":' . $id . ',"firstName":"' . $firstName . '","lastName":"' . $lastName . '","error":""}';
		sendResultInfoAsJson( $retValue );
	}
	function returnWithError( $err )
	{
		$retValue = '{"id":0,"firstName":"","lastName":"","error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	function getRequestInfo()
	{
		return json_decode(file_get_contents('php://input'), true);
	}
	function sendResultInfoAsJson( $obj )
	{
		header('Content-type: application/json');
		echo $obj;
	}
$conn->close();
?>
