<?php
/**
 * Created by PhpStorm.
 * User: totoro
 * Date: 06.09.15
 * Time: 10:21
 */

class ArundaMediaEntry {
    public $path;
    public $thumbPath;
    public $name;
    public $caption;
    public $type;
}

function arunda_getFiletype($filename) {
    $filename = strtolower($filename);
    if (endsWith($filename,'.jpeg') || endsWith($filename,'.jpg'))
        return "JPEG";
    if (endsWith($filename,'.pdf') )
        return "PDF";
    if (endsWith($filename, '.mp3'))
        return "MP3";
    return "UNKNOWN";
}

function startsWith($haystack, $needle)
{
    $length = strlen($needle);
    return (substr($haystack, 0, $length) === $needle);
}

function endsWith($haystack, $needle)
{
    $length = strlen($needle);
    if ($length == 0) {
        return true;
    }

    return (substr($haystack, -$length) === $needle);
}



