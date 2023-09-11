<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'show_data_riwayat':
                $nama = $_POST['nama'];

                $sql = "SELECT * FROM sampah WHERE nama LIKE '%$nama%' ORDER BY tgl_penjemputan DESC";
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
            case 'saldo':
                $sql = "SELECT SUM(harga) AS saldo FROM sampah";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $riwayat = mysqli_fetch_assoc($result);

                    echo json_encode($riwayat);
                    exit();
                } else {
                    $respon['kode']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'detail':
                $id_sampah = $_POST['id_sampah'];

                $sql = "SELECT nama, kategori, berat, harga, tgl_penjemputan, alamat, catatan, status_bayar, CONCAT('$http_b', bukti_bayar) AS bukti
                FROM sampah WHERE id_sampah = '$id_sampah'";
                $result = mysqli_query($conn,$sql);

                if(mysqli_num_rows($result)>0){
                    header("Access-Control-Allow-Origin: *");
                    header("Content-Type: application/json");
                    $data = mysqli_fetch_assoc($result);
                    
                    echo json_encode($data); 
                    exit();
                }else{
                    $respon['kode']= "0";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'get_kategori':
                $sql = "SELECT kategori FROM kategori";
                $result = mysqli_query($conn,$sql);
                if (mysqli_num_rows($result)>0) {
                    header("Access-Control-Allow-Origin: *");
                    header("Content-type: application/json; charset=UTF-8");

                    $nama_kategori = array();
                        while($nama = mysqli_fetch_assoc($result)){
                            array_push($nama_kategori, $nama);
                        }
                    echo json_encode($nama_kategori);
                }
                break;
        }
    }

?>