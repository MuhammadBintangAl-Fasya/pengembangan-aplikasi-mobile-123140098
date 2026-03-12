package com.bintang.myprofileapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SocialButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    color: Color = Color(0xFF7C3AED),
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(44.dp),          
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color
        ),
        border = BorderStroke(1.5.dp, color)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 13.sp
        )
    }
}