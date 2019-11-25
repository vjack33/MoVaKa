<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['phoneno']) && isset($_POST['altphoneno']) && isset($_POST['email']) && isset($_POST['deviceid1']) && isset($_POST['deviceid2']) && isset($_POST['password'])) {
    
    // receiving the post params
    $firstname = $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $phoneno = $_POST['phoneno'];
    $altphoneno = $_POST['altphoneno'];
    $email = $_POST['email'];
    $deviceid1 = $_POST['deviceid1'];
    $deviceid2 = $_POST['deviceid2'];
    $password = $_POST['password'];

    // check if user is already existed with the same email
    if ($db->isUserExisted($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($firstname, $lastname, $phoneno, $altphoneno, $email, $deviceid1, $deviceid2, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["firstname"] = $user["firstname"];
            $response["user"]["lastname"] = $user["lastname"];
            $response["user"]["phoneno"] = $user["phoneno"];
            $response["user"]["altphoneno"] = $user["altphoneno"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["deviceid1"] = $user["deviceid1"];
            $response["user"]["deviceid2"] = $user["deviceid2"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

