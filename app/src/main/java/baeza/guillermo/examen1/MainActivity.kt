package baeza.guillermo.examen1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import baeza.guillermo.examen1.ui.theme.Examen1Theme
import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import baeza.guillermo.examen1.ui.theme.mainPruple
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen1Theme {
                var vista by rememberSaveable { mutableStateOf(1) }

                if (vista == 1) {
                    //Sing In
                    MySingIn(vistaChange = {vista = it})
                } else if (vista == 2) {
                    // Register
                    MyRegister(vistaChange = {vista = it})
                } else if (vista == 3) {
                    // Home
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(top = 200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Vista 3")
                    }
                } else {
                    // Favorites
                }
            }
        }
    }
}

@Composable
fun MySingIn(vistaChange : (Int) -> Unit) {
    var user by rememberSaveable{ mutableStateOf("") }
    var pswd by rememberSaveable{ mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState,
        topBar = { MyTopNav(enabled = false) },
        bottomBar = { MyBottomNav(enabled = false, vistaChange) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(top = 150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .background(Color.White)
                    .padding(15.dp)
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row() {
                            TextField(
                                value = user,
                                onValueChange = {user = it},
                                label = {Text("Usuario / Email")},
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(R.drawable.usuario),
                                        contentDescription = "Usuario",
                                        modifier = Modifier.height(56.dp)
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row() {
                            TextField(
                                value = pswd,
                                onValueChange = { pswd = it },
                                label = { Text("Password") },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(
                                        onClick = {passwordVisible = !passwordVisible}){
                                        Image(
                                            painter = painterResource(R.drawable.reveal),
                                            contentDescription = "RevealPswd",
                                            modifier = Modifier.height(56.dp)
                                        )
                                    }
                                },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(R.drawable.candado),
                                        contentDescription = "Password",
                                        modifier = Modifier.height(56.dp)
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Button(onClick = {
                                if (user.isNotEmpty() && pswd.isNotEmpty()){
                                    vistaChange(2)
                                }else{
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Completar los campos es obligatorio",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = mainPruple),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Entrar", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        ClickableText(
                            text = AnnotatedString("¿Aún no te has registrado?"),
                            onClick = {
                                vistaChange(2)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun MyBottomNav(enabled:Boolean, vistaChange : (Int) -> Unit){
    BottomNavigation(
        backgroundColor = mainPruple,
        contentColor = Color.White
    ) {
        BottomNavigationItem(selected = false, enabled = enabled, onClick = {vistaChange(3)}, icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home"
            )
        })
        BottomNavigationItem(selected = false, enabled = enabled, onClick = {vistaChange(4)}, icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite"
            )
        })
        BottomNavigationItem(selected = false, enabled = enabled, onClick = {}, icon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil"
            )
        })
    }
}

@Composable
fun MyTopNav(enabled:Boolean){
    TopAppBar(
        title = {Text(text = "Monkey Films")},
        backgroundColor = mainPruple,
        contentColor = Color.White,
        elevation = 123.dp,
        modifier = Modifier.height(60.dp),
        navigationIcon = {
            IconButton(onClick = { }, enabled = enabled) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { }, enabled = enabled) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir")
            }
        }
    )
}

@Composable
fun MyRegister(vistaChange : (Int) -> Unit) {
    var user by rememberSaveable { mutableStateOf("") }
    var pswd by rememberSaveable { mutableStateOf("") }
    var pswd2 by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var deportes by rememberSaveable { mutableStateOf(false)}
    var accion by rememberSaveable { mutableStateOf(false)}
    var sifi by rememberSaveable { mutableStateOf(false)}
    var romance by rememberSaveable { mutableStateOf(false)}
    var historicas by rememberSaveable { mutableStateOf(false)}
    var documentales by rememberSaveable { mutableStateOf(false)}
    val maxChar = 50
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState,
        topBar = { MyTopNav(enabled = false) },
        bottomBar = { MyBottomNav(enabled = false, vistaChange) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = user,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            user = it
                        }
                    },
                    label = { Text(text = "Usuario") },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = "${user.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = email,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            email = it
                        }
                    },
                    label = { Text(text = "Email") },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = "${email.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = pswd,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            pswd = it
                        }
                    },
                    label = { Text(text = "Contraseña") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = "${pswd.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = pswd2,
                    onValueChange = {
                        if (it.length <= maxChar) {
                            pswd2 = it
                        }
                    },
                    label = { Text(text = "Repite la contraseña") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Text(
                    text = "${pswd2.length} / $maxChar",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Intereses",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    MyCheckbox("Deportes", state = deportes) {deportes = it}

                    Spacer(modifier = Modifier.width(40.dp))

                    MyCheckbox("Romance", state = romance) {romance = it}
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    MyCheckbox("Acción", state = accion) {accion = it}

                    Spacer(modifier = Modifier.width(40.dp))

                    MyCheckbox("Históricas", state = historicas) {historicas = it}
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    MyCheckbox("Si-Fi", state = sifi) {sifi = it}

                    Spacer(modifier = Modifier.width(40.dp))

                    MyCheckbox("Documentales", state = documentales) {documentales = it}
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        if (user.isNotEmpty() && pswd.isNotEmpty() && pswd == pswd2 && (email.contains('@') && email.contains('.'))) {
                            vistaChange(3)
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Completar correctamente los campos es obligatorio",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = mainPruple),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Registrarse", color = Color.White)
                }
            }
        }
    )
}

@Composable
fun MyCheckbox(texto:String, state:Boolean, onCheckedChange: (Boolean) -> Unit) {
    Checkbox(
        checked = state,
        onCheckedChange = { onCheckedChange(!state) },
        colors = CheckboxDefaults.colors(checkedColor = mainPruple)
    )
    Text(text = texto, modifier = Modifier.padding(top = 13.dp))
}