<?php
  $connection = mysqli_connect("localhost","user","pass","homeinformation") or die("Error " . mysqli_error($connection));


$sql="SELECT * FROM Icecream";

 $result = mysqli_query($connection, $sql) or die("Error in Selecting " . mysqli_error($connection));
 

   $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        
        $emparray[] = $row;
    }
    $jsonString=json_encode($emparray);
    echo str_replace('\/','/',$jsonString);
    
   
    //close the db connection
    mysqli_close($connection);
?>