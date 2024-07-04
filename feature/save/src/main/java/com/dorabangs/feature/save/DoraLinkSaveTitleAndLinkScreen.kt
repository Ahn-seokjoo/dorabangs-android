package com.dorabangs.feature.save

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.dorabangs.core.designsystem.R
import com.mashup.dorabangs.core.designsystem.theme.DoraRoundTokens
import com.mashup.dorabangs.core.designsystem.theme.DoraTypoTokens
import com.mashup.dorabangs.core.designsystem.theme.LinkSaveColorTokens

@Composable
fun DoraLinkSaveTitleAndLinkScreen(
    url: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(all = 20.dp)
            .height(88.dp)
            .clip(DoraRoundTokens.Round12),
    ) {
        // 추후에 coil이나 뭐나,,로 바꿔야함 url 받아서 하거든요ㅕ
        Image(
            modifier = Modifier.size(88.dp),
            painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = "나중에 바꿀 이미지 위치",
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = LinkSaveColorTokens.LinkContainerBackgroundColor)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // TODO(외부 API? 통해서 url로 제목 알아와야함)
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Aespa supernova 제목 데이터가 띄어집니다. 좋다! 2줄 이상이 되면 처리될 예정입니다",
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = DoraTypoTokens.caption3Bold,
                color = LinkSaveColorTokens.TitleTextColor,
            )

            Spacer(
                modifier = Modifier.height(height = 4.dp),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = url,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DoraTypoTokens.SMedium,
                color = LinkSaveColorTokens.LinkTextColor,
            )
        }
    }
}

@Composable
@Preview
fun DoraLinkSaveTitleAndLinkScreenPreview() {
    DoraLinkSaveTitleAndLinkScreen(url = "https://www.naver.com/articale 길면 넌 바보다")
}

@Composable
@Preview
fun DoraLinkSaveTitleAndLinkScreenPreviewShort() {
    DoraLinkSaveTitleAndLinkScreen(url = "https://youtube.com")
}
