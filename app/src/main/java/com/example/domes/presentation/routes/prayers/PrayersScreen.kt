package com.example.domes.presentation.routes.prayers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.ExpandCircleDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.example.domes.presentation.common.composables.shape.Polygon
import com.example.domes.presentation.common.composables.shape.Shape
import com.example.domes.presentation.common.composables.slider.Slider
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.composables.MainActionSection
import com.example.domes.presentation.routes.prayers.composables.PercentageWidthBox
import com.example.domes.presentation.routes.prayers.composables.PercentageWidthBoxChild
import com.example.domes.presentation.routes.prayers.composables.PrayerCard
import com.example.domes.presentation.routes.prayers.composables.SelectedDateSection
import com.example.domes.presentation.routes.prayers.composables.SnoozePrayerDialog
import com.example.domes.presentation.routes.settings.routes.location.LocationEvent
import com.example.domes.presentation.routes.settings.routes.notifications.NotificationsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.fade
import com.google.accompanist.placeholder.material3.placeholder
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class UIConstants {
    companion object {
        fun getFormatter(pattern: String): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(pattern)
        }

        fun getArabicFormatter(pattern: String): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(pattern).withLocale(Locale("ar"))
        }

        val SHEET_PEEK_HEIGHT = 100.dp
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun PrayersScreen(
    navController: NavController,
    viewModel: PrayersViewModel,
    notificationsViewModel: NotificationsViewModel,
) {
    val prayersState = viewModel.prayersState.value
    val locationState = viewModel.locationState.value
    val notificationsState = notificationsViewModel.state.value

    val prayerTimes =
        if (prayersState.selectedDate == LocalDate.now()) prayersState.currentPrayerTimes else prayersState.selectedPrayerTimes

    val localDensity = LocalDensity.current
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val notificationPermissionState =
        rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
    val pullRefreshState =
        rememberPullRefreshState(refreshing = prayersState.isRefreshing, onRefresh = {
            viewModel.onEvent(
                PrayersEvent.RefreshLocation
            )
        })
    val scrollBehaviourState =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState(initialPage = 1) { 3 }
    val prayerTimesSectionHeight = remember { mutableStateOf(0.dp) }

    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.settledPage == 0) {
            viewModel.onEvent(PrayersEvent.ChangeDate(prayersState.selectedDate.minusDays(1)))
        } else if (pagerState.settledPage == 2) {
            viewModel.onEvent(PrayersEvent.ChangeDate(prayersState.selectedDate.plusDays(1)))
        }

        scope.launch {
            pagerState.animateScrollToPage(1)
        }
    }

    val isNotificationsAllowed =
        notificationPermissionState.status.isGranted && notificationsState.isNotificationsAllowed

    // Animations

    val transition = updateTransition(
        targetState = bottomSheetScaffoldState.bottomSheetState.targetValue,
        label = "BottomSheetTransition"
    )

    val blurRadius by transition.animateFloat(label = "BackgroundBlurRadius") { state ->
        if (state == SheetValue.Expanded) 20f else 1f
    }
    val alpha by transition.animateFloat(label = "BackgroundAlpha") { state ->
        if (state == SheetValue.Expanded) 0.5f else 1f
    }
    val scale by transition.animateFloat(
        label = "BackgroundScale",
        transitionSpec = { tween(1000, easing = EaseOutQuint) }
    ) { state ->
        if (state == SheetValue.Expanded) 0.95f else 1f
    }
    val dayFraction = animateFloatAsState(
        targetValue = prayersState.dayFraction
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
        sheetPeekHeight = UIConstants.SHEET_PEEK_HEIGHT,
        sheetShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
        sheetDragHandle = {},
        sheetContent = {
            SelectedDateSection(
                gregorianDate = prayersState.selectedDate,
                hijriDate = prayersState.selectedHijriDate,
                scope = scope,
                bottomSheetExpandAction = { scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } },
                collapseBottomSheet = { scope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() } },
                isLoading = prayersState.isLoading,
            ) { selectedDate ->
                viewModel.onEvent(PrayersEvent.ChangeDate(selectedDate))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center
        ) {
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehaviourState.nestedScrollConnection)
                    .graphicsLayer {
                        this.alpha = alpha
                        this.scaleX = scale
                        this.scaleY = scale
                        this.renderEffect = BlurEffect(blurRadius, blurRadius)
                    },
                topBar = {
                    val (foregroundText, backgroundText) = if (prayersState.selectedDate == LocalDate.now()) {
                        (prayersState.currentPrayer?.prayer?.name
                            ?: "Domes") to (prayersState.currentPrayer?.prayer?.arabicName ?: "")
                    } else {
                        prayersState.selectedDate.format(UIConstants.getFormatter("d MMMM")) to
                                prayersState.selectedHijriDate.format(
                                    UIConstants.getArabicFormatter(
                                        "MMM d"
                                    )
                                )
                    }

                    LayeredTopAppBar(
                        foregroundText,
                        backgroundText,
                        topActions = {
                            IconButton(
                                onClick = { viewModel.onEvent(PrayersEvent.SetOverarchingDropdownVisibility()) }
                            ) {
                                Icon(
                                    Icons.Outlined.ExpandCircleDown,
                                    contentDescription = "More options"
                                )
                            }

                            DropdownMenu(
                                expanded = prayersState.showOverarchingDropdown,
                                onDismissRequest = {
                                    viewModel.onEvent(
                                        PrayersEvent.SetOverarchingDropdownVisibility(
                                            false
                                        )
                                    )
                                }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        navController.navigate(Screen.Settings.route)
                                        viewModel.onEvent(
                                            PrayersEvent.SetOverarchingDropdownVisibility(
                                                false
                                            )
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Settings,
                                            contentDescription = "Settings"
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Help") },
                                    onClick = { /* Handle edit! */ },
                                    leadingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Outlined.HelpOutline,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        },
                        bottomActions = MainActionSection(
                            prayersState.timeLeft,
                            viewModel.calculateRemainingPrayerTime(),
                            notificationPermissionState.status.isGranted,
                            isNotificationsAllowed,
                            locationState.locale,
                            navController = navController,
                        ) {
                            viewModel.onEvent(PrayersEvent.SetSnoozeDialogVisibility(true))
                        },
                        scrollBehavior = scrollBehaviourState
                    )
                },
                floatingActionButton = {
                    // TODO: Debug
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(
                                if (locationState.location?.latitude != 38.71667) {
                                    LocationEvent.ChangeCoordinates(
                                        38.71667,
                                        -9.13333
                                    )
                                } else {
                                    LocationEvent.ChangeCoordinates(
                                        55.3617609, -3.4433238
                                    )
                                }
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                        )
                    ) {
                        Icon(Icons.Filled.Explore, "Find Qibla")
                    }

//                    FloatingActionButton(
//                        onClick = {
//                            navController.navigate(Screen.Qibla.route)
//                        },
//                        containerColor = MaterialTheme.colorScheme.tertiary,
//                        contentColor = MaterialTheme.colorScheme.onTertiary,
//                        elevation = FloatingActionButtonDefaults.elevation(
//                            defaultElevation = 0.dp,
//                            pressedElevation = 0.dp,
//                        )
//                    ) {
//                        Icon(Icons.Filled.Explore, "Find Qibla")
//                    }

//                    ExpandableFloatingActionButton(
//                        icon = IconSource(Icons.Filled.Explore)
//                    ) {
//                        QiblaScreen(navController = navController)
//                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        item {
                            val shapeParam = Shape(
                                roundness = .4f,
                                sides = 4,
                                rotation = 90f,
                                shapeId = Shape.ShapeId.Polygon
                            )
                            val shape = shapeParam.genShape().normalized()

                            FixedCard {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 16.dp,
                                            start = 24.dp,
                                            end = 24.dp,
                                            bottom = 16.dp
                                        )
                                        .placeholder(
                                            visible = prayersState.isLoading,
                                            highlight = PlaceholderHighlight.fade(),
                                            shape = RoundedCornerShape(100),
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Slider(
                                        dayFraction.value,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )

                                    PercentageWidthBox(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(20.dp),
                                        children = prayerTimes.filter { it.prayer.displayable }
                                            .map { prayerTime ->
                                                PercentageWidthBoxChild(
                                                    composable = {
                                                        Box(modifier = Modifier.size(10.dp)) {
                                                            Polygon(
                                                                shape,
                                                                color = if (prayerTime == prayersState.currentPrayer) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSecondary,
                                                                alpha = if (prayerTime == prayersState.currentPrayer) 1f else 0.75f
                                                            )
                                                        }
                                                    },
                                                    widthFraction = prayerTime.dayFraction?.toFloat()
                                                )
                                            }
                                    )
                                }
                            }
                        }
                        item {
                            HorizontalPager(
                                pagerState,
                                pageSpacing = 16.dp,
                            ) { index ->
                                if (index == 1) {
                                    Column(
                                        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                                            prayerTimesSectionHeight.value = with(localDensity) {
                                                layoutCoordinates.size.height.toDp()
                                            }
                                        }
                                    ) {
                                        val expandedPrayerIndex =
                                            prayerTimes.indexOf(prayersState.expandedPrayer)

                                        Prayer.ALL.forEachIndexed { index, prayer ->
                                            val prayerTime =
                                                prayerTimes.find { it.prayer == prayer }

                                            PrayerCard(
                                                prayer = prayer,
                                                prayerTime = prayerTime,
                                                prayerRecordDetail = prayersState.selectedPrayerRecord.details.find { it.prayer == prayer },
                                                isExpanded = prayerTime != null && prayersState.expandedPrayer === prayerTime,
                                                isCurrent = prayerTime != null && prayerTime == prayersState.currentPrayer,
                                                hasPassed = prayerTime?.timeWindow?.isBefore(
                                                    LocalDateTime.now()
                                                ) == true && prayerTime != prayersState.currentPrayer,
                                                isSilent = notificationsState.prayerNotifications.find { it.prayer == prayer }?.isNotificationAllowed === false,
                                                isNotificationsAllowed = isNotificationsAllowed,
                                                isFirstItem = index == 0,
                                                isLastItem = index == Prayer.ALL.lastIndex,
                                                isBeforeExpandedItem = index == expandedPrayerIndex - 1,
                                                isAfterExpandedItem = index == expandedPrayerIndex + 1,
                                                remainingTimePercentage = viewModel.calculateRemainingPrayerTime(),
                                                navController = navController,
                                                expand = {
                                                    viewModel.onEvent(
                                                        PrayersEvent.ChangeExpandedPrayer(
                                                            if (it) null else prayerTime
                                                        )
                                                    )
                                                },
                                                onPrayerRecordDetailValueChange = { prayed, notes, missedReason ->
                                                    viewModel.onEvent(
                                                        PrayersEvent.ChangePrayerRecord(
                                                            prayer,
                                                            prayed, notes, missedReason
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                } else if (index == 0) {
                                    FixedCard(
                                        modifier = Modifier
                                            .height(prayerTimesSectionHeight.value),
                                        variant = Variant.FADED
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                prayersState.selectedDate.minusDays(1).format(
                                                    UIConstants.getFormatter("d MMMM")
                                                ),
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
                                    }
                                } else {
                                    FixedCard(
                                        modifier = Modifier
                                            .height(prayerTimesSectionHeight.value),
                                        variant = Variant.FADED
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                prayersState.selectedDate.plusDays(1).format(
                                                    UIConstants.getFormatter("d MMMM")
                                                ),
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (bottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures {
                                scope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                                }
                            }
                        }
                )
            }

            PullRefreshIndicator(
                prayersState.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )

            if (prayersState.showSnoozeDialog) {
                prayersState.currentPrayer?.prayer?.let {
                    SnoozePrayerDialog(
                        prayer = it,
                        setDialogVisibility = { viewModel.onEvent(PrayersEvent.SetSnoozeDialogVisibility(it)) },
                        onAffirmative = { /*TODO*/ }
                    )
                }
            }
        }
    }
}