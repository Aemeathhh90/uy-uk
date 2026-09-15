package com.kakaanime.app.ui.motion

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween

/** Dantotsu-inspired motion language, built only with Compose animation APIs. */
object KakaMotion {
    private const val SCREEN_ENTER_MS = 280
    private const val SCREEN_EXIT_MS = 190
    private const val FADE_ENTER_MS = 220
    private const val FADE_EXIT_MS = 150
    private const val MODAL_ENTER_MS = 220
    private const val MODAL_EXIT_MS = 160

    fun <S> AnimatedContentTransitionScope<S>.screenTransition(
        forward: Boolean,
        deepNavigation: Boolean,
    ): ContentTransform {
        val direction = if (forward) {
            AnimatedContentTransitionScope.SlideDirection.Start
        } else {
            AnimatedContentTransitionScope.SlideDirection.End
        }
        val enter = if (deepNavigation) {
            slideIntoContainer(direction, animationSpec = tween(SCREEN_ENTER_MS, easing = FastOutSlowInEasing)) +
                fadeIn(animationSpec = tween(FADE_ENTER_MS, easing = FastOutSlowInEasing))
        } else {
            slideInHorizontally(animationSpec = tween(SCREEN_ENTER_MS, easing = FastOutSlowInEasing)) { width ->
                if (forward) width / 12 else -width / 12
            } + fadeIn(animationSpec = tween(FADE_ENTER_MS, easing = FastOutSlowInEasing))
        }
        val exit = if (deepNavigation) {
            slideOutOfContainer(direction, animationSpec = tween(SCREEN_EXIT_MS, easing = LinearOutSlowInEasing)) +
                fadeOut(animationSpec = tween(FADE_EXIT_MS, easing = LinearOutSlowInEasing))
        } else {
            slideOutHorizontally(animationSpec = tween(SCREEN_EXIT_MS, easing = LinearOutSlowInEasing)) { width ->
                if (forward) -width / 12 else width / 12
            } + fadeOut(animationSpec = tween(FADE_EXIT_MS, easing = LinearOutSlowInEasing))
        }
        return enter togetherWith exit
    }

    val modalEnterTransition: EnterTransition =
        fadeIn(animationSpec = tween(FADE_ENTER_MS, easing = FastOutSlowInEasing)) +
            scaleIn(initialScale = .96f, animationSpec = tween(MODAL_ENTER_MS, easing = FastOutSlowInEasing))

    val modalExitTransition: ExitTransition =
        fadeOut(animationSpec = tween(FADE_EXIT_MS, easing = LinearOutSlowInEasing)) +
            scaleOut(targetScale = .98f, animationSpec = tween(MODAL_EXIT_MS, easing = LinearOutSlowInEasing))
}
