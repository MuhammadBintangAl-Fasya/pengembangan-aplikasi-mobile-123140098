package com.bintang.myprofileapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bintang.myprofileapp.model.sampleProfile
import com.bintang.myprofileapp.ui.components.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val profile = sampleProfile
    var isEditMode by remember { mutableStateOf(false) }
    var showBio by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.Check
                            else Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {

            ProfileHeader(profile = profile)

            Spacer(Modifier.height(8.dp))


            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showBio = !showBio },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("About Me", fontWeight = FontWeight.Bold,
                            color = Color(0xFF0A0E1A))
                        Icon(
                            imageVector = if (showBio) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }

                    AnimatedVisibility(
                        visible = showBio,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = profile.bio,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Contact Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A0E1A),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Divider()

                    InfoItem(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = profile.email,
                        iconTint = Color(0xFF7C3AED)
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    InfoItem(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = profile.phone,
                        iconTint = Color(0xFF0D9488)
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    InfoItem(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = profile.location,
                        iconTint = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialButton(
                    label = "GitHub",
                    icon = Icons.Default.Code,
                    onClick = { /* buka github */ },
                    color = Color(0xFF1E2030),
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    label = "Email Me",
                    icon = Icons.Default.Send,
                    onClick = { /* kirim email */ },
                    color = Color(0xFF7C3AED),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

