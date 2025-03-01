package com.walletconnect.sample.wallet.ui.routes.composable_routes.connections

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.skydoves.landscapist.glide.GlideImage
import com.walletconnect.sample.common.ui.TopBarActionImage
import com.walletconnect.sample.common.ui.WCTopAppBar
import com.walletconnect.sample.common.ui.findActivity
import com.walletconnect.sample.common.ui.themedColor
import com.walletconnect.sample.wallet.R
import com.walletconnect.sample.wallet.ui.Web3WalletViewModel
import com.walletconnect.sample.wallet.ui.routes.Route

@Composable
fun ConnectionsRoute(navController: NavController, connectionsViewModel: ConnectionsViewModel, web3WalletViewModel: Web3WalletViewModel) {
    val context = LocalContext.current
    val activity = context.findActivity()

    activity?.intent.takeIf { intent -> intent?.action == Intent.ACTION_VIEW && !intent.dataString.isNullOrBlank() }?.let { intent ->
        web3WalletViewModel.pair(intent.dataString.toString())
        intent.data = null
    }
    connectionsViewModel.refreshConnections()
    val connections by connectionsViewModel.connections.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Title(navController)
        Connections(connections) { connectionUI -> navController.navigate("${Route.ConnectionDetails.path}/${connectionUI.id}") }
    }
}

@Composable
fun Title(navController: NavController) {
    WCTopAppBar(
        titleText = "Connections",
        TopBarActionImage(R.drawable.ic_copy) { navController.navigate(Route.PasteUri.path) },
        TopBarActionImage(R.drawable.ic_qr_code) { navController.navigate(Route.ScanUri.path) }
    )
}

@Composable
fun Connections(
    connections: List<ConnectionUI>,
    onClick: (ConnectionUI) -> Unit = {},
) {
    val modifier = Modifier.fillMaxHeight()
    if (connections.isEmpty()) {
        NoConnections(modifier)
    } else {
        ConnectionsLazyColumn(connections, modifier, onClick)
    }
}

@Composable
fun ConnectionsLazyColumn(
    connections: List<ConnectionUI>, modifier: Modifier, onClick: (ConnectionUI) -> Unit = {},
) {
    val shape = RoundedCornerShape(28.dp)
    LazyColumn(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 10.dp)
            .clip(shape = shape)
            .background(
                color = themedColor(
                    darkColor = Color(0xFFE4E4E7).copy(alpha = .06f),
                    lightColor = Color(0xFF505059).copy(.03f)
                )
            )
            .border(
                1.dp,
                color = themedColor(
                    darkColor = Color(0xFFE4E4E7).copy(alpha = .06f),
                    lightColor = Color(0xFF505059).copy(.03f)
                ),
                shape = shape
            )
            .padding(vertical = 1.dp, horizontal = 2.dp),
    ) {
        connections.forEach { connectionUI ->
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Connection(connectionUI, onClick)
            }
        }
    }
}

@Composable
fun Connection(
    connectionUI: ConnectionUI, onClick: (ConnectionUI) -> Unit = {},
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(connectionUI) }) {
        Spacer(modifier = Modifier.width(20.dp))

        val iconModifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = themedColor(darkColor = Color(0xFF191919), lightColor = Color(0xFFE0E0E0))
            )
        if (connectionUI.icon?.isNotBlank() == true) {
            GlideImage(modifier = iconModifier, imageModel = { connectionUI.icon })
        } else {
            Icon(modifier = iconModifier.alpha(.7f), imageVector = ImageVector.vectorResource(id = R.drawable.sad_face), contentDescription = "Sad face")
        }

        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            Text(text = connectionUI.name, style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, color = themedColor(darkColor = 0xFFe3e7e7, lightColor = 0xFF141414)))
            Text(text = connectionUI.uri, style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 13.sp, color = themedColor(darkColor = 0xFF788686, lightColor = 0xFF788686)))
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            tint = themedColor(darkColor = Color(0xFFe1e5e5), lightColor = Color(0xFF111111)),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
            contentDescription = "Forward"
        )
        Spacer(modifier = Modifier.width(20.dp))

    }
}

@Composable
fun NoConnections(modifier: Modifier) {
    val contentColor = Color(if (isSystemInDarkTheme()) 0xFF585F5F else 0xFF9EA9A9)
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            tint = contentColor,
            imageVector = ImageVector.vectorResource(if (isSystemInDarkTheme()) R.drawable.no_connections_icon_dark else R.drawable.no_connections_icon_light), contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Apps you connect with will appear here.", maxLines = 1, color = contentColor, style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp))
        Row() {
            Text(text = "To connect ", color = contentColor, style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp))
            Icon(tint = contentColor, imageVector = ImageVector.vectorResource(id = R.drawable.ic_qr_code), contentDescription = "Scan QRCode Icon", modifier = Modifier.size(24.dp))
            Text(text = " scan or ", color = contentColor, style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp))
            Icon(tint = contentColor, imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy), contentDescription = "Paste Icon", modifier = Modifier.size(24.dp))
            Text(text = " paste the code", color = contentColor, style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp))
        }
        Text(text = "that’s displayed in the app.", maxLines = 1, color = contentColor, style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp))
        Spacer(modifier = Modifier.weight(1f))
    }
}

