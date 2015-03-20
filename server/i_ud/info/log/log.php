<?php
function logmsg_info($msg) {
    error_log($msg."\n", 3, '/home/osoft/www/ud/logmsg_info.txt');
}
?>
