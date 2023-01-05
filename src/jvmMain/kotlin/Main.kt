import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import utils.Crypt
import java.nio.file.FileSystems
import javax.swing.JFileChooser

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun App(window: ComposeWindow) {
    val fs = FileSystems.getDefault()

    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    val advancedMethodOptions = remember { mutableStateOf(false) }

    val path = remember { mutableStateOf(listOf(fs.rootDirectories.first().toString())) }
    val token = remember { mutableStateOf("") }
    val tokenSupportingText = remember { mutableStateOf(Crypt.AES_SUPPORTING_TEXT) }
    val method = remember { mutableStateOf(Crypt.AES) }
    val toggleMethodOptionsButtonText = remember { mutableStateOf("More Options") }

    MaterialTheme {
        if (showDialog.value) {
            AlertDialog(onDismissRequest = { showDialog.value = false },
                confirmButton = { Button(onClick = { showDialog.value = false }) { Text("Close") } },
                modifier = Modifier.width(400.dp).padding(0.dp, 0.dp, 16.dp, 0.dp),
                title = { Text("Alert") },
                text = { Text(dialogText.value) })
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            PathField(path, onExploreClick = {
                val jFileChooser = JFileChooser(path.value.first()).apply {
                    fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
                    isMultiSelectionEnabled = true
                }
                when (jFileChooser.showOpenDialog(null)) {
                    JFileChooser.APPROVE_OPTION -> path.value = jFileChooser.selectedFiles.toList().map { it.path }
                    JFileChooser.ERROR_OPTION -> error("An error occurred!")
                }
            })
            Spacer(modifier = Modifier.height(16.dp))

            TokenField(token, tokenSupportingText)
            Spacer(modifier = Modifier.height(16.dp))

            EncryptionMethodRow(advancedMethodOptions, method, onMethodSelected = { m, supportingText ->
                method.value = m
                tokenSupportingText.value = supportingText
            }, toggleMethodOptionsButtonText, onToggleMethodOptionsButtonClick = {
                advancedMethodOptions.value = !advancedMethodOptions.value

                when (advancedMethodOptions.value) {
                    false -> {
                        toggleMethodOptionsButtonText.value = "More Options"

                        method.value = if (method.value.startsWith(Crypt.AES)) {
                            Crypt.AES
                        } else {
                            Crypt.DES
                        }
                    }

                    true -> {
                        toggleMethodOptionsButtonText.value = "Less Options"

                        method.value = when (method.value) {
                            Crypt.AES -> Crypt.AES_CBC_NO_PADDING
                            else -> Crypt.DES_CBC_NO_PADDING
                        }
                    }
                }
            })
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))

            ProcessButtonsRow(onEncryptButtonClick = {
                val message = try {
                    Crypt().encrypt(method.value, path.value, token.value)
                } catch (e: Error) {
                    e.message ?: ""
                } catch (e: Exception) {
                    e.message ?: ""
                }

                showDialog.value = true
                dialogText.value = message
            }, onDecryptButtonClick = {
                val message = try {
                    Crypt().decrypt(method.value, path.value, token.value)
                } catch (e: Error) {
                    e.message ?: ""
                } catch (e: Exception) {
                    e.message ?: ""
                }

                showDialog.value = true
                dialogText.value = message
            })
        }
    }
}

@Composable
fun PathField(path: MutableState<List<String>>, onExploreClick: () -> Unit) {
    OutlinedTextField(
        path.value.joinToString(","),
        onValueChange = { path.value = it.split(',') },
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        label = { Text("Path") },
        trailingIcon = {
            Text("Explore", modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp).clickable { onExploreClick() })
        },
        singleLine = true
    )
}

@Composable
fun TokenField(token: MutableState<String>, tokenSupportingText: MutableState<String>) {
    OutlinedTextField(
        token.value,
        onValueChange = { token.value = it },
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        label = { Text("Token") },
        singleLine = true
    )
    Spacer(modifier = Modifier.height(2.dp))

    Text(tokenSupportingText.value)
}

@Composable
fun EncryptionMethodRow(
    advancedMethodOptions: MutableState<Boolean>,
    method: MutableState<String>,
    onMethodSelected: (method: String, supportingText: String) -> Unit,
    toggleMethodOptionsButtonText: MutableState<String>,
    onToggleMethodOptionsButtonClick: () -> Unit
) {
    Text("Encryption Method", fontWeight = FontWeight.Bold)

    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        when (advancedMethodOptions.value) {
            false -> BasicMethods(
                method = method, onMethodSelected = onMethodSelected
            )

            true -> AdvancedMethods(
                method = method, onMethodSelected = onMethodSelected
            )
        }
    }

    ToggleMethodOptionsButton(text = toggleMethodOptionsButtonText, onClick = onToggleMethodOptionsButtonClick)
}

@Composable
fun RowScope.BasicMethods(
    method: MutableState<String>, onMethodSelected: (method: String, supportingText: String) -> Unit
) {
    MethodRadioButton(modifier = Modifier.weight(1f).wrapContentHeight(),
        method = Crypt.AES,
        currentMethod = method,
        onMethodSelected = { onMethodSelected(Crypt.AES, Crypt.AES_SUPPORTING_TEXT) })
    Spacer(modifier = Modifier.width(8.dp))

    MethodRadioButton(modifier = Modifier.weight(1f).wrapContentHeight(),
        method = Crypt.DES,
        currentMethod = method,
        onMethodSelected = { onMethodSelected(Crypt.DES, Crypt.DES_SUPPORTING_TEXT) })
}

@Composable
fun RowScope.AdvancedMethods(
    method: MutableState<String>, onMethodSelected: (method: String, supportingText: String) -> Unit
) {
    Column(modifier = Modifier.weight(1f).wrapContentHeight()) {
        listOf(
            Crypt.AES_CBC_NO_PADDING,
            Crypt.AES_CBC_PKCS5_PADDING,
            Crypt.AES_ECB_NO_PADDING,
            Crypt.AES_ECB_PKCS5_PADDING,
        ).forEach { m ->
            MethodRadioButton(modifier = Modifier.wrapContentSize(),
                method = m,
                currentMethod = method,
                onMethodSelected = { onMethodSelected(m, Crypt.AES_SUPPORTING_TEXT) })
        }
    }
    Spacer(modifier = Modifier.width(8.dp))

    Column(modifier = Modifier.weight(1f).wrapContentHeight()) {
        listOf(
            Crypt.DES_CBC_NO_PADDING, Crypt.DES_CBC_PKCS5_PADDING, Crypt.DES_ECB_NO_PADDING, Crypt.DES_ECB_PKCS5_PADDING
        ).forEach { m ->
            MethodRadioButton(modifier = Modifier.wrapContentSize(),
                method = m,
                currentMethod = method,
                onMethodSelected = { onMethodSelected(m, Crypt.DES_SUPPORTING_TEXT) })
        }
    }
}

@Composable
fun MethodRadioButton(
    modifier: Modifier, method: String, currentMethod: MutableState<String>, onMethodSelected: () -> Unit
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = currentMethod.value == method, onClick = onMethodSelected)
        Spacer(modifier = Modifier.height(8.dp))

        Text(method, modifier = Modifier.clickable(onClick = onMethodSelected))
    }
}

@Composable
fun ToggleMethodOptionsButton(text: MutableState<String>, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.wrapContentSize()) {
        Text(text.value)
    }
}

@Composable
fun ProcessButtonsRow(onEncryptButtonClick: () -> Unit, onDecryptButtonClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = onEncryptButtonClick) {
            Text("Encrypt")
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onDecryptButtonClick) {
            Text("Decrypt")
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(position = WindowPosition(Alignment.Center), size = DpSize(800.dp, 580.dp)),
        title = "File Encryption System",
        resizable = false
    ) {
        App(window)
    }
}
