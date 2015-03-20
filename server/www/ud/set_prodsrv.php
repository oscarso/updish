<?php
require_once('/home/osoft/i_ud/info/debug/info_debug.php');
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/i_ud/info/security/sectok.php');
require_once('/home/osoft/i_ud/db/db_ud.php');
require_once('/home/osoft/www/ud/set_vote.php');

logmsg_info('set_prodsrv.php');
$data = json_decode(file_get_contents('php://input'), true);

$sectok = $data['SECTOKEN'];
logmsg_info('set_prodsrv: sectok: '.$sectok);
$uid = verify_sectok($sectok);
$gplace_id = $data['gplace_id'];
$uname_en = $data['uname_en'];
$item_types = $data['item_types'];
$ccs = $data['ccs'];
$b64_photo_file = $data['photo_file'];

logmsg_info('           uid: '.$uid);
logmsg_info('     gplace_id: '.$gplace_id);
logmsg_info('      uname_en: '.$uname_en);
logmsg_info('    item_types: '.$item_types);
logmsg_info('           ccs: '.$ccs);
/*logmsg_info('b64_photo_file: '.$b64_photo_file);*/

$photo_file = base64_decode($b64_photo_file);
$photo_file_name = sha1($uname_en.$b64_photo_file).'.png';
logmsg_info('photo_file_name: '.$photo_file_name);
$photo_file_path = "images/".$gplace_id."/".$photo_file_name;
logmsg_info('photo_file_path: '.$photo_file_path);
file_put_contents($photo_file_path, $photo_file);

$id_hash = sha1($uid.$photo_file_path.time());
$prod_id = set_prodsrv("NONE",$item_types,$ccs,$uname_en,"NONE",$id_hash);
logmsg_info('set_prodsrv returns prod_id: '.$prod_id);

set_place_prodsrv($gplace_id,$prod_id);

$url_photo_path = 'http://www.oscarsoft.co/ud/'.$photo_file_path;
logmsg_info('url_photo_path: '.$url_photo_path);
set_place_photo($gplace_id,$url_photo_path,$prod_id);

set_vote($uid,$gplace_id,$prod_id);

echo '[]';
?>

<?php
function set_prodsrv(
        $in_gtype_id,
        $in_item_type,
        $in_country_id,
	$in_uname_en,
	$in_iname_en,
	$in_id_hash)
{
  try {
     $conn = dbOpen();

     logmsg_info('set_prodsrv:  in_gtype_id: '.$in_gtype_id);
     logmsg_info('             in_item_type: '.$in_item_type);
     logmsg_info('            in_country_id: '.$in_country_id);
     logmsg_info('              in_uname_en: '.$in_uname_en);
     logmsg_info('              in_iname_en: '.$in_iname_en);
     logmsg_info('               in_id_hash: '.$in_id_hash);

     #execute the stored procedure
     $sp_args = ":in_gtype_id,:in_item_type,:in_country_id,:in_uname_en,:in_iname_en,:in_id_hash";
     $stmt = $conn->prepare("CALL sp_set_prodsrv(".$sp_args.")");
     $stmt->bindParam(':in_gtype_id', $in_gtype_id, PDO::PARAM_STR, 64);
     $stmt->bindParam(':in_item_type', $in_item_type, PDO::PARAM_STR, 64);
     $stmt->bindParam(':in_country_id', $in_country_id, PDO::PARAM_STR, 2);
     $stmt->bindParam(':in_uname_en', $in_uname_en, PDO::PARAM_STR, 256);
     $stmt->bindParam(':in_iname_en', $in_iname_en, PDO::PARAM_STR, 256);
     $stmt->bindParam(':in_id_hash', $in_id_hash, PDO::PARAM_STR, 64);
     $stmt->execute();

     $stmt = $conn->prepare("CALL sp_get_prodid_by_hashid(:in_id_hash)");
     $stmt->bindParam(':in_id_hash', $in_id_hash, PDO::PARAM_STR, 64);
     $stmt->execute();
     $row = $stmt->fetch();
 
     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }

  return $row[0];
}

function set_place_prodsrv(
        $in_gplace_id,
        $in_prod_id)
{
  try {
     $conn = dbOpen();

     logmsg_info('set_place_prodsrv:  in_gplace_id: '.$in_gplace_id);
     logmsg_info('                      in_prod_id: '.$in_prod_id);

     #execute the stored procedure
     $stmt = $conn->prepare("CALL sp_set_place_prodsrv(:in_gplace_id,:in_prod_id)");
     $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64);
     $stmt->bindParam(':in_prod_id', $in_prod_id, PDO::PARAM_INT);
     $stmt->execute();

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
}

function set_place_photo(
        $in_gplace_id,
	$in_photo_path,
        $in_prod_id)
{
  try {
     $conn = dbOpen();

     logmsg_info('set_place_photo:  in_gplace_id: '.$in_gplace_id);
     logmsg_info('                 in_photo_path: '.$in_photo_path);
     logmsg_info('                    in_prod_id: '.$in_prod_id);
     
     #execute the stored procedure
     $sp_args = ":in_gplace_id,:in_photo_path,:in_prod_id";
     $stmt = $conn->prepare("CALL sp_set_place_photo(".$sp_args.")");
     $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64);
     $stmt->bindParam(':in_photo_path', $in_photo_path, PDO::PARAM_STR, 1024);
     $stmt->bindParam(':in_prod_id', $in_prod_id, PDO::PARAM_INT);
     $stmt->execute();

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
}
?>
