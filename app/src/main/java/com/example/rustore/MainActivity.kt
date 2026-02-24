package com.example.rustore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.rustore.ui.theme.BgGray
import com.example.rustore.ui.theme.CatColors
import com.example.rustore.ui.theme.DivColor
import com.example.rustore.ui.theme.RuBlue
import com.example.rustore.ui.theme.RuBlueDark
import com.example.rustore.ui.theme.RuStoreTheme
import com.example.rustore.ui.theme.TextGray
import com.example.rustore.ui.theme.TextLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import java.lang.ProcessBuilder.Redirect.to

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RuStoreApp() }
    }
}



data class Shot(val id: String, val desc: String)

data class App(
    val id: String, val name: String, val dev: String, val cat: AppCategory,
    val short: String, val full: String, val rate: Float, val dls: String,
    val age: AgeRating, val shots: List<Shot>, val size: String = "50 МБ", val ver: String = "1.0",
    @DrawableRes val iconRes: Int = R.drawable.ic_default_app
)

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Ok<T>(val data: T) : State<T>()
    data class Err(val msg: String) : State<Nothing>()
}

fun allApps() = Repo
fun appById(id: String) = Repo.find { it.id == id }
fun appsByCat(c: AppCategory) = Repo.filter { it.cat == c }
fun searchApps(q: String) = if (q.isBlank()) emptyList() else Repo.filter {
    it.name.contains(q, true) || it.short.contains(q, true) || it.dev.contains(q, true)
}

fun popularApps() = Repo.sortedByDescending { it.rate }.take(5)
fun catStats() = AppCategory.entries.associateWith { c -> Repo.count { it.cat == c } }

@kotlinx.serialization.Serializable
object Onboard
@kotlinx.serialization.Serializable
object Showcase
@kotlinx.serialization.Serializable
object Categories
@kotlinx.serialization.Serializable
object Search
@kotlinx.serialization.Serializable
data class CatApps(val cat: String)
@kotlinx.serialization.Serializable
data class AppDetail(val id: String)
@Serializable
data class Screenshots(val id: String, val idx: Int)

val Context.dataStore by preferencesDataStore("rustore_prefs")

@Composable
fun RuStoreApp() {
    val nav = rememberNavController()
    val context = LocalContext.current

    val onboardShown by context.dataStore.data
        .map { it[booleanPreferencesKey("onboard_shown")] ?: false }
        .collectAsState(initial = false)

    var shown by remember { mutableStateOf(onboardShown) }

    NavHost(
        navController = nav,
        startDestination = if (shown) "showcase" else "onboard",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("onboard") {
            OnboardScreen {
                shown = true
                nav.navigate("showcase") {
                    popUpTo("onboard") { inclusive = true }
                }
            }
        }

        composable("showcase") {
            ShowcaseScreen(
                onApp = { nav.navigate("app_detail/$it") },
                onCats = { nav.navigate("categories") },
                onSearch = { nav.navigate("search") }
            )
        }

        composable("categories") {
            CategoriesScreen { nav.navigate("cat_apps/${it.name}") }
        }

        composable("cat_apps/{cat}") { backStackEntry ->
            val cat = try {
                AppCategory.valueOf(backStackEntry.arguments?.getString("cat") ?: "GAMES")
            } catch (_: Exception) {
                AppCategory.GAMES
            }
            CatAppsScreen(cat, { nav.popBackStack() }) {
                nav.navigate("app_detail/$it")
            }
        }

        composable("search") {
            SearchScreen({ nav.popBackStack() }) { nav.navigate("app_detail/$it") }
        }

        composable("app_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            AppDetailScreen(id, { nav.popBackStack() }) { idx ->
                nav.navigate("screenshots/$id/$idx")
            }
        }

        composable("screenshots/{id}/{idx}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val idx = backStackEntry.arguments?.getString("idx")?.toIntOrNull() ?: 0
            ScreenshotScreen(id, idx) { nav.popBackStack() }
        }
    }
}

@Composable
fun OnboardScreen(onCont: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(120.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_rustore),
                contentDescription = "RuStore",
                modifier = Modifier.size(240.dp)
            )

            Spacer(Modifier.height(90.dp))

            Text(
                text = "Приложения\nВ Вашем\nКармане",
                fontSize = 39.sp,
                lineHeight = 50.sp,
                letterSpacing = 0.7.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }

        Button(
            onClick = onCont,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0078FF)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Text(
                text = "🚀 Начать",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseScreen(onApp: (String) -> Unit, onCats: () -> Unit, onSearch: () -> Unit) {
    var state by remember { mutableStateOf<State<List<App>>>(State.Loading) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        state = State.Ok(allApps())
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            state = State.Loading
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BgGray)) {
            TopAppBar(
                title = { Text("RuStore", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RuBlue),
                actions = {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                    }
                }
            )

            when (state) {
                State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = RuBlue)
                }
                is State.Err -> ErrView((state as State.Err).msg) { state = State.Loading }
                is State.Ok -> AppsList((state as State.Ok).data, onApp, onCats)
            }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            state = State.Ok(allApps())
            isRefreshing = false
        }
    }
}

@Composable
fun AppsList(apps: List<App>, onApp: (String) -> Unit, onCats: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                onClick = onCats,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RuBlue)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Категории приложений", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Финансы, игры, инструменты", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                }
            }
        }
        item { Text("Рекомендуемые", fontWeight = FontWeight.SemiBold) }
        items(apps, key = { it.id }) { app -> AppCard(app) { onApp(app.id) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(app: App, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = app.iconRes),
                contentDescription = app.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = app.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(text = app.short, fontSize = 12.sp, color = TextGray, maxLines = 2)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = CatColors[app.cat]?.copy(alpha = 0.1f) ?: Color.Unspecified
                    ) {
                        Text(
                            text = app.cat.title,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            color = CatColors[app.cat] ?: RuBlue
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(text = app.age.label, fontSize = 10.sp, color = TextLight)
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFFFFB300))
                    Text(text = "%.1f".format(app.rate), fontSize = 10.sp)
                }
            }
            Button(
                onClick = {},
                modifier = Modifier.height(28.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RuBlue),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) { Text("Открыть", fontSize = 12.sp) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onCat: (AppCategory) -> Unit) {
    val stats = remember { catStats() }
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { isRefreshing = true },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BgGray)) {
            TopAppBar(
                title = { Text("Категории", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RuBlue)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(AppCategory.entries) { cat ->
                    Card(
                        onClick = { onCat(cat) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(CatColors[cat]?.copy(alpha = 0.1f) ?: RuBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = CatIcons[cat] ?: Icons.Default.Menu,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = CatColors[cat] ?: RuBlue
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = cat.title, fontWeight = FontWeight.SemiBold)
                                Text(text = "${stats[cat]} приложений", fontSize = 12.sp, color = TextGray)
                            }
                            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextLight)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            isRefreshing = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatAppsScreen(cat: AppCategory, onBack: () -> Unit, onApp: (String) -> Unit) {
    var state by remember { mutableStateOf<State<List<App>>>(State.Loading) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(cat) {
        delay(500)
        state = State.Ok(appsByCat(cat))
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            state = State.Loading
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BgGray)) {
            TopAppBar(
                title = { Text(text = cat.title, fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CatColors[cat] ?: RuBlue),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )

            when (state) {
                State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CatColors[cat] ?: RuBlue)
                }
                is State.Err -> ErrView((state as State.Err).msg) { state = State.Loading }
                is State.Ok -> {
                    val apps = (state as State.Ok).data
                    if (apps.isEmpty()) {
                        EmptyView("Нет приложений", "В этой категории пока ничего нет")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(apps, key = { it.id }) { app ->
                                AppCard(app) { onApp(app.id) }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            state = State.Ok(appsByCat(cat))
            isRefreshing = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBack: () -> Unit, onApp: (String) -> Unit) {
    var q by remember { mutableStateOf("") }
    var res by remember { mutableStateOf<List<App>>(emptyList()) }
    val pop = remember { popularApps() }
    val focusRequester = remember { FocusRequester() }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { delay(300); focusRequester.requestFocus() }
    LaunchedEffect(q) { delay(300); res = searchApps(q) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            res = emptyList()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BgGray)) {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = q,
                        onValueChange = { q = it },
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        placeholder = { Text("Поиск...") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextGray) },
                        trailingIcon = {
                            if (q.isNotEmpty()) {
                                IconButton(onClick = { q = "" }) {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = TextGray)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = RuBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RuBlue),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )

            when {
                q.isEmpty() -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row() {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = null,
                                tint = RuBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Популярные", fontWeight = FontWeight.SemiBold)
                        }
                    }
                    items(pop, key = { it.id }) { app -> AppCard(app) { onApp(app.id) } }
                }
                res.isEmpty() -> EmptyView("Не найдено", "По запросу \"$q\" ничего нет", Icons.Outlined.Close)
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Text("Найдено: ${res.size}", fontSize = 12.sp, color = TextGray) }
                    items(res, key = { it.id }) { app -> AppCard(app) { onApp(app.id) } }
                }
            }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            res = searchApps(q)
            isRefreshing = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppDetailScreen(id: String, onBack: () -> Unit, onShot: (Int) -> Unit) {
    var state by remember { mutableStateOf<State<App?>>(State.Loading) }
    var isRefreshing by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var installed by remember { mutableStateOf(false) }

    fun loadApp() {
        state = State.Loading
    }

    LaunchedEffect(id) {
        delay(400)
        state = State.Ok(appById(id))
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(800)
            state = State.Ok(appById(id))
            isRefreshing = false
        }
    }

    val app = (state as? State.Ok)?.data
    if (app == null && state is State.Ok) {
        ErrView("Не найдено") { onBack() }
        return
    }
    if (app == null) {
        Column(modifier = Modifier.fillMaxSize().background(BgGray)) {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RuBlue),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RuBlue)
            }
        }
        return
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().background(BgGray)) {
            TopAppBar(
                title = { Text(text = app.name, fontWeight = FontWeight.Medium, color = Color.White, maxLines = 1) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RuBlue),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Share, contentDescription = null, tint = Color.White)
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = app.iconRes),
                                    contentDescription = app.name,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = app.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Text(text = app.dev, color = RuBlue)
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = CatColors[app.cat]?.copy(alpha = 0.1f) ?: RuBlue.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = app.cat.title,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                                fontSize = 12.sp,
                                                color = CatColors[app.cat] ?: RuBlue
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = DivColor
                                        ) {
                                            Text(
                                                text = app.age.label,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                color = TextGray
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Stat(Icons.Outlined.Star, "Рейтинг", "%.1f".format(app.rate))
                                Stat(Icons.Outlined.Info, "Загрузок", app.dls)
                                Stat(Icons.Outlined.Menu, "Размер", app.size)
                            }
                        }
                    }
                }

                if (app.shots.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Скриншоты",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(app.shots) { index, shot ->
                                    Box(
                                        modifier = Modifier
                                            .width(180.dp)
                                            .height(320.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFE0E0E0))
                                            .clickable { onShot(index) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Outlined.Info,
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = TextLight
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = shot.desc, fontSize = 12.sp, color = TextGray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { installed = !installed },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (installed) Color(0xFF4CAF50) else RuBlue
                        )
                    ) {
                        Icon(
                            imageVector = if (installed) Icons.Filled.Check else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (installed) "Открыть" else "Установить",
                            fontSize = 16.sp
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Описание", fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = app.full,
                                fontSize = 14.sp,
                                color = TextGray,
                                maxLines = if (expanded) Int.MAX_VALUE else 5,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (app.full.length > 100) {
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(onClick = { expanded = !expanded }) {
                                    Text(
                                        text = if (expanded) "Свернуть" else "Далее",
                                        color = RuBlue
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Информация", fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow("Разработчик", app.dev)
                            InfoRow("Категория", app.cat.title)
                            InfoRow("Рейтинг", app.age.label)
                            InfoRow("Загрузок", app.dls)
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun Stat(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = TextGray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 10.sp, color = TextGray)
    }
}

@Composable
fun Info(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = RuBlue)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontWeight = FontWeight.Medium)
        Text(text = label, fontSize = 10.sp, color = TextGray)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = TextGray)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenshotScreen(id: String, idx: Int, onBack: () -> Unit) {
    val app = remember { appById(id) }
    if (app == null || app.shots.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Нет скриншотов", color = Color.White)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = RuBlue)) {
                    Text("Назад")
                }
            }
        }
        return
    }

    val pagerState = rememberPagerState(initialPage = idx.coerceIn(0, app.shots.lastIndex)) { app.shots.size }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.7f),
                        1f to Color.Transparent
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = app.name, color = Color.White, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    Text(
                        text = "${currentPage + 1} из ${app.shots.size}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.Face,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = TextLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = app.shots[page].desc.ifEmpty { "Скриншот" },
                            color = TextGray
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            if (app.shots[currentPage].desc.isNotEmpty()) {
                Text(
                    text = app.shots[currentPage].desc,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            if (app.shots.size > 1) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(app.shots.size) { index ->
                        val color by animateColorAsState(
                            if (index == currentPage) Color.White else Color.White.copy(alpha = 0.3f)
                        )
                        Box(
                            modifier = Modifier
                                .size(if (index == currentPage) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrView(msg: String, onRetry: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFFF44336)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ошибка", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(msg, fontSize = 14.sp, color = TextGray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = RuBlue)) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Повторить")
        }
    }
}

@Composable
fun EmptyView(title: String, subtitle: String, icon: ImageVector = Icons.Outlined.Search) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = TextLight)
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(subtitle, fontSize = 14.sp, color = TextGray)
    }
}