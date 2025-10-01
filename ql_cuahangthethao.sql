-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1:3307
-- Thời gian đã tạo: Th7 26, 2025 lúc 03:43 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `ql_cuahangthethao`
--

DELIMITER $$
--
-- Thủ tục
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `TaoHoaDonTuGioHang` (IN `p_MaNguoiDung` INT)   BEGIN
    DECLARE v_TongTien DECIMAL(12,2) DEFAULT 0;
    DECLARE v_MaHoaDon INT;

    -- Tính tổng tiền
    SELECT SUM(SP.Gia * CGH.SoLuong)
    INTO v_TongTien
    FROM ChiTietGioHang CGH
    JOIN ChiTietSanPham CTSP ON CGH.MaChiTiet = CTSP.MaChiTiet
    JOIN SanPham SP ON CTSP.MaSanPham = SP.MaSanPham
    JOIN GioHang GH ON GH.MaGioHang = CGH.MaGioHang
    WHERE GH.MaNguoiDung = p_MaNguoiDung;

    -- Tạo hóa đơn
    INSERT INTO HoaDon (MaNguoiDung, TongTien, TrangThai)
    VALUES (p_MaNguoiDung, v_TongTien, 'DaThanhToan');

    SET v_MaHoaDon = LAST_INSERT_ID();

    -- Thêm chi tiết hóa đơn từ giỏ hàng
    INSERT INTO ChiTietHoaDon (MaHoaDon, MaChiTiet, SoLuong, DonGia)
    SELECT v_MaHoaDon, CGH.MaChiTiet, CGH.SoLuong, SP.Gia
    FROM ChiTietGioHang CGH
    JOIN ChiTietSanPham CTSP ON CGH.MaChiTiet = CTSP.MaChiTiet
    JOIN SanPham SP ON CTSP.MaSanPham = SP.MaSanPham
    JOIN GioHang GH ON GH.MaGioHang = CGH.MaGioHang
    WHERE GH.MaNguoiDung = p_MaNguoiDung;

    -- Xoá giỏ hàng
    DELETE FROM ChiTietGioHang 
    WHERE MaGioHang IN (
        SELECT MaGioHang FROM GioHang WHERE MaNguoiDung = p_MaNguoiDung
    );
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietgiohang`
--

CREATE TABLE `chitietgiohang` (
  `MaChiTietGH` int(11) NOT NULL,
  `MaGioHang` int(11) DEFAULT NULL,
  `MaChiTiet` int(11) DEFAULT NULL,
  `SoLuong` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietgiohang`
--

INSERT INTO `chitietgiohang` (`MaChiTietGH`, `MaGioHang`, `MaChiTiet`, `SoLuong`) VALUES
(52, 16, 529, 1),
(56, 16, 527, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `MaChiTietHD` int(11) NOT NULL,
  `MaHoaDon` int(11) DEFAULT NULL,
  `MaChiTiet` int(11) DEFAULT NULL,
  `SoLuong` int(11) DEFAULT NULL,
  `DonGia` decimal(12,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`MaChiTietHD`, `MaHoaDon`, `MaChiTiet`, `SoLuong`, `DonGia`) VALUES
(4, 3, 508, 1, 690000.00),
(5, 3, 511, 1, 690000.00),
(6, 3, 508, 2, 690000.00),
(7, 4, 580, 1, 4500000.00),
(8, 4, 590, 1, 7000000.00),
(9, 4, 605, 1, 10000000.00),
(10, 4, 624, 1, 600000.00),
(11, 5, 508, 1, 720000.00),
(12, 5, 515, 1, 810000.00),
(13, 6, 508, 1, 720000.00),
(14, 7, 509, 1, 720000.00),
(15, 8, 508, 1, 621000.00),
(16, 8, 606, 1, 8500000.00),
(17, 9, 510, 2, 720000.00),
(18, 10, 604, 1, 8500000.00),
(19, 10, 508, 1, 621000.00),
(20, 11, 509, 1, 720000.00),
(21, 12, 526, 1, 621000.00),
(22, 12, 532, 1, 621000.00),
(23, 13, 509, 1, 621000.00),
(24, 14, 647, 1, 250000.00),
(25, 14, 624, 1, 200000.00),
(26, 15, 508, 1, 720000.00),
(27, 15, 611, 1, 935000.00),
(28, 16, 508, 1, 621000.00),
(29, 16, 508, 1, 621000.00),
(30, 16, 539, 1, 621000.00),
(31, 17, 532, 1, 765000.00),
(32, 17, 602, 1, 1870000.00),
(33, 17, 611, 1, 935000.00);

--
-- Bẫy `chitiethoadon`
--
DELIMITER $$
CREATE TRIGGER `trg_TruTonKho_AfterInsertChiTietHD` AFTER INSERT ON `chitiethoadon` FOR EACH ROW BEGIN
    UPDATE ChiTietSanPham
    SET SoLuongTon = SoLuongTon - NEW.SoLuong
    WHERE MaChiTiet = NEW.MaChiTiet;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietnhap`
--

CREATE TABLE `chitietnhap` (
  `MaChiTietPN` int(11) NOT NULL,
  `MaPhieuNhap` int(11) DEFAULT NULL,
  `MaChiTiet` int(11) DEFAULT NULL,
  `SoLuongNhap` int(11) DEFAULT NULL,
  `DonGiaNhap` decimal(12,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietsanpham`
--

CREATE TABLE `chitietsanpham` (
  `MaChiTiet` int(11) NOT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  `KichCo` varchar(20) DEFAULT NULL,
  `MauSac` varchar(50) DEFAULT NULL,
  `SoLuongTon` int(11) DEFAULT 0,
  `HinhAnhChiTIet` text NOT NULL,
  `GiaThanh` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietsanpham`
--

INSERT INTO `chitietsanpham` (`MaChiTiet`, `MaSanPham`, `KichCo`, `MauSac`, `SoLuongTon`, `HinhAnhChiTIet`, `GiaThanh`) VALUES
(508, 100, '38', 'đen xanh', 43, 'GIÀY ACURA VHD.jpg', 800000),
(509, 100, '39', 'đen xanh', 42, 'GIÀY ACURA VHD.jpg', 800000),
(510, 100, '40', 'đen xanh', 49, 'GIÀY ACURA VHD.jpg', 800000),
(511, 100, '41', 'đen xanh', 49, 'GIÀY ACURA VHD.jpg', 800000),
(515, 100, '38', 'đỏ vàng ', 98, 'GIÀY CHUKCHI VHD.jpg', 900000),
(516, 100, '39', 'đỏ vàng', 50, 'GIÀY CHUKCHI VHD.jpg', 900000),
(517, 100, '40', 'đỏ vàng', 50, 'GIÀY CHUKCHI VHD.jpg', 900000),
(518, 100, '41', 'đỏ vàng', 50, 'GIÀY CHUKCHI VHD.jpg', 900000),
(519, 100, '42', 'đỏ vàng ', 60, 'GIÀY CHUKCHI VHD.jpg', 900000),
(520, 100, '43', 'đỏ vàng ', 50, 'GIÀY CHUKCHI VHD.jpg', 900000),
(521, 100, '44', 'đỏ vàng', 50, 'GIÀY CHUKCHI VHD.jpg', 900000),
(522, 100, '38', 'ĐỎ ĐEN ', 50, 'GIÀY INFINITI VHD.jpg', 850000),
(523, 100, '39', 'đỏ đen ', 50, 'GIÀY INFINITI VHD.jpg', 860000),
(524, 100, '40', 'đỏ đen ', 50, 'GIÀY INFINITI VHD.jpg', 850000),
(525, 100, '44', 'đo đen', 70, 'GIÀY INFINITI VHD.jpg', 850000),
(526, 100, '41', 'đỏ đen ', 49, 'GIÀY INFINITI VHD.jpg', 850000),
(527, 100, '42', 'đỏ đen ', 50, 'GIÀY INFINITI VHD.jpg', 850000),
(528, 100, '43', 'đỏ đen ', 50, 'GIÀY INFINITI VHD.jpg', 850000),
(529, 100, '40', 'đen đỏ', 50, 'GIÀY VHD SÓI ĐEN ĐỎ.jpg', 850000),
(530, 100, '41', 'đỏ đen', 50, 'GIÀY VHD SÓI ĐEN ĐỎ.jpg', 860000),
(531, 100, '42', 'đỏ đen', 50, 'GIÀY VHD SÓI ĐEN ĐỎ.jpg', 850000),
(532, 100, '43', 'đỏ đen ', 48, 'GIÀY VHD SÓI ĐEN ĐỎ.jpg', 850000),
(534, 100, '44', 'đỏ đen', 70, 'GIÀY VHD SÓI ĐEN ĐỎ.jpg', 880000),
(535, 100, '40', 'trắng xanh', 100, 'GIAY VISION VHD.jpg', 500000),
(536, 100, '41', 'trắng xanh ', 40, 'GIAY VISION VHD.jpg', 500000),
(537, 100, '42', 'trắng xanh', 50, 'GIAY VISION VHD.jpg', 500000),
(538, 100, '42', 'trắng xanh', 50, 'GIAY VISION VHD.jpg', 500000),
(539, 100, '43', 'trắng xanh', 49, 'GIAY VISION VHD.jpg', 500000),
(540, 100, '44', 'trắng xanh', 50, 'GIAY VISION VHD.jpg', 500000),
(541, 105, '40', 'đen vàng', 100, 'GIÀY SAO VÀNG 301 BLACK.jpg', 700000),
(542, 105, '41', 'đen vàng', 50, 'GIÀY SAO VÀNG 301 BLACK.jpg', 700000),
(543, 105, '42', 'đen vàng', 50, 'GIÀY SAO VÀNG 301 BLACK.jpg', 700000),
(544, 105, '43', 'đen vàng', 50, 'GIÀY SAO VÀNG 301 BLACK.jpg', 700000),
(545, 105, '44', 'đen vàng', 50, 'GIÀY SAO VÀNG 301 BLACK.jpg', 700000),
(546, 105, '40', 'đen đỏ', 100, 'GIÀY SAO VÀNG 301 ĐEN ĐỎ.jpg', 750000),
(547, 105, '41', 'đen đỏ', 100, 'GIÀY SAO VÀNG 301 ĐEN ĐỎ.jpg', 750000),
(548, 105, '42', 'đen đỏ', 100, 'GIÀY SAO VÀNG 301 ĐEN ĐỎ.jpg', 750000),
(549, 100, '43', 'đen đỏ', 100, 'GIÀY SAO VÀNG 301 ĐEN ĐỎ.jpg', 750000),
(550, 105, '44', 'đen đỏ', 100, 'GIÀY SAO VÀNG 301 ĐEN ĐỎ.jpg', 750000),
(551, 105, '44', 'xanh non ', 100, 'GIÀY SAO VÀNG 401 NEON.jpg', 560000),
(552, 105, '40', 'xanh non ', 100, 'GIÀY SAO VÀNG 401 NEON.jpg', 560000),
(553, 105, '41', 'xanh non', 100, 'GIÀY SAO VÀNG 401 NEON.jpg', 560000),
(554, 100, '42', 'xanh non ', 100, 'GIÀY SAO VÀNG 401 NEON.jpg', 560000),
(555, 105, '43', 'xanh non ', 100, 'GIÀY SAO VÀNG 401 NEON.jpg', 560000),
(556, 105, '40', 'xanh navy', 50, 'GIÀY SAO VÀNG WARRIOR ĐEN TRẮNG.jpg', 650000),
(557, 105, '41', 'xanh navy', 50, 'GIÀY SAO VÀNG WARRIOR ĐEN TRẮNG.jpg', 650000),
(558, 105, '42', 'xanh navy', 50, 'GIÀY SAO VÀNG WARRIOR ĐEN TRẮNG.jpg', 650000),
(559, 105, '43', 'xanh navy', 1, 'GIÀY SAO VÀNG WARRIOR ĐEN TRẮNG.jpg', 650000),
(560, 105, '44', 'xanh navy', 100, 'GIÀY SAO VÀNG WARRIOR ĐEN TRẮNG.jpg', 650000),
(561, 105, '40', 'trắng tuyết', 100, 'GIÀY SAO VÀNG WARRIOR TRẮNG.jpg', 800000),
(562, 105, '41', 'trắng tuyết', 100, 'GIÀY SAO VÀNG WARRIOR TRẮNG.jpg', 800000),
(563, 105, '42', 'trắng tuyết', 100, 'GIÀY SAO VÀNG WARRIOR TRẮNG.jpg', 800000),
(564, 105, '43', 'trắng tuyết', 100, 'GIÀY SAO VÀNG WARRIOR TRẮNG.jpg', 800000),
(565, 105, '44', 'trắng tuyết', 100, 'GIÀY SAO VÀNG WARRIOR TRẮNG.jpg', 800000),
(566, 104, '40', 'đen trắng ', 100, 'GIÀY MIZUNO ĐEN TRẮNG.jpg', 1200000),
(567, 104, '41', 'đen trắng ', 100, 'GIÀY MIZUNO ĐEN TRẮNG.jpg', 1200000),
(568, 104, '42', 'đen trắng ', 100, 'GIÀY MIZUNO ĐEN TRẮNG.jpg', 1200000),
(569, 104, '43', 'đen rắng ', 100, 'GIÀY MIZUNO ĐEN TRẮNG.jpg', 1200000),
(570, 104, '44', 'đen rắng ', 100, 'GIÀY MIZUNO ĐEN TRẮNG.jpg', 1200000),
(571, 104, '40', 'đen vàng ', 100, 'GIÀY MIZUNO ĐEN VÀNG.jpg', 1500000),
(572, 104, '41', 'đen vàng ', 100, 'GIÀY MIZUNO ĐEN VÀNG.jpg', 1500000),
(573, 104, '42', 'đen vàng ', 100, 'GIÀY MIZUNO ĐEN VÀNG.jpg', 1500000),
(574, 105, '43', 'đen vàng ', 100, 'GIÀY MIZUNO ĐEN VÀNG.jpg', 1500000),
(575, 104, '44', 'đen vàng ', 100, 'GIÀY MIZUNO ĐEN VÀNG.jpg', 2000000),
(576, 104, '40', 'đỏ vàng ', 100, 'GIÀY MIZUNO ĐỎ VÀNG.jpg', 2000000),
(577, 104, '44', 'đỏ vàng ', 100, 'GIÀY MIZUNO ĐỎ VÀNG.jpg', 2000000),
(578, 104, '40', 'trắng xanh ', 100, 'GIÀY MIZUNO TRẮNG XANH.jpg', 2500000),
(579, 104, '44', 'trắng xanh ', 100, 'GIÀY MIZUNO TRẮNG XANH.jpg', 2500000),
(580, 104, '40', 'xanh trắng ', 99, 'GIÀY MIZUNO XANH TRẮNG.jpg', 1800000),
(581, 104, '44', 'xanh trắng ', 100, 'GIÀY MIZUNO XANH TRẮNG.jpg', 3000000),
(582, 103, '40', 'xám đen ', 100, 'GIÀY BEYONO ÁM ĐEN.jpg', 750000),
(583, 103, '44', 'xám đen ', 100, 'GIÀY BEYONO ÁM ĐEN.jpg', 750000),
(584, 103, '40', 'đỏ trắng ', 101, 'GIÀY BEYONO ĐỎ TRẮNG.jpg', 770000),
(585, 103, '44', 'đỏ trắng ', 100, 'GIÀY BEYONO ĐỎ TRẮNG.jpg', 770000),
(586, 103, '40', 'lam lục ', 50, 'GIÀY BEYONO LAM LỤC.jpg', 780000),
(587, 103, '44', 'lam lục ', 50, 'GIÀY BEYONO LAM LỤC.jpg', 780000),
(588, 103, '40', 'navy trắng ', 50, 'GIÀY BEYONO NAVY TRẮNG.jpg', 780000),
(589, 103, '44', 'navy trắng ', 100, 'GIÀY BEYONO NAVY TRẮNG.jpg', 3000000),
(590, 103, '40', 'trắng xanh ', 39, 'GIÀY BEYONO TRẮNG ANH.jpg', 780000),
(591, 103, '44', 'trắng xanh ', 50, 'GIÀY BEYONO TRẮNG ANH.jpg', 780000),
(592, 102, '40', 'trắng xanh non', 50, 'GIÀY  ASICS BẢN MỚI.jpg', 4000000),
(593, 102, '44', 'trắng xanh non ', 100, 'GIÀY  ASICS BẢN MỚI.jpg', 2000000),
(594, 102, '40', 'xanh navy', 52, 'GIÀY  ASICS GEL TASK MT 4.jpg', 2850000),
(595, 102, '44', 'xanh vany', 51, 'GIÀY  ASICS GEL TASK MT 4.jpg', 2000000),
(596, 102, '40', 'trắng xanh', 50, 'GIÀY  ASICS NOVA.jpg', 1890000),
(597, 102, '44', 'trắng xanh', 50, 'GIÀY  ASICS NOVA.jpg', 2000000),
(598, 102, '40', 'trắng lam ', 100, 'GIÀY  ASICS SKY FF3.jpg', 2880000),
(599, 102, '44', 'trắng lam ', 100, 'GIÀY  ASICS SKY FF3.jpg', 2880000),
(600, 102, '40', 'trắng xanh navy', 100, 'GIÀY ASIC BẢN CHƠI PICK.jpg', 1500000),
(601, 102, '44', 'trắng xanh navy', 100, 'GIÀY ASIC BẢN CHƠI PICK.jpg', 2000000),
(602, 101, '40', '7 sắc cầu vồng', 59, 'GIÀY ADIDAS CRAZYFLIGHT 7 SẮC CẦU VỒNG.jpg', 2200000),
(603, 101, '44', '7 sắc càu vồng ', 101, 'GIÀY ADIDAS CRAZYFLIGHT 7 SẮC CẦU VỒNG.jpg', 2200000),
(604, 101, '40', 'xanh dương ', 50, 'GIÀY ADIDAS CRAZYFLIGHT ANH TRẮNG.jpg', 2500000),
(605, 101, '44', 'xanh dương ', 39, 'GIÀY ADIDAS CRAZYFLIGHT ANH TRẮNG.jpg', 3500000),
(606, 101, '40', 'cam vàng ', 49, 'GIÀY ADIDAS CRAZYFLIGHT CAM VÀNG.jpg', 1550000),
(607, 101, '44', 'cam vàng ', 30, 'GIÀY ADIDAS CRAZYFLIGHT CAM VÀNG.jpg', 1550000),
(608, 101, '40', 'đen mid trắng ', 100, 'GIÀY ADIDAS CRAZYFLIGHT ĐEN TRẮNG.jpg', 1850000),
(609, 101, '44', 'đen mid trắng ', 90, 'GIÀY ADIDAS CRAZYFLIGHT ĐEN TRẮNG.jpg', 1850000),
(610, 101, '40', 'đen đế hồng ', 50, 'GIÀY ADIDAS ĐEN HỒNG.jpg', 1100000),
(611, 101, '44', 'đen đế hồng ', 98, 'GIÀY ADIDAS ĐEN HỒNG.jpg', 1100000),
(612, 106, 'M', 'đen ', 30, 'bó gối asic.jpg', 500000),
(613, 106, 'L', 'đen ', 30, 'bó gối asic.jpg', 500000),
(614, 106, 'M', 'đen ', 20, 'bó gối LP tem đỏ.jpg', 250000),
(615, 106, 'L', 'đen ', 20, 'bó gối LP tem đỏ.jpg', 0),
(616, 106, 'M', 'đen ', 50, 'bó gối LP tem xanh.jpg', 600000),
(617, 106, 'L', 'đen', 100, 'bó gối LP tem xanh.jpg', 600000),
(618, 106, 'M', 'đen ', 50, 'bố gói mizuno.jpg', 450000),
(619, 106, 'L', 'đen ', 40, 'bố gói mizuno.jpg', 0),
(620, 112, '1 size', 'vàng trắng ', 50, 'bóng dragon.jpg', 800000),
(621, 112, '1 size', 'vàng trắng ', 50, 'bóng hunter.jpg', 600000),
(622, 112, '1 size ', 'vàng trắng ', 100, 'bóng mikasa.jpg', 1200000),
(623, 112, '1 size', 'trắng đỏ ', 100, 'bóng molten.jpg', 450000),
(624, 112, '1 size', 'vàng trắng ', 148, 'bóng thăng long.jpg', 2000000),
(625, 107, 'M', 'đen loang ', 100, 'mẫu đen loang.jpg', 300000),
(626, 107, 'L', 'xanh navy', 100, 'mẫu navy viền xanh non.jpg', 500000),
(627, 107, 'XL', 'trắng ', 40, 'mẫu trắng có cổ.jpg', 1000000),
(628, 107, 'S', 'trắng cổ cam ', 30, 'mẫu trắng cổ cam.jpg', 250000),
(629, 107, 'XXL', 'trắng loang', 40, 'mẫu trắng loang.jpg', 2000000),
(630, 108, 'S', 'đỏ bích ', 60, 'mẫu đỏ bích.jpg', 225000),
(631, 108, 'M', 'đỏ tươi', 100, 'mẫu đỏ.jpg', 225000),
(632, 107, 'L', 'trắng tinh', 100, 'mẫu trắng.jpg', 225000),
(633, 108, 'XL', 'xanh lam ', 100, 'mẫu xanh lam.jpg', 225000),
(634, 108, 'XXL', 'đen ', 120, 'nẫu đen.jpg', 2000000),
(635, 109, 'S', 'đen đỏ', 100, 'mẫu đen đỏ.jpg', 170000),
(636, 109, 'M', 'đỏ vàng ', 100, 'mẫu đỏ vàng.jpg', 170000),
(637, 109, 'L', 'trắng xanh ', 70, 'mẫu trắng xanh.jpg', 170000),
(638, 109, 'XL', 'vàng cam ', 100, 'mẫu vàng cam.jpg', 170000),
(640, 110, 'S', 'đỏ đô', 70, 'mẫu đỏ đô.jpg', 210000),
(641, 110, 'M', 'hồng nhạt ', 100, 'mẫu hồng nhạt.jpg', 210000),
(642, 110, 'L', 'trắng đen', 100, 'mẫu trắng đen.jpg', 210000),
(643, 110, 'XL', 'xanh dương ', 100, 'mẫu trắng xanh dương.jpg', 210000),
(644, 110, 'XXL', 'xanh ngọc ', 100, 'mẫu xanh ngọc.jpg', 210000),
(645, 111, 'S', 'sói đen ', 100, 'mẫu sói đen.jpg', 250000),
(646, 111, 'M', ' sói đỏ', 100, 'mẫu sói đỏ.jpg', 250000),
(647, 111, 'L', 'sói trắng ', 99, 'mẫu sói trắng.jpg', 250000),
(648, 111, 'XL', 'sói vàng ', 100, 'mẫu sói vàng.jpg', 250000),
(649, 111, 'XXL', 'sói xanh ', 100, 'mẫu sói xanh.jpg', 250000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhgia`
--

CREATE TABLE `danhgia` (
  `MaDanhGia` int(11) NOT NULL,
  `MaNguoiDung` int(11) DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL,
  `SoSao` int(11) DEFAULT NULL CHECK (`SoSao` between 1 and 5),
  `BinhLuan` text DEFAULT NULL,
  `NgayDanhGia` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `danhgia`
--

INSERT INTO `danhgia` (`MaDanhGia`, `MaNguoiDung`, `MaSanPham`, `SoSao`, `BinhLuan`, `NgayDanhGia`) VALUES
(2, 5, 100, 5, 'vip :)))', '2025-07-16 09:59:17');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhmuc`
--

CREATE TABLE `danhmuc` (
  `MaDanhMuc` int(11) NOT NULL,
  `TenDanhMuc` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `danhmuc`
--

INSERT INTO `danhmuc` (`MaDanhMuc`, `TenDanhMuc`) VALUES
(201, 'Quần áo'),
(202, 'Phụ kiện'),
(203, 'Giày'),
(204, 'Quả bóng');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `giohang`
--

CREATE TABLE `giohang` (
  `MaGioHang` int(11) NOT NULL,
  `MaNguoiDung` int(11) DEFAULT NULL,
  `NgayTao` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `giohang`
--

INSERT INTO `giohang` (`MaGioHang`, `MaNguoiDung`, `NgayTao`) VALUES
(16, 2, '2025-07-25 22:31:22'),
(18, 5, '2025-07-25 23:28:17');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `MaHoaDon` int(11) NOT NULL,
  `MaNguoiDung` int(11) DEFAULT NULL,
  `NgayLap` datetime DEFAULT current_timestamp(),
  `TongTien` decimal(12,2) DEFAULT NULL,
  `TrangThai` enum('ChuaThanhToan','DaThanhToan','DaGiao','Huy','DangGiao') DEFAULT 'ChuaThanhToan',
  `MaKM` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`MaHoaDon`, `MaNguoiDung`, `NgayLap`, `TongTien`, `TrangThai`, `MaKM`) VALUES
(1, 2, '2025-06-02 13:07:35', 1900000.00, 'DaThanhToan', NULL),
(2, 2, '2025-05-02 13:13:47', 1900000.00, 'DaThanhToan', NULL),
(3, 1, '2025-07-16 09:54:34', 2760000.00, 'DaThanhToan', NULL),
(4, 1, '2025-04-16 09:55:54', 22115000.00, 'DaThanhToan', NULL),
(5, 5, '2025-07-16 14:09:00', 1530000.00, 'DaGiao', NULL),
(6, 5, '2025-07-16 14:35:55', 720000.00, 'DangGiao', NULL),
(7, 5, '2025-07-16 14:37:17', 720000.00, 'Huy', NULL),
(8, 1, '2025-07-16 14:56:13', 9121000.00, 'ChuaThanhToan', 1),
(9, 5, '2025-07-17 09:35:22', 1440000.00, 'DangGiao', NULL),
(10, 1, '2025-07-17 09:50:30', 9121000.00, 'ChuaThanhToan', 1),
(11, 5, '2025-02-18 11:57:25', 720000.00, 'DaThanhToan', NULL),
(12, 1, '2025-07-18 12:50:47', 1242000.00, 'DaThanhToan', 1),
(13, 1, '2025-07-18 12:54:31', 621000.00, 'ChuaThanhToan', 1),
(14, 5, '2025-07-18 13:04:04', 250000.00, 'DangGiao', NULL),
(15, 5, '2025-01-20 18:50:29', 1655000.00, 'DangGiao', NULL),
(16, 1, '2025-07-25 22:27:15', 1863000.00, 'ChuaThanhToan', 1),
(17, 5, '2025-07-25 22:59:05', 3570000.00, 'DangGiao', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khuyenmai`
--

CREATE TABLE `khuyenmai` (
  `MaKM` int(11) NOT NULL,
  `TenKM` varchar(100) DEFAULT NULL,
  `MoTa` text DEFAULT NULL,
  `PhanTramGiam` decimal(5,2) DEFAULT NULL,
  `NgayBatDau` datetime DEFAULT NULL,
  `NgayKetThuc` datetime DEFAULT NULL,
  `MaSanPham` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khuyenmai`
--

INSERT INTO `khuyenmai` (`MaKM`, `TenKM`, `MoTa`, `PhanTramGiam`, `NgayBatDau`, `NgayKetThuc`, `MaSanPham`) VALUES
(1, 'Sale mùa hè', 'Giảm 10% cho giày chạy', 10.00, '2025-07-01 00:00:00', '2025-07-31 00:00:00', 100),
(2, 'Sale mùa hè', 'Giảm 10% cho giày chạy', 15.00, '2025-07-01 00:00:00', '2025-07-31 00:00:00', 101);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguoidung`
--

CREATE TABLE `nguoidung` (
  `MaNguoiDung` int(11) NOT NULL,
  `TenDangNhap` varchar(50) NOT NULL,
  `MatKhau` varchar(255) NOT NULL,
  `HoTen` varchar(100) DEFAULT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `VaiTro` enum('Admin','User') NOT NULL DEFAULT 'User',
  `AnhDaiDien` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nguoidung`
--

INSERT INTO `nguoidung` (`MaNguoiDung`, `TenDangNhap`, `MatKhau`, `HoTen`, `SoDienThoai`, `DiaChi`, `VaiTro`, `AnhDaiDien`) VALUES
(1, 'admin', '123', 'Nguyễn Đạt', '09765323', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(2, 'khach1', 'user123', 'Trần Khách', '0987654321', 'TP.HCM', 'User', ''),
(3, 'user02', '123', 'Nguyen van b', '0123456789', 'Hà Nội', 'User', ''),
(4, 'admin2', '123', 'Nguyễn Đức Anh', '1234567891', 'Hà Nội', 'Admin', ''),
(5, 'user007', '123', 'Nguyễn Văn K', '123331321', 'Hà Nội', 'User', NULL),
(6, 'NguyenVanHao', '123456', 'Nguyễn Văn Hào', '0975845634', 'Ninh bình', 'Admin', ''),
(7, 'PhamTheNam', '123456', 'Phạm Thế Nam', '0984762883', 'Quảng Ninh', 'Admin', ''),
(8, 'NguyenThiHoa', '123456', 'Nguyễn Thị Hoa', '09574356542', 'Hà Nội', 'User', ''),
(9, 'NguyenKhanhLinh', '123456', 'Nguyễn Khánh Linh', '09574356542', 'Hà Nội', 'User', ''),
(10, 'NguyenAnhDung', '123456', 'Nguyễn Anh Dũng', '0868036455', 'Hà Nội', 'Admin', '0097578bd5f55cab05e4.jpg'),
(11, 'admin01', '123', 'Nguyễn Admin 01', '0900000001', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(12, 'admin02', '123', 'Nguyễn Admin 02', '0900000002', 'TPHCM', 'Admin', 'AnhDaiDien.png'),
(13, 'admin03', '123', 'Nguyễn Admin 03', '0900000003', 'Đà Nẵng', 'Admin', 'AnhDaiDien.png'),
(14, 'admin04', '123', 'Nguyễn Admin 04', '0900000004', 'Hải Phòng', 'Admin', 'AnhDaiDien.png'),
(15, 'admin05', '123', 'Nguyễn Admin 05', '0900000005', 'Cần Thơ', 'Admin', 'AnhDaiDien.png'),
(16, 'admin06', '123', 'Nguyễn Admin 06', '0900000006', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(17, 'admin07', '123', 'Nguyễn Admin 07', '0900000007', 'TPHCM', 'Admin', 'AnhDaiDien.png'),
(18, 'admin08', '123', 'Nguyễn Admin 08', '0900000008', 'Hải Dương', 'Admin', 'AnhDaiDien.png'),
(19, 'admin09', '123', 'Nguyễn Admin 09', '0900000009', 'Bắc Ninh', 'Admin', 'AnhDaiDien.png'),
(20, 'admin10', '123', 'Nguyễn Admin 10', '0900000010', 'Quảng Ninh', 'Admin', 'AnhDaiDien.png'),
(21, 'admin11', '123', 'Nguyễn Admin 11', '0900000011', 'Nam Định', 'Admin', 'AnhDaiDien.png'),
(22, 'admin12', '123', 'Nguyễn Admin 12', '0900000012', 'Hòa Bình', 'Admin', 'AnhDaiDien.png'),
(23, 'admin13', '123', 'Nguyễn Admin 13', '0900000013', 'Hưng Yên', 'Admin', 'AnhDaiDien.png'),
(24, 'admin14', '123', 'Nguyễn Admin 14', '0900000014', 'Bắc Giang', 'Admin', 'AnhDaiDien.png'),
(25, 'admin15', '123', 'Nguyễn Admin 15', '0900000015', 'Hà Nam', 'Admin', 'AnhDaiDien.png'),
(26, 'admin16', '123', 'Nguyễn Admin 16', '0900000016', 'Hải Phòng', 'Admin', 'AnhDaiDien.png'),
(27, 'admin17', '123', 'Nguyễn Admin 17', '0900000017', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(28, 'admin18', '123', 'Nguyễn Admin 18', '0900000018', 'TPHCM', 'Admin', 'AnhDaiDien.png'),
(29, 'admin19', '123', 'Nguyễn Admin 19', '0900000019', 'Đà Nẵng', 'Admin', 'AnhDaiDien.png'),
(30, 'admin20', '123', 'Nguyễn Admin 20', '0900000020', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(107, 'adminlinh', '123', 'Lê Thị Linh', '0901000001', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(108, 'adminhuy', '123', 'Nguyễn Văn Huy', '0901000002', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(109, 'adminthao', '123', 'Trần Thị Thảo', '0901000003', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(110, 'admintung', '123', 'Bùi Văn Tùng', '0901000004', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(111, 'adminhoang', '123', 'Phạm Hoàng Nam', '0901000005', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(112, 'adminha', '123', 'Đỗ Thu Hà', '0901000006', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(113, 'adminkhoa', '123', 'Nguyễn Văn Khoa', '0901000007', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(114, 'admintam', '123', 'Vũ Đức Tâm', '0901000008', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(115, 'adminngan', '123', 'Lê Khánh Ngân', '0901000009', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(116, 'admintrung', '123', 'Hoàng Trung Hiếu', '0901000010', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(117, 'adminanh', '123', 'Nguyễn Thị Ánh', '0901000011', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(118, 'admincuong', '123', 'Lê Văn Cường', '0901000012', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(119, 'adminloan', '123', 'Trịnh Thị Loan', '0901000013', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(120, 'admintrang', '123', 'Đặng Minh Trang', '0901000014', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(121, 'adminminh', '123', 'Lý Trung Minh', '0901000015', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(122, 'adminthu', '123', 'Nguyễn Thị Thu', '0901000016', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(123, 'adminkhanh', '123', 'Phan Hữu Khánh', '0901000017', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(124, 'adminvy', '123', 'Đoàn Ngọc Vy', '0901000018', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(125, 'adminngoc', '123', 'Ngô Nhật Ngọc', '0901000019', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(126, 'admindat', '123', 'Nguyễn Quốc Đạt', '0901000020', 'Hà Nội', 'Admin', 'AnhDaiDien.png'),
(127, 'userlong', '123', 'Nguyễn Trọng Long', '0911000001', 'TPHCM', 'User', 'AnhDaiDien.png'),
(128, 'usermai', '123', 'Phạm Thị Mai', '0911000002', 'Hà Nội', 'User', 'AnhDaiDien.png'),
(129, 'userkieu', '123', 'Đỗ Thị Kiều', '0911000003', 'Hà Nội', 'User', 'AnhDaiDien.png'),
(130, 'usernam', '123', 'Trần Hữu Nam', '0911000004', 'Đà Nẵng', 'User', 'AnhDaiDien.png'),
(131, 'uservy', '123', 'Nguyễn Mỹ Vy', '0911000005', 'Hải Phòng', 'User', 'AnhDaiDien.png'),
(132, 'userduy', '123', 'Lê Văn Duy', '0911000006', 'TPHCM', 'User', 'AnhDaiDien.png'),
(133, 'userthu', '123', 'Hoàng Thị Thu', '0911000007', 'Hưng Yên', 'User', 'AnhDaiDien.png'),
(134, 'userlan', '123', 'Vũ Ngọc Lan', '0911000008', 'Quảng Ninh', 'User', 'AnhDaiDien.png'),
(135, 'userquang', '123', 'Phạm Quang Minh', '0911000009', 'Nam Định', 'User', 'AnhDaiDien.png'),
(136, 'userhuong', '123', 'Đinh Thu Hương', '0911000010', 'Bắc Giang', 'User', 'AnhDaiDien.png'),
(137, 'userhieu', '123', 'Bùi Văn Hiếu', '0911000011', 'Thanh Hóa', 'User', 'AnhDaiDien.png'),
(138, 'userbao', '123', 'Nguyễn Minh Bảo', '0911000012', 'Hà Nam', 'User', 'AnhDaiDien.png'),
(139, 'usermy', '123', 'Trần Thị Mỹ', '0911000013', 'Thái Bình', 'User', 'AnhDaiDien.png'),
(140, 'usertu', '123', 'Vũ Nhật Tú', '0911000014', 'Hải Dương', 'User', 'AnhDaiDien.png'),
(141, 'usertuong', '123', 'Lý Thị Tường', '0911000015', 'Nghệ An', 'User', 'AnhDaiDien.png'),
(142, 'useran', '123', 'Nguyễn Thanh An', '0911000016', 'Ninh Bình', 'User', 'AnhDaiDien.png'),
(143, 'userngan', '123', 'Phan Thảo Ngân', '0911000017', 'TPHCM', 'User', 'AnhDaiDien.png'),
(144, 'userkien', '123', 'Lương Văn Kiên', '0911000018', 'Hà Nội', 'User', 'AnhDaiDien.png'),
(145, 'userhoa', '123', 'Đào Thị Hoa', '0911000019', 'Hải Phòng', 'User', 'AnhDaiDien.png'),
(146, 'userphong', '123', 'Lê Quốc Phong', '0911000020', 'Đà Nẵng', 'User', 'AnhDaiDien.png');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `MaNCC` int(11) NOT NULL,
  `TenNCC` varchar(255) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `SoDienThoai` varchar(20) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhacungcap`
--

INSERT INTO `nhacungcap` (`MaNCC`, `TenNCC`, `DiaChi`, `SoDienThoai`, `Email`) VALUES
(1, 'Công ty Nike Việt Nam', 'Hà Nội', '0324567890', 'contact@nike.vn'),
(2, 'Công ty ADIDAS Việt Nam', 'Hà Nội', '086578432', 'contact@adidas.vn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhap`
--

CREATE TABLE `phieunhap` (
  `MaPhieuNhap` int(11) NOT NULL,
  `MaNCC` int(11) DEFAULT NULL,
  `NgayNhap` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phieunhap`
--

INSERT INTO `phieunhap` (`MaPhieuNhap`, `MaNCC`, `NgayNhap`) VALUES
(5, 1, '2025-07-25 10:10:34');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `MaSanPham` int(11) NOT NULL,
  `TenSanPham` varchar(255) NOT NULL,
  `MoTa` text DEFAULT NULL,
  `Gia` decimal(12,2) NOT NULL,
  `HinhAnh` varchar(255) DEFAULT NULL,
  `MaDanhMuc` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`MaSanPham`, `TenSanPham`, `MoTa`, `Gia`, `HinhAnh`, `MaDanhMuc`) VALUES
(100, 'Giày VHD', 'Giày thể thao thời trang', 690000.00, 'GIAY VISION VHD.jpg', 203),
(101, 'Giày ADIDAS', 'Giày thể thao xịn', 2000000.00, 'GIÀY ADIDAS ĐEN HỒNG.jpg', 203),
(102, 'Giày ASICS', 'Giày vip ', 5000000.00, 'GIÀY  ASICS NOVA.jpg', 203),
(103, 'Giày BEYONO', 'Giày bá đạo', 7000000.00, 'GIÀY BEYONO TRẮNG ANH.jpg', 203),
(104, 'Giày MUZINO', 'Giày xịn xò', 4500000.00, 'GIÀY MIZUNO ĐEN TRẮNG.jpg', 203),
(105, 'Giày SAO VÀNG', 'Giày Việt Nam', 1000000.00, 'GIÀY SAO VÀNG 301 ĐEN ĐỎ.jpg', 203),
(106, 'Bó gối', 'Bó gối đẹp', 100000.00, 'bó gối asic.jpg', 202),
(107, 'Quần áo asic', 'Áo quần thể thao', 300000.00, 'mẫu đen loang.jpg', 201),
(108, 'Quần áo beyono', 'Áo quần thể thao', 350000.00, 'mẫu trắng.jpg', 201),
(109, 'Quần áo hiwing', 'Áo quần thể thao', 350000.00, 'mẫu đỏ vàng.jpg', 201),
(110, 'Quần áo sao vàng', 'Áo quần thể thao', 400000.00, 'mẫu đỏ đô.jpg', 201),
(111, 'Quần áo VHD', 'Áo quần thể thao', 400000.00, 'mẫu sói đen.jpg', 201),
(112, 'quả bóng chuyền ', 'các loại bóng da do nhật bản và việt nam tạo nên ', 600000.00, 'bóng thăng long.jpg', 204);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  ADD PRIMARY KEY (`MaChiTietGH`),
  ADD KEY `MaGioHang` (`MaGioHang`),
  ADD KEY `MaChiTiet` (`MaChiTiet`);

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD PRIMARY KEY (`MaChiTietHD`),
  ADD KEY `MaHoaDon` (`MaHoaDon`),
  ADD KEY `MaChiTiet` (`MaChiTiet`);

--
-- Chỉ mục cho bảng `chitietnhap`
--
ALTER TABLE `chitietnhap`
  ADD PRIMARY KEY (`MaChiTietPN`),
  ADD KEY `MaPhieuNhap` (`MaPhieuNhap`),
  ADD KEY `MaChiTiet` (`MaChiTiet`);

--
-- Chỉ mục cho bảng `chitietsanpham`
--
ALTER TABLE `chitietsanpham`
  ADD PRIMARY KEY (`MaChiTiet`),
  ADD KEY `MaSanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  ADD PRIMARY KEY (`MaDanhGia`),
  ADD KEY `MaNguoiDung` (`MaNguoiDung`),
  ADD KEY `MaSanPham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `danhmuc`
--
ALTER TABLE `danhmuc`
  ADD PRIMARY KEY (`MaDanhMuc`);

--
-- Chỉ mục cho bảng `giohang`
--
ALTER TABLE `giohang`
  ADD PRIMARY KEY (`MaGioHang`),
  ADD KEY `MaNguoiDung` (`MaNguoiDung`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MaHoaDon`),
  ADD KEY `MaNguoiDung` (`MaNguoiDung`),
  ADD KEY `fk_hoadon_khuyenmai` (`MaKM`);

--
-- Chỉ mục cho bảng `khuyenmai`
--
ALTER TABLE `khuyenmai`
  ADD PRIMARY KEY (`MaKM`),
  ADD KEY `fk_khuyenmai_sanpham` (`MaSanPham`);

--
-- Chỉ mục cho bảng `nguoidung`
--
ALTER TABLE `nguoidung`
  ADD PRIMARY KEY (`MaNguoiDung`),
  ADD UNIQUE KEY `TenDangNhap` (`TenDangNhap`);

--
-- Chỉ mục cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  ADD PRIMARY KEY (`MaNCC`);

--
-- Chỉ mục cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD PRIMARY KEY (`MaPhieuNhap`),
  ADD KEY `MaNCC` (`MaNCC`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`MaSanPham`),
  ADD KEY `MaDanhMuc` (`MaDanhMuc`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  MODIFY `MaChiTietGH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  MODIFY `MaChiTietHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT cho bảng `chitietnhap`
--
ALTER TABLE `chitietnhap`
  MODIFY `MaChiTietPN` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `chitietsanpham`
--
ALTER TABLE `chitietsanpham`
  MODIFY `MaChiTiet` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=650;

--
-- AUTO_INCREMENT cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  MODIFY `MaDanhGia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `danhmuc`
--
ALTER TABLE `danhmuc`
  MODIFY `MaDanhMuc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=205;

--
-- AUTO_INCREMENT cho bảng `giohang`
--
ALTER TABLE `giohang`
  MODIFY `MaGioHang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `MaHoaDon` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `khuyenmai`
--
ALTER TABLE `khuyenmai`
  MODIFY `MaKM` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `nguoidung`
--
ALTER TABLE `nguoidung`
  MODIFY `MaNguoiDung` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=147;

--
-- AUTO_INCREMENT cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  MODIFY `MaNCC` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  MODIFY `MaPhieuNhap` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `MaSanPham` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  ADD CONSTRAINT `chitietgiohang_ibfk_1` FOREIGN KEY (`MaGioHang`) REFERENCES `giohang` (`MaGioHang`),
  ADD CONSTRAINT `chitietgiohang_ibfk_2` FOREIGN KEY (`MaChiTiet`) REFERENCES `chitietsanpham` (`MaChiTiet`);

--
-- Các ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`MaHoaDon`) REFERENCES `hoadon` (`MaHoaDon`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`MaChiTiet`) REFERENCES `chitietsanpham` (`MaChiTiet`);

--
-- Các ràng buộc cho bảng `chitietnhap`
--
ALTER TABLE `chitietnhap`
  ADD CONSTRAINT `chitietnhap_ibfk_1` FOREIGN KEY (`MaPhieuNhap`) REFERENCES `phieunhap` (`MaPhieuNhap`),
  ADD CONSTRAINT `chitietnhap_ibfk_2` FOREIGN KEY (`MaChiTiet`) REFERENCES `chitietsanpham` (`MaChiTiet`);

--
-- Các ràng buộc cho bảng `chitietsanpham`
--
ALTER TABLE `chitietsanpham`
  ADD CONSTRAINT `chitietsanpham_ibfk_1` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`);

--
-- Các ràng buộc cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  ADD CONSTRAINT `danhgia_ibfk_1` FOREIGN KEY (`MaNguoiDung`) REFERENCES `nguoidung` (`MaNguoiDung`),
  ADD CONSTRAINT `danhgia_ibfk_2` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`);

--
-- Các ràng buộc cho bảng `giohang`
--
ALTER TABLE `giohang`
  ADD CONSTRAINT `giohang_ibfk_1` FOREIGN KEY (`MaNguoiDung`) REFERENCES `nguoidung` (`MaNguoiDung`);

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `fk_hoadon_khuyenmai` FOREIGN KEY (`MaKM`) REFERENCES `khuyenmai` (`MaKM`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`MaNguoiDung`) REFERENCES `nguoidung` (`MaNguoiDung`);

--
-- Các ràng buộc cho bảng `khuyenmai`
--
ALTER TABLE `khuyenmai`
  ADD CONSTRAINT `fk_khuyenmai_sanpham` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD CONSTRAINT `phieunhap_ibfk_1` FOREIGN KEY (`MaNCC`) REFERENCES `nhacungcap` (`MaNCC`);

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `sanpham_ibfk_1` FOREIGN KEY (`MaDanhMuc`) REFERENCES `danhmuc` (`MaDanhMuc`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
