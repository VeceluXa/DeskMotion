package com.danilovfa.deskmotion.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes: Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(16.dp),
)

val Shapes.largeIcon: CornerBasedShape
    get() = RoundedCornerShape(24.dp)

val Shapes.largeBanner: CornerBasedShape
    get() = RoundedCornerShape(20.dp)

val Shapes.largeBlock: CornerBasedShape
    get() = RoundedCornerShape(16.dp)

val Shapes.largeShimmer: CornerBasedShape
    get() = RoundedCornerShape(12.dp)

val Shapes.tiny: CornerBasedShape
    get() = RoundedCornerShape(10.dp)

val Shapes.micro: CornerBasedShape
    get() = RoundedCornerShape(4.dp)

val Shapes.textShimmer: CornerBasedShape
    get() = RoundedCornerShape(2.dp)

val Shapes.image: CornerBasedShape
    get() = RoundedCornerShape(8.dp)