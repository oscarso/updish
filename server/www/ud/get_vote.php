<?php
require_once('/home/osoft/i_ud/info/debug/info_debug.php');
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/i_ud/info/security/sectok.php');
require_once('/home/osoft/i_ud/db/db_ud.php');

logmsg_info('get_vote.php');
$data = json_decode(file_get_contents('php://input'), true);
if ($data != NULL) {
   logmsg_info('get_vote: Received JSON');
   $sectok = $data['SECTOKEN'];
   logmsg_info('get_vote: sectok: '.$sectok);
   $uid = verify_sectok($sectok);
   $gplace_id = $data["gplace_id"];
} else {
   logmsg_info('get_vote: Received HTTP GET');
   $uid = $_GET['uid'];
   $gplace_id = $_GET['gpid'];
}
logmsg_info('uid: '.$uid);
logmsg_info('gplace_id: '.$gplace_id);

$json_v = getProductServiceVote($gplace_id, $uid);
$v = json_decode($json_v);

echo $json_v;
?>

<?php
function getProductServiceVote($gplace_id, $user_id) {
  try {
     $conn = dbOpen();

     #execute the stored procedure
     $stmt = $conn->prepare("CALL sp_get_vote(:in_gplace_id,:in_user_id)");
     $stmt->bindParam(':in_gplace_id', $gplace_id, PDO::PARAM_STR, 64); 
     $stmt->bindParam(':in_user_id', $user_id, PDO::PARAM_STR, 64); 
     $stmt->execute();

     $i = 0;
     $arr_v = array();
     while ($row = $stmt->fetch()) {
        $arr_v[$i] = $row;
        $i++;
     }
     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
  return json_encode($arr_v);
}
?>
