-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 15, 2023 at 08:10 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sampah`
--

-- --------------------------------------------------------

--
-- Table structure for table `foto_sampah`
--

CREATE TABLE `foto_sampah` (
  `id` int(11) NOT NULL,
  `jenis` varchar(15) DEFAULT NULL,
  `foto_sampah` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `foto_sampah`
--

INSERT INTO `foto_sampah` (`id`, `jenis`, `foto_sampah`) VALUES
(1, 'Sampah Padat', 'IMG20230615041234.jpg'),
(3, '', ''),
(4, 'Sampah Basah', 'IMG20230615042227.jpg'),
(5, 'Sampah Basah', 'IMG20230615042246.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` int(11) NOT NULL,
  `kategori` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `kategori`) VALUES
(1, 'Plastik'),
(2, 'Logam'),
(3, 'Kertas'),
(4, 'Karet');

-- --------------------------------------------------------

--
-- Table structure for table `sampah`
--

CREATE TABLE `sampah` (
  `id_sampah` int(11) NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `kategori` varchar(25) DEFAULT NULL,
  `berat` varchar(15) DEFAULT NULL,
  `harga` varchar(15) DEFAULT NULL,
  `tgl_penjemputan` date DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `catatan` text DEFAULT NULL,
  `status_bayar` varchar(15) DEFAULT NULL,
  `bukti_bayar` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `sampah`
--

INSERT INTO `sampah` (`id_sampah`, `nama`, `kategori`, `berat`, `harga`, `tgl_penjemputan`, `alamat`, `catatan`, `status_bayar`, `bukti_bayar`) VALUES
(4, 'Minnn', 'Kertas', '5', '7500', '2023-06-16', 'Desa Pare Kec Pare Kediri', '', 'Sudah bayar', 'IMG_20230615012711.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `foto_sampah`
--
ALTER TABLE `foto_sampah`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indexes for table `sampah`
--
ALTER TABLE `sampah`
  ADD PRIMARY KEY (`id_sampah`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `foto_sampah`
--
ALTER TABLE `foto_sampah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sampah`
--
ALTER TABLE `sampah`
  MODIFY `id_sampah` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
