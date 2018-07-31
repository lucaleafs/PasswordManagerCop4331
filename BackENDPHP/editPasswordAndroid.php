<?php

	$inData = getRequestInfo();
	$oldAccount = $inData["oldAccount"];
	$username = $inData["username"];
    $password = $inData["password"];
    $url = $inData["url"];
    $iconUrl = $inData["iconUrl"];
    $favorite = $inData["favorite"];
    $userId = $inData["userId"];
    $account = $inData["account"];
    
	// initializes variables and save incoming JSON to them.
    // Manual SQL connection
    
	$conn = new mysqli("localhost", "passwords", "Processes2018!", "password_COP4331");
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
      $sql =  "UPDATE `passwords` SET `account` = '{$account}', `username` = '{$username}', `password` = '{$password}', `favorite` = '{$favorite}', `url` = '{$url}', `iconUrl` = '{$iconUrl}' WHERE `account` = '{$oldAccount}'";
   
        //try to execute query. If it works, then send back success message.
        // else.. return error.
		if ($conn->query($sql) === TRUE)
		{
			$conn->close();
		    sendResultInfoAsJson("record updated successfully");
		}else{
		    $conn->close();
	    	returnWithError(  "failure to update record." );
		}

	}
	
        // auxiliary function for passing back JSON content to frontend.
	function getRequestInfo()
	{
		return json_decode(file_get_contents('php://input'), true);
	}

        // auxiliary function for setting headers and passing back JSON content to frontend.
	function sendResultInfoAsJson( $obj )
	{
		//header('Content-type: application/json');
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