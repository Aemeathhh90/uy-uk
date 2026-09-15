package com.kakaanime.app.ui.motion

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S> KakaAnimatedScreen(
    targetState: S,
    rank: (S) -> Int,
    deepNavigation: (S, S) -> Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (S) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            val forward = rank(targetState) >= rank(initialState)
            with(KakaMotion) {
                screenTransition(
                    forward = forward,
                    deepNavigation = deepNavigation(initialState, targetState),
                )
            }
        },
        label = "KakaAnime screen transition",
    ) { target -> content(target) }
}
