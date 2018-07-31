
<?php



	// get in the data from json
	$inData = getRequestInfo();
	

	$id = $inData["id"];
	$username = $inData["username"];
    $password = $inData["password"];
    $url = $inData["url"];
    $iconUrl = $inData["iconUrl"];
    $favorite = $inData["favorite"];
    $userId = $inData["userId"];
    $account = $inData["account"];
    

    
    // Manual SQL connection
	$conn = new mysqli("localhost", "passwords", "Processes2018!", "password_COP4331");
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
        
        // build query
        // This checks whether anything was passed in for those fields.
        // if nothing was passed in, skip. ELSE append to query.
        // substr part is important. It removes the extra comma if no description is added.

        $sql = "UPDATE passwords SET";
        
        if($url != "")
        $sql .= " url = ' . $url . ',";
        
        if($username != "")
        $sql .= " username = ' . $username . ',";
        
        if($password != "")
        $sql .= " password = ' . $password . ',";
        
        if($favorite != "")
        $sql .= " favorite = ' . $favorite . '";
        
        if($iconUrl != "")
        $sql .= " iconUrl = ' . $iconUrl . '";
        
        if($account != "")
        $sql .= " account = ' . $account . '";
        
        else
        $sql = substr($sql, strlen($sql)-1);
        
        
        $sql .= " WHERE 'passowrds' . 'userId = ' . $userId . ' AND id = ' . $id .'";
        
 
      $sql =  "UPDATE `passwords` SET `account` = '{$account}', `username` = '{$username}', `password` = '{$password}', `favorite` = '{$favorite}', `url` = '{$url}', `iconUrl` = '{$iconUrl}' WHERE `passwords`.`id` = '{$id}'";

    
        //try to execute query. If it works, then send back success message.
        // else.. return error.
		if ($conn->query($sql) != TRUE)
		{
			
		    returnWithError( "failure to update record." );
		}
		$conn->close();
		
		returnWithError( "ok?" );

	} 
	

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

