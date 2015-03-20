<?php
require_once('/home/osoft/i_ud/info/debug/info_debug.php');
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/i_ud/info/security/sectok.php');
require_once('/home/osoft/i_ud/db/db_ud.php');

logmsg_info('user.php');

function get_user_json($in_uid, $in_level)
{
  $json = '"user" : { "uid" : "'.$in_uid.'", "level" : "'.$in_level.'" }';
  return $json;
}


function set_user($in_new_uid, $in_level)
{
  $uid = sha1($in_new_uid);
  $uid = base64_encode($uid);
  $uid = substr($uid, 0, 64);

  try {
     $conn = dbOpen();

     #execute the stored procedure
     $sp_args = ":in_uid,:in_level";
     $stmt = $conn->prepare("CALL sp_set_user(".$sp_args.")");
     $stmt->bindParam(':in_uid', $uid, PDO::PARAM_STR, 64); 
     $stmt->bindParam(':in_level', $in_level, PDO::PARAM_INT); 
     $stmt->execute();

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
  return $uid;
}


function is_user_valid($in_uid)
{
  try {
     $conn = dbOpen();

     #execute the stored procedure
     $sp_args = ":in_uid";
     $stmt = $conn->prepare("CALL sp_is_user_valid(".$sp_args.")");
     $stmt->bindParam(':in_uid', $in_uid, PDO::PARAM_STR, 64); 
     $stmt->execute();

     $row = $stmt->fetch();
     logmsg_info('is_user_valid: json(row): '.json_encode($row));
  
     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
  return $row['c'];
}
?>
