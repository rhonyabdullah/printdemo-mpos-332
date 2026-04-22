**English:**

This project is a demo that shows how to call the printer service. The printer service is an Android Service Component. According to the bindService API, your application can connect with the printer service. Once you have connected with the printer service, you can send the Epson command to print.

Usage Instructions:
	1.	Add the AIDL file PrinterInterface.aidl to the package recieptservice.com.recieptservice.
	2.	Use the following code to bind the printer service:
   Intent intent = new Intent();
   intent.setClassName(“recieptservice.com.recieptservice”, “recieptservice.com.recieptservice.service.PrinterService”);
   bindService(intent, new ServiceConnection(), Service.BIND_AUTO_CREATE);
	1.	After binding successfully, you can start calling the relevant API interfaces to print.

AIDL Documentation:

Epson Command Print Interface:
void printEpson(in byte[] data);

Get Printer Service Version Number:
String getServiceVersion();

Print String Text:
void printText(String text);

Print BitMap Image:
void printBitmap(in Bitmap pic);

Print Barcode:
data: Barcode content
symbology: Barcode type
    0 — UPC-A
    1 — UPC-E
    2 — JAN13(EAN13)
    3 — JAN8(EAN8)
    4 — CODE39
    5 — ITF
    6 — CODABAR
    7 — CODE93
    8 — CODE128
height: Barcode height 1-255, default 162
width: Barcode width 2-6, default 2
void printBarCode(String data, int symbology, int height, int width);

Print QR Code:
data: QR code content
modulesize: QR code block size 1-16
errorlevel: QR code error correction level 0-3
void printQRCode(String data, int modulesize, int errorlevel);

Alignment Direction:
alignment 0 left, 1 center, 2 right
void setAlignment(int alignment);

Set Font Size:
void setTextSize(float textSize);

New N Lines:
void nextLine(int line);

Print Table Text:
Text table content
weight: Row width weight
alignment: Each row alignment direction
void printTableText(in String[] text, in int[] weight, in int[] alignment);

Set Font Bold:
void setTextBold(boolean bold);

**Indonesia:**

Proyek ini adalah demo yang menunjukkan cara memanggil layanan printer. Layanan printer adalah Komponen Layanan Android. Menurut API bindService, aplikasi Anda dapat terhubung dengan layanan printer. Setelah Anda terhubung dengan layanan printer, Anda dapat mengirim perintah Epson untuk mencetak.

Petunjuk Penggunaan:
	1.	Tambahkan file AIDL PrinterInterface.aidl ke paket recieptservice.com.recieptservice.
	2.	Gunakan kode berikut untuk mengikat layanan printer:
   Intent intent = new Intent();
   intent.setClassName(“recieptservice.com.recieptservice”, “recieptservice.com.recieptservice.service.PrinterService”);
   bindService(intent, new ServiceConnection(), Service.BIND_AUTO_CREATE);
	1.	Setelah mengikat berhasil, Anda dapat mulai memanggil antarmuka API yang relevan untuk mencetak.

Dokumentasi AIDL:

Antarmuka Cetak Perintah Epson:
void printEpson(in byte[] data);

Dapatkan Nomor Versi Layanan Printer:
String getServiceVersion();

Cetak Teks String:
void printText(String text);

Cetak Gambar BitMap:
void printBitmap(in Bitmap pic);

Cetak Barcode:
data: Konten barcode
symbology: Jenis barcode
    0 — UPC-A
    1 — UPC-E
    2 — JAN13(EAN13)
    3 — JAN8(EAN8)
    4 — CODE39
    5 — ITF
    6 — CODABAR
    7 — CODE93
    8 — CODE128
height: Tinggi barcode 1-255, default 162
width: Lebar barcode 2-6, default 2
void printBarCode(String data, int symbology, int height, int width);

Cetak Kode QR:
data: Konten kode QR
modulesize: Ukuran blok kode QR 1-16
errorlevel: Tingkat koreksi kesalahan kode QR 0-3
void printQRCode(String data, int modulesize, int errorlevel);

Arah Penjajaran:
alignment 0 kiri, 1 tengah, 2 kanan
void setAlignment(int alignment);

Atur Ukuran Font:
void setTextSize(float textSize);

Baris Baru N:
void nextLine(int line);

Cetak Teks Tabel:
Text konten tabel
weight: Bobot lebar baris
alignment: Arah penjajaran setiap baris
void printTableText(in String[] text, in int[] weight, in int[] alignment);

Atur Font Tebal:
void setTextBold(boolean bold);



