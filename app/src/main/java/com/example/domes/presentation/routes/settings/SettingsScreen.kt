package com.example.domes.presentation.routes.settings

import android.os.Build
import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domes.presentation.common.composables.appbar.LayeredTopAppBar
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.button.Variant
import com.example.domes.presentation.common.composables.card.ExpandableCard
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.settings.composables.SettingsListItem
import com.example.domes.presentation.routes.settings.routes.appearance.AppearanceScreen
import com.example.domes.presentation.routes.settings.routes.integrations.IntegrationsScreen
import com.example.domes.ui.theme.DomesTheme
import com.maxkeppeker.sheets.core.models.base.IconSource
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

enum class Category {
    SYSTEM,
    ABOUT
}

@Parcelize
data class Setting(
    val title: String,
    val icon: @RawValue IconSource,
    val description: String? = null,
    val route: String,
    val screen: @Composable (parentSetting: ParentSetting?) -> Unit = {},
    val category: Category = Category.SYSTEM,
    val enabled: Boolean = true,
    val parent: Setting? = null,
) : Parcelable

data class ParentSetting @OptIn(ExperimentalMaterial3AdaptiveApi::class) constructor(
    val navigator: ThreePaneScaffoldNavigator<Setting>?,
    val setting: Setting,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settings: List<Setting> = emptyList(),
    history: List<Setting?> = emptyList(),
) {
    val navigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(
            currentWindowAdaptiveInfo()
        ),
        initialDestinationHistory = history.map {
            ThreePaneScaffoldDestinationItem(
                pane = if (it == null) ListDetailPaneScaffoldRole.List else ListDetailPaneScaffoldRole.Detail,
                content = it,
            )
        }
    )

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val selectedSetting = navigator.currentDestination?.content

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val breadCrumbs = mutableListOf<@Composable () -> Unit>()

            generateSequence(selectedSetting) { it.parent }
                .forEach {
                    breadCrumbs.add {
                        ActionSectionChip(
                            icon = it.icon,
                            title = it.title,
                            variant = if (it == selectedSetting) Variant.VISUAL else Variant.DEFAULT
                        )
                    }
                }

            breadCrumbs.add {
                ActionSectionChip(
                    icon = IconSource(Icons.Outlined.Settings),
                    title = if (selectedSetting == null) "Select a setting" else null
                )
            }

            LayeredTopAppBar(
                if (navigator.scaffoldValue.secondary == PaneAdaptedValue.Expanded) "Settings" else (navigator.currentDestination?.content?.title
                    ?: "Settings"),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (navigator.canNavigateBack()) {
                                navigator.navigateBack(
                                    backNavigationBehavior = BackNavigationBehavior.PopUntilCurrentDestinationChange
                                )
                            } else {
                                navController.popBackStack(
                                    route = Screen.Prayers.route,
                                    inclusive = false
                                )
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                bottomActions = breadCrumbs.reversed<@Composable () -> Unit>(

                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            NavigableListDetailPaneScaffold(
                navigator = navigator as ThreePaneScaffoldNavigator<Any>,
                listPane = {
                    AnimatedPane {
                        if (navigator.currentDestination?.content?.parent != null) {
                            navigator.currentDestination?.content?.parent?.screen?.invoke(
                                ParentSetting(
                                    navigator,
                                    navigator.currentDestination?.content?.parent!!
                                )
                            )
                        } else {
                            ListPane(
                                settings = settings,
                                selectedSetting = selectedSetting,
                                onItemClick = { setting ->
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, setting)
                                }
                            )
                        }
                    }
                },
                detailPane = {
                    AnimatedPane {
                        if (navigator.currentDestination?.content != null) {
                            navigator.currentDestination?.content?.screen?.invoke(
                                ParentSetting(
                                    navigator,
                                    navigator.currentDestination?.content!!
                                )
                            )
                        } else {
                            FixedCard(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 6.dp, bottom = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Select a setting")
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ListPane(
    settings: List<Setting> = emptyList(),
    selectedSetting: Setting? = null,
    onItemClick: (Setting) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val groupedSettings = settings.groupBy { it.category }

        groupedSettings.forEach { (_, settings) ->
            item {
                Column {
                    settings.forEachIndexed { index, setting ->
                        val selectedSettingIndex = settings.indexOf(selectedSetting)
                        ExpandableCard(
                            content = {
                                SettingsListItem(
                                    setting.title,
                                    setting.icon,
                                    setting.description,
                                    isEnabled = setting.enabled,
                                    isLastItem = index == settings.size - 1 || setting == selectedSetting || index == selectedSettingIndex - 1,
                                    onClick = { onItemClick(setting) },
                                )
                            },
                            isExpanded = setting == selectedSetting,
                            isBeforeExpandedItem = index == selectedSettingIndex - 1,
                            isAfterExpandedItem = index == selectedSettingIndex + 1,
                            isFirstItem = index == 0,
                            isLastItem = index == settings.size - 1,
                            expand = {}
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    device = "spec:id=reference_tablet,shape=Normal,width=1280,height=800,unit=dp,dpi=240"
)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SettingsPreview() {
    val navController = rememberNavController()
    val settings = listOf(
        Setting(
            "Location",
            IconSource(Icons.Outlined.LocationOn),
            "Auto-update • set manually",
            "location"
        ),
        Setting(
            "Calculation",
            IconSource(Icons.Outlined.Calculate),
            "Method • juristic",
            "calculation"
        ),
        Setting(
            "Notifications",
            IconSource(Icons.Outlined.Notifications),
            "Adhan • prayers • corrections",
            "notifications"
        ),
        Setting(
            "Integrations",
            IconSource(Icons.Outlined.AccountTree),
            "Calendar • Google Nest",
            "integrations",
            {
                IntegrationsScreen(navController)
            }
        ),
        Setting(
            "Widgets",
            IconSource(Icons.Outlined.Widgets),
            "Home screen widgets",
            "widgets",
            enabled = false
        ),
        Setting(
            "Appearance",
            IconSource(Icons.Outlined.Palette),
            "Theme • language • format",
            "appearance",
            {
                AppearanceScreen(navController)
            }
        ),
        Setting(
            "About",
            IconSource(Icons.Outlined.Info),
            "App version • contact",
            "about",
            category = Category.ABOUT,
            enabled = false
        ),
        Setting(
            "Privacy",
            IconSource(Icons.Outlined.Security),
            "Policy • terms",
            "privacy",
            category = Category.ABOUT,
            enabled = false
        ),
        Setting(
            "Feedback",
            IconSource(Icons.Outlined.QuestionAnswer),
            "Report a bug • suggest a feature",
            "feedback",
            category = Category.ABOUT,
            enabled = false
        ),
    )

    DomesTheme {
        SettingsScreen(navController, settings)
    }
}