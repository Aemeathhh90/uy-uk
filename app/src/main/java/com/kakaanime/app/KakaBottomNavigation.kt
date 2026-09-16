package com.kakaanime.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class KakaTab { HOME, CALENDAR, SOCIAL, LIBRARY, PROFILE }

@Composable
fun KakaBottomNavigation(selectedTab: KakaTab, onTabSelected: (KakaTab) -> Unit) {
    val items = listOf(
        KakaTab.HOME to ("Home" to Icons.Outlined.Home),
        KakaTab.CALENDAR to ("Calendar" to Icons.Outlined.CalendarMonth),
        KakaTab.SOCIAL to ("Social" to Icons.Outlined.Groups),
        KakaTab.LIBRARY to ("Library" to Icons.Outlined.CollectionsBookmark),
        KakaTab.PROFILE to ("Profile" to Icons.Outlined.Person),
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = .96f),
        tonalElevation = 6.dp,
        shadowElevation = 10.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { (tab, item) ->
                val selected = selectedTab == tab
                val containerColor by animateColorAsState(
                    targetValue = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = .72f)
                    } else {
                        androidx.compose.ui.graphics.Color.Transparent
                    },
                    animationSpec = tween(180),
                    label = "navContainer",
                )
                val contentColor by animateColorAsState(
                    targetValue = if (selected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    animationSpec = tween(180),
                    label = "navContent",
                )

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    shape = RoundedCornerShape(20.dp),
                    color = containerColor,
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = item.second,
                            contentDescription = item.first,
                            tint = contentColor,
                            modifier = Modifier.size(23.dp),
                        )
                        Text(
                            text = item.first,
                            style = MaterialTheme.typography.labelMedium,
                            color = contentColor,
                        )
                    }
                }
            }
        }
    }
}
