<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_sampah':
                $jenis = $_POST['jenis'];

                $sql = "SELECT id, jenis, CONCAT('$http_s', foto_sampah) AS foto
                    FROM foto_sampah WHERE jenis = '$jenis'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data_sampah = array();
                    while ($data = mysqli_fetch_assoc($result)) {
                        array_push($data_sampah, $data);
                    }
                    echo json_encode($data_sampah);
                    exit();
                } else {
                    $data_sampah = array();
                    echo json_encode($data_sampah);
                }
                break;
            case 'insert':
                $jenis = $_POST['jenis'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "gambar/";
                
                $sql = "INSERT INTO foto_sampah(jenis, foto_sampah) VALUES('$jenis','$file')";
                $result = mysqli_query($conn,$sql);

                if ($result) {
                    if(file_put_contents($path.$file, base64_decode($imstr))==false){
                        $respon['respon']= "0";
                        echo json_encode($respon);
                        exit();
                    } else {
                        $respon['respon']= "1";
                        echo json_encode($respon);
                        exit();
                    }
                }
                break;
            case 'delete':
                $id = $_POST['id'];

                $sql = "DELETE FROM foto_sampah WHERE id = '$id'";
                $result = mysqli_query($conn,$sql);
                
                $respon['respon']= "2";
                echo json_encode($respon);
                exit();
                break;
        }
    }

?>