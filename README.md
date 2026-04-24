# PrintDemo

This project is a demo of Blueprint mPOS 332 Mobile Printer Thermal series to shows how to call the printer service. The printer service is an Android Service Component. According to the `bindService` API, your application can connect with the printer service. Once you have connected with the printer service, you can send the Epson command to print.

## Usage Instructions

1.  **Add the AIDL file**: Add `PrinterInterface.aidl` to the package `recieptservice.com.recieptservice`.
2.  **Bind the printer service**: Use the following code to bind the service:

    ```kotlin
    val intent = Intent().apply {
        component = ComponentName(
            "recieptservice.com.recieptservice",
            "recieptservice.com.recieptservice.service.PrinterService"
        )
    }
    bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE)
    ```

3.  **Call APIs**: After binding successfully, you can start calling the relevant API interfaces to print.

---

## AIDL Documentation

### Epson Command Print Interface
```aidl
void printEpson(in byte[] data);
```

### Get Printer Service Version Number
```aidl
String getServiceVersion();
```

### Print String Text
```aidl
void printText(String text);
```

### Print BitMap Image
```aidl
void printBitmap(in Bitmap pic);
```

### Print Barcode
```aidl
void printBarCode(String data, int symbology, int height, int width);
```
**Parameters:**
- `data`: Barcode content
- `symbology`: Barcode type
    - `0` — UPC-A
    - `1` — UPC-E
    - `2` — JAN13 (EAN13)
    - `3` — JAN8 (EAN8)
    - `4` — CODE39
    - `5` — ITF
    - `6` — CODABAR
    - `7` — CODE93
    - `8` — CODE128
- `height`: Barcode height (1-255, default 162)
- `width`: Barcode width (2-6, default 2)

### Print QR Code
```aidl
void printQRCode(String data, int modulesize, int errorlevel);
```
**Parameters:**
- `data`: QR code content
- `modulesize`: QR code block size (1-16)
- `errorlevel`: QR code error correction level (0-3)

### Alignment Direction
```aidl
void setAlignment(int alignment);
```
- `0`: Left
- `1`: Center
- `2`: Right

### Set Font Size
```aidl
void setTextSize(float textSize);
```

### New N Lines
```aidl
void nextLine(int line);
```

### Print Table Text
```aidl
void printTableText(in String[] text, in int[] weight, in int[] alignment);
```
**Parameters:**
- `text`: Table content (array of strings)
- `weight`: Row width weight (array of ints)
- `alignment`: Each row alignment direction (array of ints)

### Set Font Bold
```aidl
void setTextBold(boolean bold);
```
