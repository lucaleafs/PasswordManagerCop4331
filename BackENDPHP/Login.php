<?php

    // saves incoming JSON payload to PHP variable.
	$inData = getRequestInfo();
	
	// initializes utility variables for login account.
	$id = 0;
	$firstName = "";
	$lastName = "";
	$login = $inData["login"];
	$password = $inData["password"];

    // Manual SQL connection
	$conn = new mysqli("localhost", "passwords", "Processes2018!", "password_COP4331");
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
        
        // query variable for account info retrieval.
	    $sql = "SELECT * FROM Users WHERE Login = '$login' AND Password = SHA('$password')";

        // save results of query execution
        // if it works, return account details.
        // else, return error that nothing was found.
		$result = $conn->query($sql);
		if ($result->num_rows > 0)
		{
			$row = $result->fetch_assoc();
			$firstName = $row["firstName"];
			$lastName = $row["lastName"];
			$id = $row["ID"];
			
		    returnWithInfo($firstName, $lastName, $id );

		}
		else
		{
			returnWithError( "No Records Found" );
		}
		$conn->close();
	} // end of login query execution.
	

        // auxiliary function for passing back JSON content to frontend.
	function getRequestInfo()
	{
		return json_decode(file_get_contents('php://input'), true);
	}

        // auxiliary function for setting headers and passing back JSON content to frontend.
	function sendResultInfoAsJson( $obj )
	{
		header('Content-type: application/json');
		echo $obj;
	}
	
		// auxiliary function for sending back any errors
	function returnWithError( $err )
	{
		$retValue = '{"id":0,"firstName":"","lastName":"","error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	
	// auxiliary function for structuring the returning content into JSON format manually.
	function returnWithInfo( $firstName, $lastName, $id )
	{
		$retValue = '{"id":' . $id . ',"firstName":"' . $firstName . '","lastName":"' . $lastName . '","error":""}';
		sendResultInfoAsJson( $retValue );
	}
	
?>