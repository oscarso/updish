<?php
require_once('/home/osoft/i_ud/info/debug/info_debug.php');
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/i_ud/info/security/sectok.php');
require_once('/home/osoft/i_ud/db/db_ud.php');

logmsg_info('set_vote.php');
$data = json_decode(file_get_contents('php://input'), true);

$sectok = $data['SECTOKEN'];
logmsg_info('set_vote: sectok: '.$sectok);
$uid = verify_sectok($sectok);
$gpid = $data['gplace_id'];
$prdid = $data['prod_id'];

logmsg_info('uid: '.$uid);
logmsg_info('gpid: '.$gpid);
logmsg_info('prdid: '.$prdid);

set_vote($uid, $gpid, $prdid);

header_remove();
$url = "http://oscarsoft.co/ud/get_vote.php?gpid=".$gpid."&uid=".$uid;
logmsg_info('url: '.$url);
echo file_get_contents($url);
?>

<?php
function set_vote(
	$in_user_id,
	$in_gplace_id,
	$in_prod_id)
{
  try {
     $conn = dbOpen();

     #execute the stored procedure
     $sp_args = ":in_gplace_id,:in_prod_id,:in_user_id";
     $stmt = $conn->prepare("CALL sp_set_vote(".$sp_args.")");
     $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64); 
     $stmt->bindParam(':in_prod_id', $in_prod_id, PDO::PARAM_STR, 64); 
     $stmt->bindParam(':in_user_id', $in_user_id, PDO::PARAM_STR, 64); 
     $stmt->execute();

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
}
?>
