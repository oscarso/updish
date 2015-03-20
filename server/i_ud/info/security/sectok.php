<?php
require_once('/home/osoft/i_ud/info/log/log.php');
require_once('/home/osoft/www/ud/user.php');

function verify_sectok($sectok) {
   logmsg_info('verify_sectok');
   $tokdata = json_decode($sectok);

   if ($tokdata != null) {
      logmsg_info('verify_sectok: tokdata->uid: '.$tokdata->{'uid'});
      if (is_user_valid($tokdata->{'uid'}) >= 1) {
         return $tokdata->{'uid'};
      } 
   }
   return null;
}
?>
