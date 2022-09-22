package com.example.assignment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment.R
import com.example.assignment.R.string
import com.example.repositories.holdings.network.HoldingResponse
import com.example.repositories.utils.Extensions.roundOffDecimal
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun DashboardView(paddingValues: PaddingValues) {
    val dashboardViewModel = viewModel(modelClass = DashboardViewModel::class.java)
    val state by dashboardViewModel.state.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(false)

    val errorState by dashboardViewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SwipeRefresh(modifier = Modifier.fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = { dashboardViewModel.getHoldings() }) {
            if (errorState && state.isEmpty()) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    EmptyView()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minWidth = 200.dp, minHeight = 400.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {

                        LazyColumn(contentPadding = paddingValues) {
                            if (state.isEmpty()) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }

                            items(state) { holdings: HoldingResponse.Data ->
                                holdingsItem(holdings = holdings)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        BottomView(holdings = state)
                    }
                }
            }
        }
    }
}

@Composable
fun holdingsItem(holdings: HoldingResponse.Data) {

    Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(16.dp)) {
        Box {

            Surface(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.align(Alignment.Center),
                contentColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.TopStart),
                            text = holdings.symbol,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        InTextBold(
                            modifier = Modifier.align(Alignment.TopEnd),
                            text = "LTP: **${holdings.ltp.toFloat().roundOffDecimal()} **",
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 0.dp, top = 0.dp, bottom = 5.dp, end = 0.dp),
                            text = "${holdings.quantity}",
                            fontSize = 11.sp,
                            color = Color.Black
                        )

                        val currentValue = holdings.ltp * holdings.quantity
                        val investmentValue = holdings.avgPrice.toFloat() * holdings.quantity
                        val pl = currentValue - investmentValue
                        InTextBold(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(start = 0.dp, top = 5.dp, bottom = 0.dp, end = 0.dp),
                            text = "P/L: **${pl.toFloat().roundOffDecimal()}**",
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                    }

                }
            }
        }
    }
}

val boldRegex = Regex("(?<!\\*)\\*\\*(?!\\*).*?(?<!\\*)\\*\\*(?!\\*)")

@Composable
fun InTextBold(text: String, modifier: Modifier = Modifier, fontSize: TextUnit, color: Color) {

    var results: MatchResult? = boldRegex.find(text)

    val boldIndexes = mutableListOf<Pair<Int, Int>>()

    val keywords = mutableListOf<String>()

    var finalText = text

    while (results != null) {
        keywords.add(results.value)
        results = results.next()
    }

    keywords.forEach { keyword ->
        val indexOf = finalText.indexOf(keyword)
        val newKeyWord = keyword.removeSurrounding("**")
        finalText = finalText.replace(keyword, newKeyWord)
        boldIndexes.add(Pair(indexOf, indexOf + newKeyWord.length))
    }

    val annotatedString = buildAnnotatedString {
        append(finalText)

        // Add bold style to keywords that has to be bold
        boldIndexes.forEach {
            addStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold, fontSize = fontSize

                ), start = it.first, end = it.second
            )

        }
    }

    Text(
        modifier = modifier, fontSize = fontSize, text = annotatedString, color = color
    )
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = LightColors, content = content)
}

private val Purple = Color(0xFFBF40BF)
private val Blue200 = Color(0xff91a4fc)

private val LightColors = darkColors(
    primary = Blue200, secondary = Color.White
)

@Composable
fun TopBar() {
    TopAppBar(title = {
        Text(text = stringResource(id = string.app_name), fontSize = 18.sp)
    }, backgroundColor = Purple, contentColor = Color.White)
}

@Composable
fun BottomView(holdings: List<HoldingResponse.Data>) {

    val totalCurrentValue = holdings.sumOf { it.quantity * it.ltp }
    val totalInvestment = holdings.sumOf { it.avgPrice.toDouble() * it.quantity }
    val todayProfitLoss = holdings.sumOf { (it.close - it.ltp) * it.quantity }
    val totalProfitLoss = totalCurrentValue - totalInvestment


    Column(Modifier.background(Color.White)) {
        AppText(
            title = stringResource(id = string.current_value),
            value = totalCurrentValue.toFloat().roundOffDecimal(),
            height = 48.dp
        )
        AppText(
            title = stringResource(id = string.total_investment),
            value = totalInvestment.toFloat().roundOffDecimal(),
            height = 48.dp
        )
        AppText(
            title = stringResource(id = string.today_profit), value = todayProfitLoss.toFloat().roundOffDecimal(), height = 48.dp
        )
        AppText(title = stringResource(id = string.pnl), value = totalProfitLoss.toFloat().roundOffDecimal(), height = 60.dp)
    }
}

@Composable
fun AppText(title: String, value: String, height: Dp) {
    Row(
        modifier = Modifier.height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = title, modifier = Modifier.align(Alignment.TopStart), fontWeight = FontWeight.Bold, color = Color.Black
            )
            Text(text = value, modifier = Modifier.align(Alignment.TopEnd), color = Color.Black)
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .defaultMinSize(400.dp)
            .background(Color.White)
            .padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.no_data),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            color = Color.Black,
            fontSize = 18.sp
        )
    }
}

/*
@Preview
@Composable
fun PreviewBottomView() {
    BottomView()
}*/
