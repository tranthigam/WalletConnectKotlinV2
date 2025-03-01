package com.walletconnect.sample.modal.ui

import androidx.compose.ui.graphics.Color
import com.walletconnect.web3.modal.ui.Web3ModalTheme

val predefinedOrangeLightTheme = Web3ModalTheme.provideLightWeb3ModalColors(
    main100 = Color(0xFFFFA500),
    main90 = Color(0xFFC7912F),
    main20 = Color(0xFFA5864E),
    foreground = Web3ModalTheme.provideForegroundLightColorPalette(color100 = Color(0xFFBE7B00))
)
val predefinedOrangeDarkTheme = Web3ModalTheme.provideDarkWeb3ModalColor(
    main100 = Color(0xFFFFA500),
    main90 = Color(0xFFC7912F),
    main20 = Color(0xFFA5864E),
    foreground = Web3ModalTheme.provideForegroundDarkColorPalette(color100 = Color(0xFFFFA500))
)

val predefinedRedLightTheme = Web3ModalTheme.provideLightWeb3ModalColors(
    main100 = Color(0xFFB7342B),
    main90 = Color(0xFFA54740),
    main20 = Color(0xFF94504B),
    background = Web3ModalTheme.provideBackgroundLightColorPalette(color100 = Color(0xFFFFCECA))
)
val predefinedRedDarkTheme = Web3ModalTheme.provideDarkWeb3ModalColor(
    main100 = Color(0xFFB7342B),
    main90 = Color(0xFFA54740),
    main20 = Color(0xFF94504B),
    background = Web3ModalTheme.provideBackgroundDarkColorPalette(color100 = Color(0xFF350400))
)

val predefinedGreenLightTheme = Web3ModalTheme.provideLightWeb3ModalColors(
    main100 = Color(0xFF10B124),
    main90 = Color(0xFF31AD41),
    main20 = Color(0xFF3B7242),
    overlay = Color(0xFF10B124)
)
val predefinedGreenDarkTheme = Web3ModalTheme.provideDarkWeb3ModalColor(
    main100 = Color(0xFF10B124),
    main90 = Color(0xFF31AD41),
    main20 = Color(0xFF3B7242),
    overlay = Color(0xFF10B124)
)