package ru.boringowl.myroadmapapp.presentation.features.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingFlat
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.boringowl.myroadmapapp.model.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteView(
    navController: NavController,
    viewModel: RoutesViewModel,
    r: Route
) {
    val isTrendingIcon = if (r.index() < 3)
        Icons.Rounded.TrendingUp
    else if (r.index() in 3..6)
        Icons.Rounded.TrendingFlat
    else
        Icons.Rounded.TrendingDown
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { navController.navigate("skills/${r.routeId}")
            }
    ) {
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        r.routeName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(0.8f)
                    )
                    Icon(
                        isTrendingIcon,
                        "Состояние",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Вакансии: ${r.vacanciesCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Резюме: ${r.resumesCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    r.routeDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.pickedRoute = r.routeId!!
                            viewModel.routeName = r.routeName
                            viewModel.isDialogOpened = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, end = 4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text("Создать план") }
                    OutlinedButton(
                        onClick = { navController.navigate("books/${r.routeId}") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) { Text("Книги") }
                }
            }
        }
    }
}