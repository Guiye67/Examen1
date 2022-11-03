package baeza.guillermo.examen1

import android.os.Bundle
import android.widget.Space
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import baeza.guillermo.examen1.ui.theme.Examen1Theme
import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Visibility
import baeza.guillermo.examen1.ui.theme.mainPruple

var vista = 1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen1Theme {
                if (vista == 1) {
                    MyScaffold()
                } else if (vista == 2) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(top = 200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                    }
                } else if (vista == 3) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(top = 200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                    }
                }
            }
        }
    }
}

@Composable
fun MySingIn() {
    val userState = remember{ mutableStateOf(TextFieldValue()) }
    val pswdState = remember{ mutableStateOf(TextFieldValue()) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
                    Image(
                        painter = painterResource(R.drawable.usuario),
                        contentDescription = "Usuario",
                        modifier = Modifier.height(56.dp)
                    )
                    TextField(
                        value = userState.value,
                        onValueChange = {userState.value = it},
                        label = {Text("Usuario / Email")}
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    Image(
                        painter = painterResource(R.drawable.candado),
                        contentDescription = "Password",
                        modifier = Modifier.height(56.dp)
                    )
                    TextField(
                        value = pswdState.value,
                        onValueChange = { pswdState.value = it },
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
                                    modifier = Modifier.height(55.dp)
                                )
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = {vista = 2},
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
                    onClick = {vista = 3}
                )
            }
        }
    }

}

@Composable
fun MyBottomNav(){
    BottomNavigation(
        backgroundColor = mainPruple,
        contentColor = Color.White
    ) {
        BottomNavigationItem(selected = false, onClick = {vista = 4}, icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home"
            )
        })
        BottomNavigationItem(selected = false, onClick = { vista = 5 }, icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite"
            )
        })
        BottomNavigationItem(selected = false, onClick = { vista = 6 }, icon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil"
            )
        })
    }
}

@Composable
fun MyTopNav(){
    TopAppBar(
        title = {Text(text = "Monkey Films")},
        backgroundColor = mainPruple,
        contentColor = Color.White,
        elevation = 123.dp,
        modifier = Modifier.height(60.dp),
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir")
            }
        }
    )
}

@Composable
fun MyScaffold() {
    Scaffold(
        topBar = { MyTopNav() },
        bottomBar = { MyBottomNav() },
        content = { MySingIn() }
    )
}