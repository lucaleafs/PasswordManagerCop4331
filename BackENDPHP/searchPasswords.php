<?php



	$inData = getRequestInfo();
	
    $accountInput = $inData["account"];
    $userIdInput = $inData["userId"];

	$conn = new mysqli("localhost", "passwords", "Processes2018!", "password_COP4331");
	if ($conn->connect_error) 
	{
		returnWithError( $conn->connect_error );
	} 
	else
	{

	    $sql = "SELECT * FROM `passwords` WHERE `userId` = '{$userIdInput}' AND `account` LIKE '{$accountInput}'";


		$result = $conn->query($sql);
		if ($result->num_rows > 0)
		{
			$row = $result->fetch_assoc();
			$url = $row["url"];
			$iconUrl = $row["iconUrl"];
			$username = $row["username"];
			$password = $row["password"];
			$favorite = $row["favorite"];
			$id = $row["id"];
			$account = $row["account"];
			
		    returnWithInfo($url, $iconUrl, $id, $username, $favorite,$password,$account );

		}
		else
		{
			returnWithError( "No Records Found" );
		}
		$conn->close();
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
	
	function returnWithError( $err )
	{
	    
		$retValue = '{"results":[],"error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	
	function  returnWithInfo($url, $iconUrl, $id, $username, $favorite,$password,$account )
	{

		$temp = '{"id":' . $id . ',"username":"' . $username . '","password":"' . $password . '","account":"' . $account . '","url":"' . $url . '","iconUrl":"' . $iconUrl . '","error":""}';
		
	    $retValue = '{"results":[' . $temp . '],"error":""}';

		sendResultInfoAsJson( $retValue );
	}
	
?>