<?php

    //grabbing the data passed in as a json
	$inData = getRequestInfo();

	
 	$account = $inData["account"];

 	$username = $inData["username"];
	$password = $inData["password"];
	$favorite = $inData["favorite"];
	$favorite = (int)$favorite;
 	$url = $inData["url"];
 	$iconUrl = $inData["iconUrl"];
 	$userId = $inData["userId"];
 	$userId = (int)$userId;

	
	
	// initializes utility variables for login account.
 	$id = 0;
 	$email = $inData["account"];
 	$email = (string)$email;
	$login = $inData["favorite"];
	$login = (int)$login;


    // Manual SQL connection
	$conn = new mysqli("localhost", "passwords", "Processes2018!", "password_COP4331");
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{
	    $ucount_query = $conn->query("select id from passwords");//gets all IDs
        $ucount = mysqli_num_rows($ucount_query);
        $ucount+=1;	
        

		$sql = "INSERT INTO `passwords` (`id`, `userId`, `account`, `username`, `password`, `favorite`, `url`, `iconUrl`) VALUES (".$ucount.", ".$userId.", '{$account}', '{$username}', '{$password}', ".$favorite.", '{$url}', '{$iconUrl}')";


        // save results of query execution
        // if it works, return account details.
        // else, return error that nothing was found.
		$result = $conn->query($sql);
		if ($result != TRUE)
		{
			
			returnWithError( "duplicate ID key" );        
		}
		$conn->close();
		
		returnWithError("No Error");
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
