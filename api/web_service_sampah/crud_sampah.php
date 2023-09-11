<?php 

include "koneksi.php";

    if($_SERVER['REQUEST_METHOD']=='POST'){

        $mode = $_POST['mode'];
        $respon = array();
        $respon['respon']= '0';
        switch($mode){
            case 'insert':
                $nama = $_POST['nama'];
                $kategori = $_POST['kategori'];
                $berat = $_POST['berat'];
                $harga = $_POST['harga'];
                $tgl_penjemputan = $_POST['tgl_penjemputan'];
                $alamat = $_POST['alamat'];
                $catatan = $_POST['catatan'];

                $sql = "INSERT INTO sampah(nama, kategori, berat, harga, tgl_penjemputan, alamat, catatan, status_bayar)
                    VALUES('$nama','$kategori','$berat','$harga','$tgl_penjemputan','$alamat','$catatan','Belum bayar')";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'delete':
                $id_sampah = $_POST['id_sampah'];

                $sql = "DELETE FROM sampah WHERE id_sampah = '$id_sampah'";
                $result = mysqli_query($conn,$sql);
                
                $respon['respon']= "1";
                echo json_encode($respon);
                exit();
                break;
            case 'edit':
                $id_sampah = $_POST['id_sampah'];
                $nama = $_POST['nama'];
                $kategori = $_POST['kategori'];
                $berat = $_POST['berat'];
                $harga = $_POST['harga'];
                $tgl_penjemputan = $_POST['tgl_penjemputan'];
                $alamat = $_POST['alamat'];
                $catatan = $_POST['catatan'];

                $sql = "UPDATE sampah SET nama = '$nama', kategori = '$kategori', berat = '$berat', harga = '$harga',
                    tgl_penjemputan = '$tgl_penjemputan', alamat = '$alamat', catatan = '$catatan' WHERE id_sampah = '$id_sampah'";
                $result = mysqli_query($conn,$sql);
                if ($result) {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
            case 'bayar':
                $id_sampah = $_POST['id_sampah'];
                $imstr = $_POST['image'];
                $file = $_POST['file'];
                $path = "bukti/";

                $sql = "UPDATE sampah SET bukti_bayar = '$file', status_bayar = 'Sudah bayar' WHERE id_sampah = '$id_sampah'";
                $result = mysqli_query($conn,$sql);

                if(file_put_contents($path.$file, base64_decode($imstr))==false){
                    $respon['respon']= "0";
                    echo json_encode($respon);
                    exit();
                } else {
                    $respon['respon']= "1";
                    echo json_encode($respon);
                    exit();
                }
                break;
        }
    }

?>