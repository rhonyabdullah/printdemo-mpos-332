package demo.printdemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import androidx.activity.ComponentActivity
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sr.SrPrinter
import recieptservice.com.recieptservice.PSAMCallback

class MainActivity : ComponentActivity() {

    private val printer by lazy { SrPrinter.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            PrintScreen(printer = printer)
        }
    }
}

@Composable
fun PrintScreen(printer: SrPrinter) {
    val context = LocalContext.current
    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    var printText by remember { mutableStateOf("") }
    var textSize by remember { mutableFloatStateOf(24.0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = printText,
            onValueChange = { printText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.hint_print_text)) }
        )

        Button(
            onClick = {
                runCatching { printer.printText(printText) }
                    .onFailure { it.printStackTrace() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_print))
        }

        Button(
            onClick = {
                runCatching {
                    printer.printBitmap(BitmapFactory.decodeResource(context.resources, R.mipmap.gh))
                    printer.printText("$printText\n")
                }.onFailure { it.printStackTrace() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_print_image))
        }

        Button(
            onClick = {
                runCatching {
                    printer.printQRCode("123456", 4, 3)
                    printer.printText("$printText\n")
                }.onFailure { it.printStackTrace() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_print_qr))
        }

        Button(
            onClick = {
                // runCatching { printer.print128BarCode("12345678901234567890123", 3, 80, 2) }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_print_barcode))
        }

        Button(
            onClick = {
                runCatching { printer.setTextBold(true) }
                    .onFailure { it.printStackTrace() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_bold))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    runCatching {
                        textSize--
                        printer.setTextSize(textSize)
                    }.onFailure { it.printStackTrace() }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.btn_font_small))
            }

            Button(
                onClick = {
                    runCatching {
                        textSize++
                        printer.setTextSize(textSize)
                    }.onFailure { it.printStackTrace() }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.btn_font_large))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    runCatching {
                        printer.printText("\n")
                        printer.setAlignment(0)
                    }.onFailure { it.printStackTrace() }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.btn_align_left))
            }

            Button(
                onClick = {
                    runCatching {
                        printer.printText("\n")
                        printer.setAlignment(1)
                    }.onFailure { it.printStackTrace() }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.btn_align_center))
            }

            Button(
                onClick = {
                    runCatching {
                        printer.printText("\n")
                        printer.setAlignment(2)
                    }.onFailure { it.printStackTrace() }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.btn_align_right))
            }
        }

        Button(
            onClick = {
                runCatching { printer.nextLine(1) }
                    .onFailure { it.printStackTrace() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_new_line))
        }

        Button(
            onClick = {
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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.btn_print_table))
        }

        Button(
            onClick = {
                printer.checkPSAMCard(2000, object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        mainHandler.post {
                            val message = if (data.getOrNull(0) == 0x59.toByte()) "has PSAM card" else "no PSAM card"
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        mainHandler.post { Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("checkPSAM")
        }

        Button(
            onClick = {
                printer.activatePSAMCard(2000, object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        mainHandler.post { Toast.makeText(context, data.toHexString(), Toast.LENGTH_LONG).show() }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        mainHandler.post { Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("activePSAM")
        }

        Button(
            onClick = {
                printer.transmitPSAMCard(2000, "00a4040005010203040500".hexToByteArray(), object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        mainHandler.post { Toast.makeText(context, data.toHexString(), Toast.LENGTH_LONG).show() }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        mainHandler.post { Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("sendPSAMData")
        }

        Button(
            onClick = {
                printer.deactivatePSAMCard(2000, object : PSAMCallback.Stub() {
                    @Throws(RemoteException::class)
                    override fun success(data: ByteArray) {
                        mainHandler.post {
                            val success = data.getOrNull(0) == 0x59.toByte()
                            val message = if (success) "deactivatePSAMCard success" else "deactivatePSAMCard failed"
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }

                    @Throws(RemoteException::class)
                    override fun error(errorCode: Int, errorMsg: String) {
                        mainHandler.post { Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show() }
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("deactivePSAM")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrintScreenPreview() {
    val context = LocalContext.current
    val printer = remember { SrPrinter.getInstance(context) }
    PrintScreen(printer = printer)
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
