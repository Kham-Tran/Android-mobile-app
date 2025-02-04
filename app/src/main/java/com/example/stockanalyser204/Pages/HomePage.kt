import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stockanalyser201.Navigator.Detector
import com.example.stockanalyser201.Navigator.List
import com.example.stockanalyser201.Navigator.Porfolio
import com.example.stockanalyser201.Navigator.paperTrading


@Composable
fun HomePage(Nav: NavHostController, context: Context) {

var configuration = LocalConfiguration.current
    Surface(modifier = Modifier.fillMaxSize()) {
        when (configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                HomePage_Landscape(Nav)
            }
            else -> {
                HomePage_Potrail(Nav)
            }
        }
    }

}


@Composable
fun HomePage_Potrail(Nav: NavHostController) {
    var width = LocalConfiguration.current.screenWidthDp.dp
    var height = LocalConfiguration.current.screenHeightDp.dp
    var lowContainerModifier = Modifier
        .padding(5.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
Surface(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier
        .height(height)
        .width(width)) {
//        Box(modifier = Modifier
//            .weight(.5f)
//            .width(width)){
           LazyRow(modifier = Modifier
                .weight(.05f)
                .width(width)) {
                item{
                    Box(modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .clickable {
                            Nav.navigate(List.route)
                        }
                        .then(lowContainerModifier)) {
                        Text("Watching List", modifier = Modifier.padding(3.dp).align(Alignment.Center))
                    }
                    Box(modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .clickable {
                            Nav.navigate(Detector.route)
                        }
                        .then(lowContainerModifier)) {
                        Text("Pump Detector", modifier = Modifier.padding(3.dp).align(Alignment.Center))
                    }
                    Box(modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .clickable {
                            Nav.navigate(Porfolio.route)
                        }
                        .then(lowContainerModifier)) {
                        Text("Porfolio", modifier = Modifier.padding(3.dp).align(Alignment.Center))
                    }

                    Box(modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .clickable {
                            Nav.navigate(paperTrading.route)
                        }
                        .then(lowContainerModifier)) {
                        Text("Paper Trading", modifier = Modifier.padding(3.dp).align(Alignment.Center))
                    }
                }
            }
//        }
        Box(modifier = Modifier
            .weight(.9f)
            .fillMaxWidth()
            .then(lowContainerModifier)) {

        }}}}


@Composable
fun HomePage_Landscape(Nav: NavHostController) {
    var width = LocalConfiguration.current.screenWidthDp.dp
    var height = LocalConfiguration.current.screenHeightDp.dp
    var lowContainerModifier = Modifier
        .padding(5.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
    Row(modifier = Modifier
        .height(height)
        .width(width)) {
        Box(modifier = Modifier
            .weight(.5f)
            .fillMaxHeight()
            .then(lowContainerModifier)){

        }
        Column(modifier = Modifier
            .weight(.5f)
            .fillMaxHeight()) {
            Box(modifier = Modifier
                .weight(.5f)
                .fillMaxWidth()
                .then(lowContainerModifier)) {

            }
            Box(modifier = Modifier
                .weight(.5f)
                .fillMaxWidth()
                .then(lowContainerModifier)) {

            }
        }
        }

    }


@Preview
@Composable
fun HomePagePreview(){
//    HomePage_Potrail()
}