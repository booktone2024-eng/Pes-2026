package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.TournamentViewModel
import com.example.data.TournamentStateEntity

// Color Constants reflecting Kurdistan Flag & Dark Gaming Vibe
val ThemeBgDark = Color(0xFF0A0A0A)
val ThemeCardDark = Color(0xFF161616)
val ThemeBorderDark = Color(0xFF1E293B)

val KurdRed = Color(0xFFEE2D24)
val KurdGreen = Color(0xFF00AB4E)
val KurdYellow = Color(0xFFFDB913)
val KurdWhite = Color(0xFFFFFFFF)

val GoldColor = Color(0xFFFFD700)
val SilverColor = Color(0xFFC0C0C0)
val BronzeColor = Color(0xFFCD7F32)

@Composable
fun KurdistanSun(
    modifier: Modifier = Modifier,
    sunColor: Color = KurdYellow,
    autoRotate: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sun_spin")
    val angle by if (autoRotate) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(45000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "spin"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2
        val sizeMin = minOf(width, height)
        val outerRadius = sizeMin * 0.45f
        val innerRadius = sizeMin * 0.18f
        
        rotate(angle, pivot = Offset(centerX, centerY)) {
            // Draw central disk
            drawCircle(
                color = sunColor,
                radius = innerRadius,
                center = Offset(centerX, centerY)
            )
            
            // Draw exactly 21 pointed rays of Kurdistan sun
            val numRays = 21
            val angleStep = 360f / numRays
            val halfBaseAngle = angleStep * 0.28f
            
            for (i in 0 until numRays) {
                val centerAngleDeg = i * angleStep - 90f
                val centerAngleRad = Math.toRadians(centerAngleDeg.toDouble())
                
                val leftAngleRad = Math.toRadians((centerAngleDeg - halfBaseAngle).toDouble())
                val rightAngleRad = Math.toRadians((centerAngleDeg + halfBaseAngle).toDouble())
                
                val tipX = (centerX + outerRadius * Math.cos(centerAngleRad)).toFloat()
                val tipY = (centerY + outerRadius * Math.sin(centerAngleRad)).toFloat()
                
                val baseLeftX = (centerX + innerRadius * Math.cos(leftAngleRad)).toFloat()
                val baseLeftY = (centerY + innerRadius * Math.sin(leftAngleRad)).toFloat()
                
                val baseRightX = (centerX + innerRadius * Math.cos(rightAngleRad)).toFloat()
                val baseRightY = (centerY + innerRadius * Math.sin(rightAngleRad)).toFloat()
                
                val path = Path().apply {
                    moveTo(baseLeftX, baseLeftY)
                    lineTo(tipX, tipY)
                    lineTo(baseRightX, baseRightY)
                    close()
                }
                
                drawPath(
                    path = path,
                    color = sunColor
                )
            }
        }
    }
}

@Composable
fun KurdistanFlagHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .shadow(12.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        // Red (Top), White (Middle), Green (Bottom) horizontal stripe background
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth().background(KurdRed))
            Box(modifier = Modifier.weight(0.9f).fillMaxWidth().background(KurdWhite))
            Box(modifier = Modifier.weight(1f).fillMaxWidth().background(KurdGreen))
        }
        
        // Sun centerpiece positioned beautifully over the white stripe
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                    .border(1.5.dp, KurdYellow.copy(alpha = 0.6f), CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                KurdistanSun(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun TournamentAppContent(viewModel: TournamentViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeBgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            KurdistanFlagHeader()
            
            Text(
                text = "PES 2026 KURDISTAN CUP",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = KurdYellow,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 2.dp)
            )
            
            Text(
                text = "جام قهرمانی کردستان - PES 2026",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = KurdWhite.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                AnimatedContent(
                    targetState = state.phase,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                    },
                    label = "phase_transition"
                ) { phase ->
                    when (phase) {
                        "SETUP" -> SetupScreen(state = state, viewModel = viewModel)
                        "SEMIFINALS" -> SemifinalsScreen(state = state, viewModel = viewModel)
                        "FINALS" -> FinalsScreen(state = state, viewModel = viewModel)
                        "PODIUM" -> PodiumScreen(state = state, viewModel = viewModel)
                        else -> SetupScreen(state = state, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SetupScreen(state: TournamentStateEntity, viewModel: TournamentViewModel) {
    val focusManager = LocalFocusManager.current
    
    // Manage input states inside Composable, seeded initially from persistent Room state
    var p1Name by remember(state.p1Name) { mutableStateOf(state.p1Name) }
    var p1Team by remember(state.p1Team) { mutableStateOf(state.p1Team) }
    
    var p2Name by remember(state.p2Name) { mutableStateOf(state.p2Name) }
    var p2Team by remember(state.p2Team) { mutableStateOf(state.p2Team) }
    
    var p3Name by remember(state.p3Name) { mutableStateOf(state.p3Name) }
    var p3Team by remember(state.p3Team) { mutableStateOf(state.p3Team) }
    
    var p4Name by remember(state.p4Name) { mutableStateOf(state.p4Name) }
    var p4Team by remember(state.p4Team) { mutableStateOf(state.p4Team) }

    // Synchronize inputs to database when they lose focus or change
    fun syncInput() {
        viewModel.updatePlayerInfo(
            p1Name, p1Team, p2Name, p2Team, p3Name, p3Team, p4Name, p4Team
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ThemeCardDark),
            border = BorderStroke(1.dp, ThemeBorderDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "ورود اطلاعات بازیکنان",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = KurdWhite,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
                
                // Player Inputs (1-4)
                PlayerInputRow(
                    label = "بازیکن اول (قرمز)",
                    nameValue = p1Name,
                    onNameChange = { p1Name = it; syncInput() },
                    teamValue = p1Team,
                    onTeamChange = { p1Team = it; syncInput() },
                    accentColor = KurdRed
                )
                
                PlayerInputRow(
                    label = "بازیکن دوم (سفید)",
                    nameValue = p2Name,
                    onNameChange = { p2Name = it; syncInput() },
                    teamValue = p2Team,
                    onTeamChange = { p2Team = it; syncInput() },
                    accentColor = KurdWhite
                )
                
                PlayerInputRow(
                    label = "بازیکن سوم (سبز)",
                    nameValue = p3Name,
                    onNameChange = { p3Name = it; syncInput() },
                    teamValue = p3Team,
                    onTeamChange = { p3Team = it; syncInput() },
                    accentColor = KurdGreen
                )
                
                PlayerInputRow(
                    label = "بازیکن چهارم (طلایی)",
                    nameValue = p4Name,
                    onNameChange = { p4Name = it; syncInput() },
                    teamValue = p4Team,
                    onTeamChange = { p4Team = it; syncInput() },
                    accentColor = KurdYellow
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val canStart = p1Name.isNotBlank() && p2Name.isNotBlank() && p3Name.isNotBlank() && p4Name.isNotBlank() &&
                       p1Team.isNotBlank() && p2Team.isNotBlank() && p3Team.isNotBlank() && p4Team.isNotBlank()

        Button(
            onClick = {
                if (canStart) {
                    focusManager.clearFocus()
                    syncInput()
                    viewModel.performDrawAndStart()
                }
            },
            enabled = canStart,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KurdYellow,
                disabledContainerColor = KurdYellow.copy(alpha = 0.3f),
                contentColor = Color.Black,
                disabledContentColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "انجام قرعه‌کشی و شروع تورنمنت",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "جهت شروع، نام و اسم تیم هر ۴ نفر را پر کنید",
            fontSize = 12.sp,
            color = KurdWhite.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerInputRow(
    label: String,
    nameValue: String,
    onNameChange: (String) -> Unit,
    teamValue: String,
    onTeamChange: (String) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, ThemeBorderDark, RoundedCornerShape(12.dp))
            .background(ThemeBgDark.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(accentColor, CircleShape)
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = KurdWhite.copy(alpha = 0.9f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = nameValue,
                onValueChange = onNameChange,
                placeholder = { Text("نام بازیکن", fontSize = 12.sp, color = KurdWhite.copy(0.4f)) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ThemeCardDark,
                    unfocusedContainerColor = ThemeCardDark,
                    focusedTextColor = KurdWhite,
                    unfocusedTextColor = KurdWhite,
                    cursorColor = KurdYellow,
                    focusedIndicatorColor = accentColor,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            
            TextField(
                value = teamValue,
                onValueChange = onTeamChange,
                placeholder = { Text("اسم تیم (مثلا رئال مادرید)", fontSize = 11.sp, color = KurdWhite.copy(0.4f)) },
                modifier = Modifier.weight(1.2f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ThemeCardDark,
                    unfocusedContainerColor = ThemeCardDark,
                    focusedTextColor = KurdWhite,
                    unfocusedTextColor = KurdWhite,
                    cursorColor = KurdYellow,
                    focusedIndicatorColor = accentColor,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}

@Composable
fun SemifinalsScreen(state: TournamentStateEntity, viewModel: TournamentViewModel) {
    // Look up players based on ID
    fun getPlayerName(id: Int) = when (id) {
        1 -> state.p1Name; 2 -> state.p2Name; 3 -> state.p3Name; 4 -> state.p4Name; else -> ""
    }
    fun getPlayerTeam(id: Int) = when (id) {
        1 -> state.p1Team; 2 -> state.p2Team; 3 -> state.p3Team; 4 -> state.p4Team; else -> ""
    }

    // Semi 1: sf1HomeId vs sf1AwayId
    // Semi 2: sf2HomeId vs sf2AwayId
    val sf1Home = state.sf1HomeId
    val sf1Away = state.sf1AwayId
    val sf2Home = state.sf2HomeId
    val sf2Away = state.sf2AwayId

    // Aggregates
    val sf1Leg1Home = state.sf1L1HomeGoals
    val sf1Leg1Away = state.sf1L1AwayGoals
    val sf1Leg2Home = state.sf1L2HomeGoals // Note: in Leg 2, the host is sf1AwayId, so sf1L2Home corresponds to sf1AwayId's goals!
    val sf1Leg2Away = state.sf1L2AwayGoals // in Leg 2, guest is sf1HomeId, so sf1L2Away corresponds to sf1HomeId's goals!

    val sf1TotalHome = sf1Leg1Home + sf1Leg2Away // sf1HomeId total goals
    val sf1TotalAway = sf1Leg1Away + sf1Leg2Home // sf1AwayId total goals

    val sf2Leg1Home = state.sf2L1HomeGoals
    val sf2Leg1Away = state.sf2L1AwayGoals
    val sf2Leg2Home = state.sf2L2HomeGoals // host is sf2AwayId
    val sf2Leg2Away = state.sf2L2AwayGoals // guest is sf2HomeId

    val sf2TotalHome = sf2Leg1Home + sf2Leg2Away // sf2HomeId total goals
    val sf2TotalAway = sf2Leg1Away + sf2Leg2Home // sf2AwayId total goals

    // Tiebreakers
    var sf1SelectedWinnerId by remember { mutableStateOf<Int?>(null) }
    var sf2SelectedWinnerId by remember { mutableStateOf<Int?>(null) }

    // Derive actual winners/losers
    val sf1WinnerId = when {
        sf1TotalHome > sf1TotalAway -> sf1Home
        sf1TotalAway > sf1TotalHome -> sf1Away
        else -> sf1SelectedWinnerId ?: 0
    }
    val sf1LoserId = if (sf1WinnerId == sf1Home) sf1Away else if (sf1WinnerId == sf1Away) sf1Home else 0

    val sf2WinnerId = when {
        sf2TotalHome > sf2TotalAway -> sf2Home
        sf2TotalAway > sf2TotalHome -> sf2Away
        else -> sf2SelectedWinnerId ?: 0
    }
    val sf2LoserId = if (sf2WinnerId == sf2Home) sf2Away else if (sf2WinnerId == sf2Away) sf2Home else 0

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "مرحله نیمه‌نهایی (رفت و برگشت)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = KurdWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        // Semi 1 Box
        SemifinalMatchCard(
            matchTitle = "نیمه‌نهایی اول",
            homeName = getPlayerName(sf1Home),
            homeTeam = getPlayerTeam(sf1Home),
            awayName = getPlayerName(sf1Away),
            awayTeam = getPlayerTeam(sf1Away),
            
            l1HomeGoals = state.sf1L1HomeGoals,
            l1AwayGoals = state.sf1L1AwayGoals,
            l1Completed = state.sf1L1Completed,
            onL1HomeChange = { goals -> viewModel.updateSF1Leg1(goals, state.sf1L1AwayGoals, true) },
            onL1AwayChange = { goals -> viewModel.updateSF1Leg1(state.sf1L1HomeGoals, goals, true) },
            
            l2HomeGoals = state.sf1L2HomeGoals, // Host is Away player
            l2AwayGoals = state.sf1L2AwayGoals, // Guest is Home player
            l2Completed = state.sf1L2Completed,
            onL2HomeChange = { goals -> viewModel.updateSF1Leg2(goals, state.sf1L2AwayGoals, true) },
            onL2AwayChange = { goals -> viewModel.updateSF1Leg2(state.sf1L2HomeGoals, goals, true) },
            
            totalHome = sf1TotalHome,
            totalAway = sf1TotalAway,
            isTie = sf1TotalHome == sf1TotalAway && state.sf1L1Completed && state.sf1L2Completed,
            selectedWinnerId = sf1SelectedWinnerId,
            homeId = sf1Home,
            awayId = sf1Away,
            onSelectWinner = { sf1SelectedWinnerId = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Semi 2 Box
        SemifinalMatchCard(
            matchTitle = "نیمه‌نهایی دوم",
            homeName = getPlayerName(sf2Home),
            homeTeam = getPlayerTeam(sf2Home),
            awayName = getPlayerName(sf2Away),
            awayTeam = getPlayerTeam(sf2Away),
            
            l1HomeGoals = state.sf2L1HomeGoals,
            l1AwayGoals = state.sf2L1AwayGoals,
            l1Completed = state.sf2L1Completed,
            onL1HomeChange = { goals -> viewModel.updateSF2Leg1(goals, state.sf2L1AwayGoals, true) },
            onL1AwayChange = { goals -> viewModel.updateSF2Leg1(state.sf2L1HomeGoals, goals, true) },
            
            l2HomeGoals = state.sf2L2HomeGoals, // Host is Away player
            l2AwayGoals = state.sf2L2AwayGoals, // Guest is Home player
            l2Completed = state.sf2L2Completed,
            onL2HomeChange = { goals -> viewModel.updateSF2Leg2(goals, state.sf2L2AwayGoals, true) },
            onL2AwayChange = { goals -> viewModel.updateSF2Leg2(state.sf2L2HomeGoals, goals, true) },
            
            totalHome = sf2TotalHome,
            totalAway = sf2TotalAway,
            isTie = sf2TotalHome == sf2TotalAway && state.sf2L1Completed && state.sf2L2Completed,
            selectedWinnerId = sf2SelectedWinnerId,
            homeId = sf2Home,
            awayId = sf2Away,
            onSelectWinner = { sf2SelectedWinnerId = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        val matchesCompleted = state.sf1L1Completed && state.sf1L2Completed &&
                               state.sf2L1Completed && state.sf2L2Completed
        val tiebreakersResolved = (sf1TotalHome != sf1TotalAway || sf1SelectedWinnerId != null) &&
                                  (sf2TotalHome != sf2TotalAway || sf2SelectedWinnerId != null)

        val canAdvance = matchesCompleted && tiebreakersResolved

        Button(
            onClick = {
                if (canAdvance) {
                    viewModel.advanceToFinals(sf1WinnerId, sf1LoserId, sf2WinnerId, sf2LoserId)
                }
            },
            enabled = canAdvance,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KurdGreen,
                disabledContainerColor = KurdGreen.copy(alpha = 0.3f),
                contentColor = KurdWhite,
                disabledContentColor = KurdWhite.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ثبت نتایج و صعود به فینال و رده‌بندی",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.resetTournament() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = KurdRed)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("انصراف و شروع مجدد قرعه کشی", fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SemifinalMatchCard(
    matchTitle: String,
    homeName: String,
    homeTeam: String,
    awayName: String,
    awayTeam: String,
    
    l1HomeGoals: Int,
    l1AwayGoals: Int,
    l1Completed: Boolean,
    onL1HomeChange: (Int) -> Unit,
    onL1AwayChange: (Int) -> Unit,
    
    l2HomeGoals: Int, // Leg 2 home is away player
    l2AwayGoals: Int, // Leg 2 away is home player
    l2Completed: Boolean,
    onL2HomeChange: (Int) -> Unit,
    onL2AwayChange: (Int) -> Unit,
    
    totalHome: Int,
    totalAway: Int,
    isTie: Boolean,
    selectedWinnerId: Int?,
    homeId: Int,
    awayId: Int,
    onSelectWinner: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = ThemeCardDark),
        border = BorderStroke(1.dp, ThemeBorderDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(KurdYellow.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = matchTitle, color = KurdYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                
                Text(
                    text = "رفت و برگشت",
                    fontSize = 11.sp,
                    color = KurdWhite.copy(alpha = 0.5f)
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))

            // Leg 1 (رفت): Home vs Away
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThemeBgDark.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "بازی رفت (میزبان: $homeName)",
                    fontSize = 11.sp,
                    color = KurdWhite.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Home
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(homeName, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 13.sp)
                        Text(homeTeam, color = KurdWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                    }

                    // Score adjustment
                    ScoreStepper(score = l1HomeGoals, onScoreChange = onL1HomeChange)
                    
                    Text(":", color = KurdWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    
                    ScoreStepper(score = l1AwayGoals, onScoreChange = onL1AwayChange)

                    // Away
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(awayName, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 13.sp)
                        Text(awayTeam, color = KurdWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Leg 2 (برگشت): Away vs Home (meaning Away is Home in Leg 2)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThemeBgDark.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "بازی برگشت (میزبان: $awayName)",
                    fontSize = 11.sp,
                    color = KurdWhite.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Home in Leg 2 (Away Player)
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(awayName, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 13.sp)
                        Text(awayTeam, color = KurdWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                    }

                    // Score adjustment
                    ScoreStepper(score = l2HomeGoals, onScoreChange = onL2HomeChange)
                    
                    Text(":", color = KurdWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    
                    ScoreStepper(score = l2AwayGoals, onScoreChange = onL2AwayChange)

                    // Away in Leg 2 (Home Player)
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(homeName, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 13.sp)
                        Text(homeTeam, color = KurdWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Aggregate Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Away Total
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (l1Completed && l2Completed && totalAway > totalHome) {
                        Icon(Icons.Default.Check, contentDescription = "برنده", tint = KurdGreen, modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = "مجموع $awayName: $totalAway",
                        fontSize = 11.sp,
                        color = if (l1Completed && l2Completed && totalAway > totalHome) KurdGreen else KurdWhite.copy(alpha = 0.8f),
                        fontWeight = if (l1Completed && l2Completed && totalAway > totalHome) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = "نتیجه کل (مجموع)",
                    fontSize = 11.sp,
                    color = KurdYellow,
                    fontWeight = FontWeight.Bold
                )

                // Home Total
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "مجموع $homeName: $totalHome",
                        fontSize = 11.sp,
                        color = if (l1Completed && l2Completed && totalHome > totalAway) KurdGreen else KurdWhite.copy(alpha = 0.8f),
                        fontWeight = if (l1Completed && l2Completed && totalHome > totalAway) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    if (l1Completed && l2Completed && totalHome > totalAway) {
                        Icon(Icons.Default.Check, contentDescription = "برنده", tint = KurdGreen, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Tiebreaker UI if aggregate is tied
            if (isTie) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, KurdRed.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .background(KurdRed.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "تساوی در مجموع! چه کسی صعود می‌کند؟ (ضربات پنالتی)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = KurdRed,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onSelectWinner(homeId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedWinnerId == homeId) KurdGreen else ThemeCardDark
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (selectedWinnerId == homeId) KurdGreen else ThemeBorderDark)
                        ) {
                            Text(text = homeName, fontSize = 11.sp, color = KurdWhite)
                        }

                        Button(
                            onClick = { onSelectWinner(awayId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedWinnerId == awayId) KurdGreen else ThemeCardDark
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (selectedWinnerId == awayId) KurdGreen else ThemeBorderDark)
                        ) {
                            Text(text = awayName, fontSize = 11.sp, color = KurdWhite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreStepper(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Minus
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(ThemeBorderDark, CircleShape)
                .clickable { if (score > 0) onScoreChange(score - 1) },
            contentAlignment = Alignment.Center
        ) {
            Text("-", color = KurdWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // Score display
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(34.dp)
                .background(ThemeBgDark, RoundedCornerShape(4.dp))
                .border(0.5.dp, ThemeBorderDark, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = score.toString(),
                color = KurdYellow,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Plus
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(ThemeBorderDark, CircleShape)
                .clickable { onScoreChange(score + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text("+", color = KurdWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FinalsScreen(state: TournamentStateEntity, viewModel: TournamentViewModel) {
    fun getPlayerName(id: Int) = when (id) {
        1 -> state.p1Name; 2 -> state.p2Name; 3 -> state.p3Name; 4 -> state.p4Name; else -> ""
    }
    fun getPlayerTeam(id: Int) = when (id) {
        1 -> state.p1Team; 2 -> state.p2Team; 3 -> state.p3Team; 4 -> state.p4Team; else -> ""
    }

    // Finalists are temporarily stored in championId (Finalist 1) and secondId (Finalist 2)
    // 3rd Place contenders are stored in thirdId (Contender 1) and fourthId (Contender 2)
    val f1 = state.championId
    val f2 = state.secondId
    val t1 = state.thirdId
    val t2 = state.fourthId

    // Final scores
    val fHomeGoals = state.finalHomeGoals
    val fAwayGoals = state.finalAwayGoals
    
    // 3rd scores
    val tHomeGoals = state.thirdHomeGoals
    val tAwayGoals = state.thirdAwayGoals

    // Single match tiebreaker selections
    var finalWinnerSelectedId by remember { mutableStateOf<Int?>(null) }
    var thirdWinnerSelectedId by remember { mutableStateOf<Int?>(null) }

    // Deduce placements
    val champId = when {
        fHomeGoals > fAwayGoals -> f1
        fAwayGoals > fHomeGoals -> f2
        else -> finalWinnerSelectedId ?: 0
    }
    val runnerUpId = if (champId == f1) f2 else if (champId == f2) f1 else 0

    val thirdPlaceWinnerId = when {
        tHomeGoals > tAwayGoals -> t1
        tAwayGoals > tHomeGoals -> t2
        else -> thirdWinnerSelectedId ?: 0
    }
    val fourthPlaceId = if (thirdPlaceWinnerId == t1) t2 else if (thirdPlaceWinnerId == t2) t1 else 0

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "فینال و رده‌بندی پایانی",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = KurdWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        // FINAL CARD
        SingleMatchCard(
            cardTitle = "🏆 فینال بزرگ (تعیین قهرمان)",
            homeName = getPlayerName(f1),
            homeTeam = getPlayerTeam(f1),
            awayName = getPlayerName(f2),
            awayTeam = getPlayerTeam(f2),
            homeGoals = fHomeGoals,
            awayGoals = fAwayGoals,
            onHomeGoalsChange = { goals -> viewModel.updateFinalMatch(goals, fAwayGoals, true) },
            onAwayGoalsChange = { goals -> viewModel.updateFinalMatch(fHomeGoals, goals, true) },
            isTie = fHomeGoals == fAwayGoals && state.finalCompleted,
            selectedWinnerId = finalWinnerSelectedId,
            homeId = f1,
            awayId = f2,
            onSelectWinner = { finalWinnerSelectedId = it },
            accentColor = KurdYellow
        )

        Spacer(modifier = Modifier.height(16.dp))

        // THIRD PLACE CARD
        SingleMatchCard(
            cardTitle = "🥉 مسابقه رده‌بندی (مقام سوم)",
            homeName = getPlayerName(t1),
            homeTeam = getPlayerTeam(t1),
            awayName = getPlayerName(t2),
            awayTeam = getPlayerTeam(t2),
            homeGoals = tHomeGoals,
            awayGoals = tAwayGoals,
            onHomeGoalsChange = { goals -> viewModel.updateThirdPlaceMatch(goals, tAwayGoals, true) },
            onAwayGoalsChange = { goals -> viewModel.updateThirdPlaceMatch(tHomeGoals, goals, true) },
            isTie = tHomeGoals == tAwayGoals && state.thirdCompleted,
            selectedWinnerId = thirdWinnerSelectedId,
            homeId = t1,
            awayId = t2,
            onSelectWinner = { thirdWinnerSelectedId = it },
            accentColor = KurdGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        val canComplete = state.finalCompleted && state.thirdCompleted &&
                          (fHomeGoals != fAwayGoals || finalWinnerSelectedId != null) &&
                          (tHomeGoals != tAwayGoals || thirdWinnerSelectedId != null)

        Button(
            onClick = {
                if (canComplete) {
                    viewModel.finishTournament(champId, runnerUpId, thirdPlaceWinnerId, fourthPlaceId)
                }
            },
            enabled = canComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KurdYellow,
                disabledContainerColor = KurdYellow.copy(alpha = 0.3f),
                contentColor = Color.Black,
                disabledContentColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ثبت نتایج نهایی و اهتزاز پرچم قهرمان 🏆",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.resetTournament() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = KurdRed)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("شروع مجدد تورنمنت از اول", fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SingleMatchCard(
    cardTitle: String,
    homeName: String,
    homeTeam: String,
    awayName: String,
    awayTeam: String,
    homeGoals: Int,
    awayGoals: Int,
    onHomeGoalsChange: (Int) -> Unit,
    onAwayGoalsChange: (Int) -> Unit,
    isTie: Boolean,
    selectedWinnerId: Int?,
    homeId: Int,
    awayId: Int,
    onSelectWinner: (Int) -> Unit,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = ThemeCardDark),
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cardTitle,
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Home
                Column(
                    modifier = Modifier.weight(1.2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(homeName, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 14.sp)
                    Text(homeTeam, color = KurdWhite.copy(alpha = 0.5f), fontSize = 11.sp)
                }

                // Score adjust
                ScoreStepper(score = homeGoals, onScoreChange = onHomeGoalsChange)
                
                Text(":", color = KurdWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 6.dp))
                
                ScoreStepper(score = awayGoals, onScoreChange = onAwayGoalsChange)

                // Away
                Column(
                    modifier = Modifier.weight(1.2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(awayName, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 14.sp)
                    Text(awayTeam, color = KurdWhite.copy(alpha = 0.5f), fontSize = 11.sp)
                }
            }

            if (isTie) {
                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, KurdRed.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .background(KurdRed.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "بازی مساوی شد! برنده در وقت اضافه یا ضربات پنالتی کیست؟",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = KurdRed
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onSelectWinner(homeId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedWinnerId == homeId) KurdGreen else ThemeCardDark
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (selectedWinnerId == homeId) KurdGreen else ThemeBorderDark)
                        ) {
                            Text(text = homeName, fontSize = 11.sp, color = KurdWhite)
                        }

                        Button(
                            onClick = { onSelectWinner(awayId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedWinnerId == awayId) KurdGreen else ThemeCardDark
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (selectedWinnerId == awayId) KurdGreen else ThemeBorderDark)
                        ) {
                            Text(text = awayName, fontSize = 11.sp, color = KurdWhite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumScreen(state: TournamentStateEntity, viewModel: TournamentViewModel) {
    fun getPlayerName(id: Int) = when (id) {
        1 -> state.p1Name; 2 -> state.p2Name; 3 -> state.p3Name; 4 -> state.p4Name; else -> ""
    }
    fun getPlayerTeam(id: Int) = when (id) {
        1 -> state.p1Team; 2 -> state.p2Team; 3 -> state.p3Team; 4 -> state.p4Team; else -> ""
    }

    val champ = state.championId
    val second = state.secondId
    val third = state.thirdId
    val fourth = state.fourthId

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            KurdistanSun(modifier = Modifier.fillMaxSize(), autoRotate = true)
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = "🏆 نتایج پایانی و رده‌بندی 🏆",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = KurdYellow,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Celebration banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = KurdYellow.copy(alpha = 0.1f)),
            border = BorderStroke(1.5.dp, KurdYellow),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👑 قهرمان بزرگ جام 👑",
                    fontSize = 13.sp,
                    color = KurdYellow,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getPlayerName(champ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = KurdWhite
                )
                Text(
                    text = "با تیم: ${getPlayerTeam(champ)}",
                    fontSize = 14.sp,
                    color = KurdWhite.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Standings List
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ThemeCardDark),
            border = BorderStroke(1.dp, ThemeBorderDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = "رتبه‌بندی نهایی",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KurdWhite,
                    modifier = Modifier.padding(bottom = 12.dp),
                    textAlign = TextAlign.Right
                )

                StandingsRow(rank = 1, name = getPlayerName(champ), team = getPlayerTeam(champ), medalColor = GoldColor)
                Divider(color = ThemeBorderDark, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                StandingsRow(rank = 2, name = getPlayerName(second), team = getPlayerTeam(second), medalColor = SilverColor)
                Divider(color = ThemeBorderDark, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                StandingsRow(rank = 3, name = getPlayerName(third), team = getPlayerTeam(third), medalColor = BronzeColor)
                Divider(color = ThemeBorderDark, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                StandingsRow(rank = 4, name = getPlayerName(fourth), team = getPlayerTeam(fourth), medalColor = KurdWhite.copy(alpha = 0.3f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.resetTournament() },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KurdRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("شروع تورنمنت جدید 🔄", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = KurdWhite)
            }
        }
        
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun StandingsRow(rank: Int, name: String, team: String, medalColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(medalColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = if (medalColor == KurdWhite.copy(alpha = 0.3f)) KurdWhite else Color.Black,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, color = KurdWhite, fontSize = 14.sp)
                Text(text = team, color = KurdWhite.copy(alpha = 0.5f), fontSize = 11.sp)
            }
        }

        Text(
            text = when (rank) {
                1 -> "🥇 قهرمان"
                2 -> "🥈 مقام دوم"
                3 -> "🥉 مقام سوم"
                else -> "مقام چهارم"
            },
            color = medalColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
