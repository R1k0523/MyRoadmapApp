package ru.boringowl.myroadmapapp.presentation.features.skills

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.boringowl.myroadmapapp.R
import ru.boringowl.myroadmapapp.model.Skill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillView(s: Skill) {
    ElevatedCard(
        Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = s.skillName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = "${stringResource(R.string.popularity)}: ${s.necessity}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}
