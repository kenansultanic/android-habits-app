package ba.kenan.myhabits.presentation.utils

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "phone",
    showSystemUi = true,
    device = "spec:width=360dp,height=640dp,dpi=480"
)
@Preview(
    name = "pixel6",
    showSystemUi = true,
    device = "id:pixel_6"
)
@Preview(
    name = "tablet",
    showSystemUi = true,
    device = "spec:width=800dp,height=1280dp,dpi=480"
)
@Preview(
    name = "fold",
    showSystemUi = true,
    device = "id:7.6in Foldable"
)
annotation class DevicesPreview
