package com.dorabangs.feature.save.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dorabangs.feature.save.DoraSaveState
import com.dorabangs.feature.save.R
import com.mashup.dorabangs.core.designsystem.component.buttons.DoraButtons
import com.mashup.dorabangs.core.designsystem.component.textfield.DoraTextField
import com.mashup.dorabangs.core.designsystem.component.topbar.DoraTopBar
import com.mashup.dorabangs.core.designsystem.theme.LinkSaveColorTokens
import com.mashup.dorabangs.domain.utils.isValidUrl

@Composable
fun DoraLinkSaveScreen(
    state: DoraSaveState,
    onValueChanged: (String) -> Unit,
    onClickSaveButton: () -> Unit,
    onClickBackIcon: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = LinkSaveColorTokens.ContainerColor),
    ) {
        DoraTopBar.BackNavigationTopBar(
            modifier = Modifier,
            title = stringResource(id = R.string.link_save_title_text),
            isTitleCenter = true,
            onClickBackIcon = onClickBackIcon,
            isShowBottomDivider = true,
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            DoraTextField(
                text = state.urlLink,
                hintText = stringResource(id = R.string.link_save_hint_text),
                labelText = stringResource(id = R.string.link_save_label_text),
                helperText = stringResource(id = R.string.link_save_error_text),
                helperEnabled = state.isError,
                counterEnabled = false,
                onValueChanged = onValueChanged,
            )
            Spacer(modifier = Modifier.height(20.dp))
            DoraButtons.DoraBtnMaxFull(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                buttonText = stringResource(R.string.link_save_button_text),
                enabled = state.isError.not() && state.urlLink.isValidUrl(),
                onClickButton = onClickSaveButton,
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
@Preview
fun DoraLinkSaveScreenPreview() {
    DoraLinkSaveScreen(
        onValueChanged = {},
        onClickSaveButton = {},
        onClickBackIcon = {},
        state = DoraSaveState(),
    )
}
