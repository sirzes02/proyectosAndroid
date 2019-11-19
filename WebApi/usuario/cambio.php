<?php
    // Se requiere de la base de datos una vez 
    require_once("../conectarBD.php");

    // Se reciben valores por GET
    $nombre = $_GET['nombre'];
    $dato = $_GET['dato'];
    $atributo = $_GET['atributo'];

    /*
    * Segun el atributo escogido:
    * 1 -> Cambio de contraseña
    * 2 -> Cambio de puntaje
    * 3 -> Cambio de imagen
    */

    switch ($atributo) {
        case 1:
            // Se llama a la conexion y se le envia un QUERY
            conectarBD::conexion()->query("UPDATE usuario SET con_usu='$dato' WHERE UPPER(nom_usu) = UPPER('$nombre')");
            return;
        case 2:
            conectarBD::conexion()->query("UPDATE usuario SET pun_usu=$dato WHERE UPPER(nom_usu) = UPPER('$nombre')");
            return;
        case 3:
            conectarBD::conexion()->query("UPDATE usuario SET img_usu='$dato' WHERE UPPER(nom_usu) = UPPER('$nombre')");
            return;
    }
?>