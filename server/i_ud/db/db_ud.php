<?php
function dbOpen() {
  $host = "localhost";
  $dbname = "osoft_ud";
  $username = "osoft_uduser";
  $password = "ud1111!!";

  try {
     $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
  return $conn;
}

function dbClose($conn) {
  $conn = null;
}
?>
