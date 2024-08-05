package components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable fun <T> NavIcon(
    title: String,
    icon: ImageVector,
    route: T,
    currentRoute: T,
    updateRoute: (T) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier.clickable(route != currentRoute) { updateRoute(route) }
) {
    Icon(icon, "")
    VerticalSpacer(0.01.dp)
    Text(title)
}