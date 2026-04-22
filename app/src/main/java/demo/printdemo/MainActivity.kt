package demo.printdemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sr.SrPrinter
import demo.printdemo.databinding.ActivityMainBinding
import recieptservice.com.recieptservice.PSAMCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var textSize = 24.0f
    private val printer by lazy { SrPrinter.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        with(binding) {
            btnPrint.setOnClickListener {
                runCatching { printer.printText(etPrintText.text.toString()) }
                    .onFailure { it.printStackTrace() }
            }
            btnPrintImage.setOnClickListener {
                runCatching {
                    printer.printBitmap(BitmapFactory.decodeResource(resources, R.mipmap.gh))
                    printer.printText("${etPrintText.text}\n")
                }.onFailure { it.printStackTrace() }
            }
            btnPrintQr.setOnClickListener {
                runCatching {
                    printer.printQRCode("123456", 4, 3)
                    printer.printText("${etPrintText.text}\n")
                }.onFailure { it.printStackTrace() }
            }
            btnPrintBarcode.setOnClickListener {
                // runCatching { printer.print128BarCode("12345678901234567890123", 3, 80, 2) }
            }
            btnBold.setOnClickListener {
                runCatching { printer.setTextBold(true) }
                    .onFailure { it.printStackTrace() }
            }
            btnFontSmall.setOnClickListener {
                runCatching {
                    textSize--
                    printer.setTextSize(textSize)
                }.onFailure { it.printStackTrace() }
            }
            btnFontLarge.setOnClickListener {
                runCatching {
                    textSize++
                    printer.setTextSize(textSize)
                }.onFailure { it.printStackTrace() }
            }
            btnAlignLeft.setOnClickListener {
                runCatching {
                    printer.printText("\n")
                    printer.setAlignment(0)
                }.onFailure { it.printStackTrace() }
            }
            btnAlignCenter.setOnClickListener {
                runCatching {
                    printer.printText("\n")
                    printer.setAlignment(1)
                }.onFailure { it.printStackTrace() }
            }
            btnAlignRight.setOnClickListener {
                runCatching {
                    printer.printText("\n")
                    printer.setAlignment(2)
                }.onFailure { it.printStackTrace() }
            }
            btnNewLine.setOnClickListener {
                runCatching { printer.nextLine(1) }
                    .onFailure { it.printStackTrace() }
            }
            btnPrintTable.setOnClickListener {
                runCatching {
                    printer.printTableText(
                        arrayOf("Kung Pao Chicken", "1 portion", "20 Yuan"),
                        intArrayOf(1, 1, 1),
                        intArrayOf(1, 1, 1)
                    )
                    printer.printTableText(
                        arrayOf("Chicken cubes", "1 portion", "10 Yuan"),
                        intArrayOf(1, 1, 1),
                        intArrayOf(1, 1, 1)
                    )
                }.onFailure { it.printStackTrace() }
            }
            btnCheckPsam.setOnClickListener {
                printer.checkPSAMCard(2000, object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        runOnUiThread {
                            val message = if (data.getOrNull(0) == 0x59.toByte()) "has PSAM card" else "no PSAM card"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        runOnUiThread { Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            }
            btnActivePsam.setOnClickListener {
                printer.activatePSAMCard(2000, object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        runOnUiThread { Toast.makeText(applicationContext, data.toHexString(), Toast.LENGTH_LONG).show() }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        runOnUiThread { Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            }
            btnSendPsam.setOnClickListener {
                printer.transmitPSAMCard(2000, "00a4040005010203040500".hexToByteArray(), object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        runOnUiThread { Toast.makeText(applicationContext, data.toHexString(), Toast.LENGTH_LONG).show() }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        runOnUiThread { Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            }
            btnDeactivePsam.setOnClickListener {
                printer.deactivatePSAMCard(2000, object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        runOnUiThread {
                            val success = data.getOrNull(0) == 0x59.toByte()
                            val message = if (success) "deactivatePSAMCard success" else "deactivatePSAMCard failed"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        runOnUiThread { Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            }
        }
    }

    private fun ByteArray.toHexString(): String = joinToString("") { "%02x".format(it) }

    private fun String.hexToByteArray(): ByteArray {
        val len = length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}
