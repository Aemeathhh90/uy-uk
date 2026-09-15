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
    private const val SCREEN_ENTER_MS = 260
    private const val SCREEN_EXIT_MS = 180
    private const val DEEP_ENTER_FRACTION = 8
    private const val DEEP_EXIT_FRACTION = 14
    private const val TAB_ENTER_FRACTION = 18
    private const val TAB_EXIT_FRACTION = 20
    private const val FADE_ENTER_MS = 210
    private const val FADE_EXIT_MS = 140
    private const val MODAL_ENTER_MS = 210
    private const val MODAL_EXIT_MS = 150

    fun <S> AnimatedContentTransitionScope<S>.screenTransition(
        forward: Boolean,
        deepNavigation: Boolean,
    ): ContentTransform {
        val enterFraction = if (deepNavigation) DEEP_ENTER_FRACTION else TAB_ENTER_FRACTION
        val exitFraction = if (deepNavigation) DEEP_EXIT_FRACTION else TAB_EXIT_FRACTION
        val enter = slideInHorizontally(
            animationSpec = tween(SCREEN_ENTER_MS, easing = FastOutSlowInEasing),
        ) { width ->
            if (forward) width / enterFraction else -width / enterFraction
        } + fadeIn(animationSpec = tween(FADE_ENTER_MS, easing = FastOutSlowInEasing))

        val exit = slideOutHorizontally(
            animationSpec = tween(SCREEN_EXIT_MS, easing = LinearOutSlowInEasing),
        ) { width ->
            if (forward) -width / exitFraction else width / exitFraction
        } + fadeOut(animationSpec = tween(FADE_EXIT_MS, easing = LinearOutSlowInEasing))

        return enter togetherWith exit
    }

    val modalEnterTransition: EnterTransition =
        fadeIn(animationSpec = tween(FADE_ENTER_MS, easing = FastOutSlowInEasing)) +
            scaleIn(initialScale = .97f, animationSpec = tween(MODAL_ENTER_MS, easing = FastOutSlowInEasing))

    val modalExitTransition: ExitTransition =
        fadeOut(animationSpec = tween(FADE_EXIT_MS, easing = LinearOutSlowInEasing)) +
            scaleOut(targetScale = .985f, animationSpec = tween(MODAL_EXIT_MS, easing = LinearOutSlowInEasing))
}
