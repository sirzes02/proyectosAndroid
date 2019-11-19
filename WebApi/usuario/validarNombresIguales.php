<?php
    // Se requiere de la base de datos una vez 
    require_once("../conectarBD.php");
    // Se obtiene la base de datos y se le envia un QUERY
    $consulta = conectarBD::conexion()->query("SELECT * FROM usuario");
    
    // Obtenemos un nombre enviado por GET
    $nombre = strtolower($_GET['nombre']);

    // Recorremos todas las filas de la consulta retornada
    while ($filas = $consulta->fetch(PDO::FETCH_ASSOC))
        // Si el nombre recibido es igual al nombre en la base de datos
        if(strtolower($filas['nom_usu']) == $nombre){
            // Se imprime 0 como true
            echo 0;
            // Se termina el PHP
            return;
        }
    // Se imprime 1 como false
    echo 1;
?>