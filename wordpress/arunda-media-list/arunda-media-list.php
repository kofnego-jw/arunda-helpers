<?php
/*
Plugin Name: arunda-media-list
Plugin URI: http://www.arunda.it/wp-plugin/
Description: This plugin organizes the arunda media list
Version: 0.0.1-SNAPSHOT
Author: pepivampir
Author URI: http://pepivampir.wordpress.com/
*/

include_once('includes/arunda-media-classes.php');

function arunda_file_exist($path) {
    $uploadDir = wp_upload_dir();
    $filepath = $uploadDir['basedir'] . '/' . $path;
//    echo $filepath;
    return file_exists($filepath);
}

function arunda_file_size($path) {
	$uploadDir = wp_upload_dir();
	$filepath = $uploadDir['basedir'] . '/' . $path;
	$size = filesize($filepath);
	if (!$size) return "0";
	return number_format((float)($size / 1048576), 2, ',', '');
}

function arunda_outputmedialist($atts, $content) {

    global $arunda_medialist;

    if (is_null($arunda_medialist))
        $arunda_medialist = array();

    $a = shortcode_atts( array(
        'path' => '.',
    ), $atts );


    $entries = preg_split('/\s*<br\s*\/?>/i',$content);

    $path = $a['path'];

    $path = preg_replace('/\.\.\//','',$path);
    $path = preg_replace('/^(\/+)/','', $path);
    $path = preg_replace('/(\/+)$/','', $path);

    foreach ($entries as $entry) {
        if (preg_match('/\s*([^:]+):(.*)/', $entry, $groups)) {
            $fileName = trim($groups[1]);
            if (!arunda_file_exist($path."/".$fileName))
                continue;
            $myEntry = new ArundaMediaEntry();
            $myEntry->path = get_site_url() . "/wp-content/uploads/" . $path . "/" . $fileName;
            $thumbPath = $path . "/" . $fileName . ".thumb.jpg";
            if (arunda_file_exist($thumbPath)) {
                $myEntry->thumbPath = get_site_url() . "/wp-content/uploads/" . $thumbPath;
            } else {
                $myEntry->thumbPath = $myEntry->path;
            }
            $myEntry->name = $fileName;
            $myEntry->caption = trim($groups[2]);
            $myEntry->type = arunda_getFiletype($fileName);
            array_push($arunda_medialist, $myEntry);
        }
    }

}


add_shortcode("arunda_media", "arunda_outputmedialist");



