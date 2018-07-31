<?php



// saves incoming JSON payload to PHP variable.
	$inData = getRequestInfo();
	

    $userId = $inData["userId"];
    $account = $inData["account"];

    // Maunal SQL connection.
	$conn = new mysqli("localhost", "passwords", "Processes2018!", "password_COP4331");
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
        
        // query used for deleting contact.
	    $sql = "DELETE FROM passwords WHERE account = '{$account}' AND UserId = '{$userId}'";
        
        // results saved from executing deletion query.
		if($conn->query($sql) === TRUE)
		returnWithInfo("success");
		else
		returnWithError( "Did not find account" );

		$conn->close();
	}// end of deletion query if/else
	

    // auxiliary function for retrieving/deconding incoming JSON payload.
	function getRequestInfo()
	{
		return json_decode(file_get_contents('php://input'), true);
	}

    // auxiliary function for passing back JSON content to frontend.
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
	function returnWithInfo($result)
	{
		$retValue = '{"result":' . $result . ',"error":""}';
		sendResultInfoAsJson( $retValue );
	}
	
?>