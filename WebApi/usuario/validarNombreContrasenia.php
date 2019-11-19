<?php
    // Se requiere de la base de datos una vez 
    require_once("../conectarBD.php");
    // Se obtiene la base de datos y se le envia un QUERY
    $consulta = conectarBD::conexion()->query("SELECT * FROM usuario");
    
    // Obtenemos un nombre enviado por GET
    $nombre = strtolower($_GET['nombre']);

    // Recorremos todas las filas de la consulta retornada
    while ($filas = $consulta->fetch(PDO::FETCH_ASSOC))
        // Si un nombre en la base de datos es igual al nombre enviado por GEt y la contraseña corresponde a este nombre 
        if(strtolower($filas['nom_usu']) == $nombre && $filas['con_usu'] == $_GET['contrasenia']){
            // Se imprime 0 como true
            echo 0;
            // Se termina el PHP
            return;
        }
    // Se imprime 1 como false
    echo 1;
?>