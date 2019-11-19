<?php
    // Se requiere de la base de datos una vez 
    require_once("../conectarBD.php");
    // Se obtiene la base de datos y se le envia un QUERY
    $consulta = conectarBD::conexion()->query("SELECT * FROM pregunta");

    // Recorremos todas las filas de la consulta retornada
    while ($filas = $consulta->fetch(PDO::FETCH_ASSOC))
        // Se imprime los datos de las preguntas [titulo <> respuestas <> verdadera] %% [titulo - respuestas - verdadera], ....
        echo $filas['tit_pre'], "<>", $filas['res_pre'], "<>", $filas['ver_pre'], "%%";
?>