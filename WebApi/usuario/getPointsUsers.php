<?php
    // Se requiere de la base de datos una vez 
    require_once("../conectarBD.php");
    // Se obtiene la base de datos y se le envia un QUERY
    $consulta = conectarBD::conexion()->query("SELECT * FROM usuario");

    // Recorremos todas las filas de la consulta retornada
    while ($filas = $consulta->fetch(PDO::FETCH_ASSOC))
        // Se imprime los datos de los usuario [nombre - puntaje - imagen], [nombre - puntaje - imagen], ....
        echo $filas['nom_usu']. "-". $filas['pun_usu']. "-". $filas['img_usu']. ",";
?>