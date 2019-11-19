<?php    
    // Se requiere de la base de datos una vez 
    require_once("../conectarBD.php");

    // Se reciben valores por GET
    $nombre = $_GET['nombre'];
    $contrasenia = $_GET['contrasenia'];

    // Se crea una lista de enteros donde estos representan las imagenes
    $listImg = array(
        2131165191, 
        2131165199, 
        2131165200, 
        2131165201, 
        2131165202, 
        2131165203, 
        2131165204, 
        2131165205, 
        2131165206, 
        2131165192, 
        2131165193, 
        2131165194, 
        2131165195, 
        2131165196, 
        2131165197, 
        2131165198);

    // Se mezcla la lista de las imagenes
    shuffle($listImg);
    // La imagen es igual a la primera posicion de la lista mezclada aleatoriamente
    $img = $listImg[0];

    // Se llama a la conexion y se le envia un QUERY
    conectarBD::conexion()->query("INSERT INTO usuario VALUES('$nombre', '$contrasenia', 0, '$img')");
?>