<?php
require_once('/home/osoft/i_ud/info/debug/info_debug.php');
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/i_ud/info/security/sectok.php');
require_once('/home/osoft/i_ud/info/api/api_google.php');
require_once('/home/osoft/i_ud/db/db_ud.php');
require_once('/home/osoft/i_ud/lib/maxmind/info_maxmind.php');
require_once('/home/osoft/i_ud/lib/maxmind/geoipcity.inc');
require_once('/home/osoft/i_ud/lib/maxmind/geoipregionvars.php');
require_once('/home/osoft/i_ud/lib/google/googlePlaces.php');
require_once('/home/osoft/www/ud/user.php');

logmsg_info('get_place.php');
$data = json_decode(file_get_contents('php://input'), true);

$sectok = $data['SECTOKEN'];
logmsg_info('get_place: sectok: '.$sectok);
$uid = verify_sectok($sectok);

$r_mile = $data['dist'];
$lat = $data['lat'];
$lng = $data['lng'];

if ($uid == "") {
   logmsg_info('uid is empty. Request for new uid');
   $uid = $lat.$lng.time();
   $uid = set_user($uid,1);
}

is_user_valid($uid);
logmsg_info('   uid: '.$uid);
logmsg_info('r_mile: '.$r_mile);
logmsg_info('   lat: '.$lat);
logmsg_info('   lng: '.$lng);

$json_place_local = getPlacesLocal($lat, $lng, $r_mile);
$place_local = json_decode($json_place_local);
if (($m == $MODE_DEBUG) ||
    (sizeof($place_local) == 0)) { 
   if ($m == $MODE_DEBUG) {
     print("Not found in our local database, trying Google Places API...<br>");
   }
   $r_feet = $r_mile * 5280;
   $place_extern = getPlacesExtern($lat, $lng, $r_feet);
   addPlaces($place_extern);
   $json_place_local = getPlacesLocal($lat, $lng, $r_mile);
}
if ($m == $MODE_DEBUG) {
   print("json_place_local: ");
}

$json_user = get_user_json($uid, 1);
$json = '{'.$json_user.', "places" : '.$json_place_local.'}';
logmsg_info('json: '.$json);

echo $json;

/*
getPlacesLocal($lat, $lng, $r);
$places = getPlacesExtern($lat, $lng, $r);
print("Please let me know if I am right ?<br>");
print("<br>First, you are in ".$locrec->city." of ".$locrec->region." in ".$locrec->country_name.".<br>");
print("And, let me guest...<br>");
print("<br>");
print("Here are the restaurants within ".$r." meters from where you are located:<br>");
printPlaces($places);
print("<br>");
print("Am I right ?<br>");
*/
?>

<?php
function getIP() {
 //This returns the True IP of the client calling the requested page
 // Checks to see if HTTP_X_FORWARDED_FOR 
 // has a value then the client is operating via a proxy
 $userIP = $_SERVER['HTTP_X_FORWARDED_FOR'];
 if($userIP == "")
  {
     $userIP = $_SERVER['REMOTE_ADDR'];
  }
  // return the IP we've figured out:
  return $userIP;
}

function getLocRec($ip) {
  $gi = geoip_open($PATH_GEOLITECITY_DAT, GEOIP_STANDARD);
  $locrec = geoip_record_by_addr($gi, $ip);
  geoip_close($gi); 
  return $locrec;
}

function getPlacesLocal($lat, $lng, $r) {
  try {
     $conn = dbOpen();

     #execute the stored procedure
     $stmt = $conn->prepare("CALL sp_get_place_in_r(:in_lat,:in_lng,:in_r)");
     $stmt->bindParam(':in_lat', $lat, PDO::PARAM_INT); 
     $stmt->bindParam(':in_lng', $lng, PDO::PARAM_INT); 
     $stmt->bindParam(':in_r', $r, PDO::PARAM_INT); 
     $stmt->execute();

    $i = 0;
     $arr_place = array();
     while ($row = $stmt->fetch()) {
        $arr_place[$i] = $row;
        $i++;
     }
     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
  /*$arrv_place = array_values($arr_place);
  print_r($arrv_place);*/
  return json_encode($arr_place);
}

function getPlacesExtern($lat, $lng, $r) {
  $googlePlaces = new googlePlaces($APIKEY_GOOGLE);
  $googlePlaces->setLocation($lat . ',' . $lng);
  /*
     https://developers.google.com/places/documentation/supported_types?hl=vi
  */
  $googlePlaces->setTypes('restaurant|cafe|establishment');
  $googlePlaces->setRadius($r);
  
  $results = $googlePlaces->Search();
  return $results;
}

function printPlaces($places) {
  $i = 0;
  while ($places["result"][$i]["name"]) {
    $item = $i + 1;
    print("#".$item." place_id:".$places["result"][$i]["place_id"]." ".$places["result"][$i]["name"].'<br>');
    print_r($places["result"][$i]["types"]);
    print("LATITUDE: ".$places["result"][$i]["geometry"]["location"]["lat"]."; ");
    print("LONGTITUDE: ".$places["result"][$i]["geometry"]["location"]["lng"]."<br>");
    $i++;
  }
}

function addPlaces($places) {
  $i = 0;
  try {
     $conn = dbOpen();

     while ($places["result"][$i]["name"]) {
       $item = $i + 1;
       $in_gplace_id = $places["result"][$i]["place_id"];
       $in_name = $places["result"][$i]["name"];
       $in_lat = $places["result"][$i]["geometry"]["location"]["lat"];
       $in_lng = $places["result"][$i]["geometry"]["location"]["lng"];
       $arr_types = $places["result"][$i]["types"];
       
       //print("#".$item." place_id:".$in_gplace_id." ".$in_name.'<br>');
       //print("LATITUDE: ".$in_lat."; ");
       //print("LONGTITUDE: ".$in_lng."<br>");

       #execute the stored procedure
       $sp_args = ":in_gplace_id,:in_name,:in_lat,:in_lng";
       $stmt = $conn->prepare("CALL sp_set_place(".$sp_args.")");
       $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64); 
       $stmt->bindParam(':in_name', $in_name, PDO::PARAM_STR, 512); 
       $stmt->bindParam(':in_lat', $in_lat, PDO::PARAM_INT); 
       $stmt->bindParam(':in_lng', $in_lng, PDO::PARAM_INT); 
       $stmt->execute();

       //print_r($arr_types);
       $t = 0;
       while ($t < count($arr_types)) {
         $in_gplace_id = $places["result"][$i]["place_id"];
         $in_gtype_id = $arr_types[$t];
         //print("<br>t: ".$t." PLACE: ".$in_gplace_id." TYPE: ".$in_gtype_id."<br>");
         $stmt = $conn->prepare("CALL sp_set_place_type(:in_gplace_id,:in_gtype_id)");
         $stmt->bindParam(':in_gplace_id', $in_gplace_id, PDO::PARAM_STR, 64); 
         $stmt->bindParam(':in_gtype_id', $in_gtype_id, PDO::PARAM_STR, 64); 
         $stmt->execute();
         $t++;
       }

       $i++;
     }

     dbClose($conn);

  } catch (PDOException $pe) {
     print "Error occurred:".$pe->getMessage()."<br>";
  }
}
?>
