package com.example.mymusicappui.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
//import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymusicappui.Screen
import com.example.mymusicappui.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mymusicappui.AccountDialog
import com.example.mymusicappui.Home
import com.example.mymusicappui.Library
import com.example.mymusicappui.MainViewModel
//import com.example.mymusicappui.MiniPlayer
//import com.example.mymusicappui.MusicPlayerManager
import com.example.mymusicappui.R
import com.example.mymusicappui.RawSong
import com.example.mymusicappui.ui.theme.SearchScreen
import com.example.mymusicappui.Subscription
import com.example.mymusicappui.rawSongs
import com.example.mymusicappui.screenInBottom
import com.example.mymusicappui.ui.theme.AccountView
import com.example.mymusicappui.ui.theme.Browse
import com.example.mymusicappui.ui.theme.MusicPlayerScreen

//import com.example.mymusicappui.ui.theme.Song


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(){
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember { mutableStateOf(false) }
    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    //Allow us to find out on which "View" we current are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    //To change the title while you click
    val title = remember{
        //TODO change that to currentScreen.title
        mutableStateOf(currentScreen.title)
    }

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {it != ModalBottomSheetValue.HalfExpanded}
    )

    val roundCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp

    val bottomBar : @Composable () -> Unit  = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent,
                contentColor = Color.White,
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5))
                            )
                        )
                ) {
                    BottomNavigation(
                        Modifier.fillMaxWidth(),
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp
                    ) {
                        screenInBottom.forEach() { item ->
                            val isSelected = currentRoute == item.bRoute
                            Log.d(
                                "Navigation",
                                "Item:${item.bTitle}, Current Route: $currentRoute, Is Selected : $isSelected"
                            )
                            val tint = if (isSelected) Color.White else Color.Black
                            BottomNavigationItem(
                                selected = currentRoute == item.bRoute,
                                onClick = {
                                    controller.navigate(item.bRoute)
                                    title.value = item.bTitle
                                },
                                icon = {
                                    Icon(
                                        tint = tint,
                                        contentDescription = item.bTitle,
                                        painter = painterResource(id = item.icon)
                                    )
                                },
                                label = { Text(text = item.bTitle, color = tint) },
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundCornerRadius,
            topEnd = roundCornerRadius
        ),
        sheetContent = {
            MoreBottomSheet(modifier = modifier)
        }
    ) {
        Scaffold(
            bottomBar = bottomBar,
            topBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent,
                    contentColor = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5))
                                )
                            )
                    ) {
                        TopAppBar(
                            title = { Text(title.value) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent //Important to make gradient visible
                            ),
                            actions = {
                                if(currentRoute == Screen.BottomScreen.Library.bRoute){
                                    IconButton(onClick = {
                                        controller.navigate("Search")
                                    }) {
                                        Icon(Icons.Default.Search, contentDescription = null)
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            if (modalSheetState.isVisible)
                                                modalSheetState.hide()
                                            else
                                                modalSheetState.show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = null
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    //Open the Drawer
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Menu"
                                    )
                                }
                            }
                        )
                    }
                }
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ){
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column {
                        screensInDrawer.forEach{item ->
                            DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                if (item.dRoute == "add_account"){
                                    dialogOpen.value = true
                                }else {
                                    controller.navigate(item.dRoute)
                                    title.value = item.dTitle
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.8f))
                }
                }
               
        ) { paddingValues ->
            // Main content - each screen handles its own music player
            Navigation(navController = controller, viewModel = viewModel, pd = paddingValues)

            // Account Dialog
            AccountDialog(dialogOpen = dialogOpen)
        }
    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreen,
    onDrawerItemClicked: () -> Unit,
){
    val background = if(selected) Color.DarkGray else Color.White
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }
    ) {
        androidx.compose.material.Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = item.dTitle,
            //TODO h5 font size but i have change to it labelMedium Fontsize
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
fun MoreBottomSheet(modifier: Modifier){
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                MaterialTheme.colorScheme.primary
            )
    ){
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Row(modifier = modifier.padding(16.dp)){
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_settings_24),
                    contentDescription = "Settings"
                )
                Text(text = "Settings", fontSize = 20.sp, color = Color.White)
            }

            Row(modifier = modifier.padding(16.dp)){
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_share_24),
                    contentDescription = "Share"
                )
                Text("Share", fontSize = 20.sp, color = Color.White)
            }

            Row(modifier = modifier.padding(16.dp)){
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_help_center_24),
                    contentDescription = "Help Center"
                )
                Text("Help Center", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {


    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(pd)
    ) {
        composable(Screen.BottomScreen.Home.bRoute) {
            Home()
        }

        composable(Screen.BottomScreen.Browse.bRoute) {
            Browse(viewModel = viewModel, navController = navController)
        }

        composable(Screen.BottomScreen.Library.bRoute) {
            Library(viewModel = viewModel)
        }

        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }

        composable(Screen.DrawerScreen.Subscription.route) {
            Subscription()
        }

        // Add the music player route
        composable(
            route = "music_player_screen/{songIndex}",
            arguments = listOf(
                navArgument("songIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val songIndex = backStackEntry.arguments?.getInt("songIndex") ?: 0
            MusicPlayerScreen(
                songIndex = songIndex,
                viewModel = viewModel,
                navController = navController
            )


        }
    }
}