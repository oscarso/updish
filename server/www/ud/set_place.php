<?php
require_once('/home/osoft/i_ud/info/debug/info_debug.php');
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/i_ud/info/security/sectok.php');
require_once('/home/osoft/i_ud/db/db_ud.php');

logmsg_info('set_place.php');
$data = json_decode(file_get_contents('php://input'), true);

$sectok = $data['SECTOKEN'];
logmsg_info('set_place: sectok: '.$sectok);
$uid = verify_sectok($sectok);
$name = $data['name'];
$lat = $data['lat'];
$lng = $data['lng'];
$delim_types = $data['types'];
$delim_ccs = $data['ccs'];

/*
   gplace_id = sha1($name.$DELIM.$lat.$DELIM.$lng)
*/
$DELIM = '|';
$gplace_id = sha1($name.$DELIM.$lat.$DELIM.$lng);

logmsg_info('        uid: '.$uid);
logmsg_info('  gplace_id: '.$gplace_id);
logmsg_info('       name: '.$name);
logmsg_info('delim_types: '.$delim_types);
logmsg_info('  delim_ccs: '.$delim_ccs);
logmsg_info('        lat: '.$lat);
logmsg_info('        lng: '.$lng);

set_place($gplace_id, $name, $lat, $lng);

$types = explode("|", $delim_types);
for ($i=0; $i < count($types); $i++) {
   logmsg_info("set_place_type(".$gplace_id.", ".$types[$i]);
   set_place_type($gplace_id,$types[$i]);
}

$arr_ret_data = array();
$arr_ret_data[0] = array('gplace_id' => $gplace_id);
$json = json_encode($arr_ret_data);
logmsg_info('set_place: ret_data json: '.$json);

$new_dir = "images/".$gplace_id;
logmsg_info('set_place: new_dir: '.$new_dir);
mkdir($new_dir, 0755, true);

echo $json;
?>

<?php
function set_place(
	$in_gplace_id,
	$in_name,
	$in_lat,
        $in_lng)
{
  try {
     $conn = dbOpen();

     #execute the stored procedure
     $sp_args = ":in_gplace_id,:in_name,:in_lat,:in_lng";
     $stmt = $conn->prepare("CALL sp_set_place(".$sp_args.")");
     $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64); 
     $stmt->bindParam(':in_name', $in_name, PDO::PARAM_STR, 512); 
     $stmt->bindParam(':in_lat', $in_lat, PDO::PARAM_INT);
     $stmt->bindParam(':in_lng', $in_lng, PDO::PARAM_INT);
     $stmt->execute();

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
}

function set_place_type(
	$in_gplace_id,
	$in_type)
{
  try {
     $conn = dbOpen();

     #execute the stored procedure
     $stmt = $conn->prepare("CALL sp_set_place_type(:in_gplace_id,:in_type)");
     $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64); 
     $stmt->bindParam(':in_type', $in_type, PDO::PARAM_STR, 64); 
     $stmt->execute();

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
}
?>
