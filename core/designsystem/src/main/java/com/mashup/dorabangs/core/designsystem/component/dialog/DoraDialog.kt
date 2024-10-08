package com.mashup.dorabangs.core.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.mashup.dorabangs.core.designsystem.component.buttons.DoraButtons
import com.mashup.dorabangs.core.designsystem.theme.DialogColorTokens
import com.mashup.dorabangs.core.designsystem.theme.DialogRoundTokens
import com.mashup.dorabangs.core.designsystem.theme.DoraTypoTokens

@Composable
fun DoraDialog(
    title: String,
    content: String,
    confirmBtnText: String,
    disMissBtnText: String,
    modifier: Modifier = Modifier,
    isShowDialog: Boolean = false,
    dialogProperties: DialogProperties = DialogProperties(),
    onDisMissRequest: () -> Unit = {},
    onClickConfirmBtn: () -> Unit = {},
) {
    if (isShowDialog) {
        Dialog(
            properties = dialogProperties,
            onDismissRequest = onDisMissRequest,
        ) {
            (LocalView.current.parent as DialogWindowProvider)?.window?.setDimAmount(0.3f)
            Column(
                modifier = modifier
                    .background(
                        color = DialogColorTokens.BackgroundColor,
                        shape = DialogRoundTokens.Radius,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(top = 24.dp),
                        text = title,
                        style = DoraTypoTokens.base2Bold,
                        color = DialogColorTokens.TitleColor,
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = content,
                        style = DoraTypoTokens.caption2Normal,
                        color = DialogColorTokens.ContentColor,
                        textAlign = TextAlign.Center,
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    DoraButtons.DoraMediumDismissBtn(
                        modifier = Modifier.weight(1f),
                        buttonText = disMissBtnText,
                        onClickButton = onDisMissRequest,
                    )
                    DoraButtons.DoraMediumConfirmBtn(
                        modifier = Modifier.weight(1f),
                        buttonText = confirmBtnText,
                        onClickButton = onClickConfirmBtn,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDoraDialog() = DoraDialog(
    title = "Title",
    content = "Text Field Text Field Text Field Text Field Text Field Text Field, Text Field Text Field Text Field",
    confirmBtnText = "버튼",
    disMissBtnText = "버튼",
    isShowDialog = true,
)
