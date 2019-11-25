<?php
$conn = mysqli_connect("localhost","root","","android") or die(mysql_error());
$deviceid = $_GET['deviceid'];
//execute query
$result = $conn->query("SELECT bpm, tempf, time1 FROM readings WHERE devid ='$deviceid' ORDER BY time1 DESC");
//loop through returned data
$data = array();
foreach ($result as $row) {
	$data[] = $row;
}
//free memory associated with result
$result->close();
mysqli_close($conn);
// print the data
print json_encode($data);
?>