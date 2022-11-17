package baeza.guillermo.examen1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import baeza.guillermo.examen1.ui.theme.mainPruple
import kotlinx.coroutines.launch
import baeza.guillermo.examen1.models.Carta

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen1Theme {
                var vista by rememberSaveable { mutableStateOf(1) }
                val cartas by rememberSaveable { mutableStateOf(mutableListOf<Carta>(
                    Carta("Pelicula 1", 10, R.drawable.paisaje, "Descripcion 1"),
                    Carta("Pelicula 2", 30, R.drawable.paisaje, "Descripcion 2")
                )) }
                val favoritas by rememberSaveable { mutableStateOf(mutableListOf<Carta>(
                    Carta("Pelicula 1", 10, R.drawable.paisaje, "Descripcion 1")
                )) }

                if (vista == 1) {
                    //Sing In
                    MySingIn(vistaChange = {vista = it})
                } else if (vista == 2) {
                    // Register
                    MyRegister(vistaChange = {vista = it})
                } else if (vista == 3) {
                    // Home
                    MyHome(
                        cartas = cartas,
                        vistaChange = {vista = it},
                        onChangeFav = {
                            if(!favoritas.contains(it))
                                favoritas.add(Carta(it.nombre, it.likes, it.imagen, it.descripcion))
                        },
                        addCarta = {cartas.add(it)}
                    )
                } else {
                    // Favorites
                    MyFavs(
                        favoritas = favoritas,
                        onChangeFav = {favoritas.remove(it)},
                        vistaChange = {vista = it}
                    )
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
        topBar = { MyTopNavDis() },
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
                                    vistaChange(3)
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
fun MyTopNav(enabled:Boolean, scaffoldState : ScaffoldState, addCarta : (Carta) -> Unit, vistaChange : (Int) -> Unit){
    var add by rememberSaveable { mutableStateOf(false) }
    var nombreCarta by rememberSaveable{ mutableStateOf("") }
    var likesCarta by rememberSaveable{ mutableStateOf("") }
    var descripcion by rememberSaveable{ mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
            IconButton(onClick = { add = true }, enabled = enabled) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir")
            }
        }
    )

    if(add) {
        Dialog(
            onDismissRequest ={
                add = false
                nombreCarta = ""
                likesCarta = ""
                descripcion = ""
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Añadir Película",
                    fontSize = 25.sp
                )
                Spacer(Modifier.padding(top = 20.dp))
                TextField(
                    value = nombreCarta,
                    onValueChange = { nombreCarta = it },
                    placeholder = { Text(text = "Nombre de la peli") }
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TextField(
                    value = likesCarta,
                    onValueChange = { likesCarta = it },
                    placeholder = { Text(text = "Puntuacion de la peli") }
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = { Text(text = "Descripción de la peli") }
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Button(
                    onClick = {
                        if (nombreCarta.isNotEmpty() && likesCarta.isNotEmpty()) {
                            addCarta(Carta(nombreCarta, likesCarta.toInt(), R.drawable.paisaje, descripcion))
                            nombreCarta = ""
                            likesCarta = ""
                            vistaChange(4)
                            vistaChange(3)
                            add = false
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Completar los campos es obligatorio",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = mainPruple)
                ) {
                    Text("Añadir", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MyTopNavDis(){
    TopAppBar(
        title = {Text(text = "Monkey Films")},
        backgroundColor = mainPruple,
        contentColor = Color.White,
        elevation = 123.dp,
        modifier = Modifier.height(60.dp),
        navigationIcon = {
            IconButton(onClick = { }, enabled = false) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = {}, enabled = false) {
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
        topBar = { MyTopNavDis() },
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

                    Spacer(modifier = Modifier.width(37.dp))

                    MyCheckbox("Romance", state = romance) {romance = it}
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    MyCheckbox("Acción", state = accion) {accion = it}

                    Spacer(modifier = Modifier.width(54.dp))

                    MyCheckbox("Históricas", state = historicas) {historicas = it}
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    MyCheckbox("Si-Fi", state = sifi) {sifi = it}

                    Spacer(modifier = Modifier.width(73.dp))

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

@Composable
fun MyHome(cartas: MutableList<Carta>, onChangeFav: (Carta) -> Unit, vistaChange : (Int) -> Unit, addCarta : (Carta) -> Unit) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopNav(enabled = true, scaffoldState = scaffoldState, addCarta, vistaChange) },
        bottomBar = { MyBottomNav(enabled = true, vistaChange = vistaChange) },
        content = {
            Column(
                Modifier.padding(2.dp)
            ){
                cartas.forEach { item ->
                    MyCard(item, onChangeFav = onChangeFav, scaffoldState)
                    Spacer(modifier = Modifier.padding(bottom = 2.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyCard(carta:Carta, onChangeFav: (Carta) -> Unit, scaffoldState : ScaffoldState) {
    val scope = rememberCoroutineScope()
    var verMas by rememberSaveable { mutableStateOf(false) }

    if(verMas) {
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp),
            onClick = {verMas = !verMas}
        ) {
            Column() {
                Image(
                    painterResource(id = carta.imagen),
                    contentDescription = carta.descripcion,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(modifier = Modifier.padding(start = 1.dp, top = 4.dp)) {
                    Image(
                        painter = painterResource(R.drawable.usuario),
                        contentDescription = "Icono",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Column(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 7.dp)
                                .fillMaxWidth(0.8f)
                        ) {
                            Text(carta.nombre)
                            Text(carta.descripcion,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                fontSize = 14.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.Star, contentDescription = "Puntuacion"
                        )
                        Text(text = carta.likes.toString(),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 8.dp))
                    }
                }
            }
        }
    } else {
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            onClick = {verMas = !verMas}
        ) {
            Row(Modifier.padding(start = 1.dp, top = 4.dp)){
                Image(
                    painter = painterResource(R.drawable.usuario),
                    contentDescription = "Icono",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column(Modifier.padding(start = 20.dp, top = 7.dp)) {
                        Text(carta.nombre)
                        Row{
                            Icon(
                                imageVector = Icons.Filled.Star, contentDescription = "Puntuacion",
                                Modifier
                                    .size(20.dp)
                                    .padding(top = 1.dp)
                            )
                            Text(text = carta.likes.toString(), fontSize = 12.sp, modifier = Modifier.padding(top = 3.dp))
                        }
                    }
                    IconButton(
                        onClick = {onChangeFav(carta)
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Pelicula añadida a favoritos...",
                                    duration = SnackbarDuration.Short
                                )
                            }},
                        modifier = Modifier.padding(top = 7.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
                    }
                }
            }
        }
    }
}

@Composable
fun MyFavs(favoritas : MutableList<Carta>, onChangeFav: (Carta) -> Unit, vistaChange : (Int) -> Unit) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopNavDis() },
        bottomBar = { MyBottomNav(enabled = true, vistaChange = vistaChange) },
        content = {
            Column(
                Modifier.padding(2.dp)
            ){
                favoritas.forEach { item ->
                    MyFavCard(item, onChangeFav = onChangeFav, vistaChange)
                    Spacer(modifier = Modifier.padding(bottom = 2.dp))
                }
            }
        }
    )
}

@Composable
fun MyFavCard(carta:Carta, onChangeFav: (Carta) -> Unit, vistaChange : (Int) -> Unit) {
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(Modifier.padding(start = 1.dp, top = 4.dp)){
            Image(
                painter = painterResource(R.drawable.usuario),
                contentDescription = "Icono",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Column(Modifier.padding(start = 20.dp, top = 7.dp)) {
                    Text(carta.nombre)
                    Row{
                        Icon(
                            imageVector = Icons.Filled.Star, contentDescription = "Puntuacion",
                            Modifier
                                .size(20.dp)
                                .padding(top = 1.dp)
                        )
                        Text(text = carta.likes.toString(), fontSize = 12.sp, modifier = Modifier.padding(top = 3.dp))
                    }
                }
                IconButton(
                    onClick = {
                        onChangeFav(carta)
                        vistaChange(3)
                        vistaChange(4)
                    },
                    modifier = Modifier.padding(top = 7.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                }
            }
        }
    }
}