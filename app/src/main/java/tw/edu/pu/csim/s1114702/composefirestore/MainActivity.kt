package tw.edu.pu.csim.s1114702.composefirestore


//import android.R
import android.R.id
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize
import androidx.compose.ui.Alignment

import tw.edu.pu.csim.s1114702.composefirestore.ui.theme.ComposeFireStoreTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.initialize(this)
        setContent {
            ComposeFireStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Birth(m = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun Birth(m: Modifier = Modifier) {
        var userName by remember { mutableStateOf("") }
        var userWeight by remember { mutableStateOf("") }
        var userHeight by remember { mutableStateOf("") }
        var msg by remember { mutableStateOf("") }
        val lightPink = Color(0xFFF8EEDD)
        val db = Firebase.firestore

        fun calculateBMI(weight: Float, height: Float): Float {
            return weight / ((height / 100) * (height / 100))
        }

        fun bmiCategory(bmi: Float): String {
            return when {
                bmi < 18.5 -> "體重過輕"
                bmi in 18.5..24.9 -> "正常範圍"
                bmi in 25.0..29.9 -> "過重"
                bmi in 30.0..34.9 -> "輕度肥胖"
                bmi in 35.0..39.9 -> "中度肥胖"
                else -> "重度肥胖"
            }
        }

        fun getBMIMessage(weight: String, height: String): String {
            val weightFloat = weight.toFloatOrNull()
            val heightFloat = height.toFloatOrNull()

            return if (weightFloat != null && heightFloat != null && weightFloat > 0f && heightFloat > 0f) {
                val bmi = calculateBMI(weightFloat, heightFloat)
                "您的BMI是：${"%.2f".format(bmi)}\n${bmiCategory(bmi)}"
            } else {
                "請輸入有效的體重和身高"
            }
        }

        fun getWater(weight: String): String {
            val weightFloat = weight.toFloatOrNull()
            return if (weightFloat != null && weightFloat > 0f) {
                val waterIntake = weightFloat * 30
                "每日建議飲水量：${"%.2f".format(waterIntake)} 毫升"
            } else {
                "請輸入有效的體重以計算建議飲水量"
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // 固定顶部的部分
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
                    .padding(top = 24.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "檢康無憂",
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp) // 留出顶部标题的空间
            ) {
                item {


                Column(modifier = Modifier.padding(16.dp)) {


                    @OptIn(ExperimentalMaterial3Api::class)
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        modifier = m.fillMaxWidth(),
                        label = { Text("姓名") },
                        placeholder = { Text("請輸入您的姓名") },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = lightPink, // 設置背景顏色
                            //textColor = Color.Black,          // 設置文字顏色
                            focusedIndicatorColor = Color.Black, // 設置聚焦時的指示器顏色
                            unfocusedIndicatorColor = Color.Gray, // 設置未聚焦時的指示器顏色
                            focusedLabelColor = Color.DarkGray
                        )
                    )
                    @OptIn(ExperimentalMaterial3Api::class)
                    TextField(
                        value = userWeight,
                        onValueChange = { userWeight = it },
                        modifier = m.fillMaxWidth(),
                        label = { Text("體重 (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = lightPink, // 設置背景顏色
                            //textColor = Color.Black,          // 設置文字顏色
                            focusedIndicatorColor = Color.Black, // 設置聚焦時的指示器顏色
                            unfocusedIndicatorColor = Color.Gray, // 設置未聚焦時的指示器顏色
                            focusedLabelColor = Color.DarkGray
                        )
                    )
                    @OptIn(ExperimentalMaterial3Api::class)
                    TextField(
                        value = userHeight,
                        onValueChange = { userHeight = it },
                        modifier = m.fillMaxWidth(),
                        label = { Text("身高 (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = lightPink, // 設置背景顏色
                            //textColor = Color.Black,          // 設置文字顏色
                            focusedIndicatorColor = Color.Black, // 設置聚焦時的指示器顏色
                            unfocusedIndicatorColor = Color.Gray, // 設置未聚焦時的指示器顏色
                            focusedLabelColor = Color.DarkGray
                        )
                    )

                    Text(
                        text = "${getBMIMessage(userWeight, userHeight)}\n${getWater(userWeight)}",
                        modifier = m.padding(vertical = 16.dp)
                    )

                    Row(modifier = m.fillMaxWidth()) {
                        Button(onClick = {
                            val weightFloat = userWeight.toFloatOrNull()
                            val heightFloat = userHeight.toFloatOrNull()

                            if (weightFloat != null && heightFloat != null && weightFloat > 0f && heightFloat > 0f) {
                                val bmi = calculateBMI(weightFloat, heightFloat)
                                val category = bmiCategory(bmi)

                                val user = Person(
                                    userName = userName,
                                    userWeight = userWeight,
                                    userHeight = userHeight,
                                    bmi = "%.2f".format(bmi),
                                    bmiCategory = category
                                )

                                db.collection("HealthCheck")
                                    .add(user)
                                    .addOnSuccessListener {
                                        msg = "新增資料成功"
                                    }
                                    .addOnFailureListener { e ->
                                        msg = "新增資料失敗：${e.message}"
                                    }
                            } else {
                                msg = "請輸入有效的體重和身高"
                            }
                        }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )
                            ) {
                            Text("新增資料")
                        }

                        Button(onClick = {
                            db.collection("HealthCheck")
                                .whereEqualTo("userName", userName)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (documents.isEmpty) {
                                        msg = "找不到資料"
                                    } else {
                                        msg = "查詢成功："
                                        for (doc in documents) {
                                            msg += "\n姓名: ${doc.getString("userName")}" +
                                                    "\n體重: ${doc.getString("userWeight")}" +
                                                    "\n身高: ${doc.getString("userHeight")}" +
                                                    "\nBMI: ${doc.getString("bmi")}, ${doc.getString("bmiCategory")}"
                                        }
                                    }
                                }

                                .addOnFailureListener { e ->
                                    msg = "查詢資料失敗：${e.message}"
                                }
                        }, modifier = Modifier.weight(1f)
                            ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )) {
                            Text("查詢資料")
                        }

                        Button(onClick = {
                            db.collection("HealthCheck")
                                .whereEqualTo("userName", userName)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (documents.isEmpty) {
                                        msg = "找不到資料"
                                    } else {
                                        for (doc in documents) {
                                            db.collection("HealthCheck").document(doc.id).delete()
                                                .addOnSuccessListener {
                                                    msg = "刪除成功"
                                                }
                                                .addOnFailureListener { e ->
                                                    msg = "刪除失敗：${e.message}"
                                                }
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    msg = "刪除資料失敗：${e.message}"
                                }
                        }, modifier = Modifier.weight(1f)
                            ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )) {
                            Text("刪除資料")
                        }
                    }

                    Text(text = msg, modifier = m.padding(vertical = 8.dp))
                }
            }

            }



             //添加右下角的圖片
            Image(
                painter = painterResource(id = R.drawable.compose), // 指定圖片資源名稱
                contentDescription = "右下角圖片",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(100.dp)
            )

        }
    }

    data class Person(
        var userName: String = "",
        var userWeight: String = "",
        var userHeight: String = "",
        var bmi: String = "",
        var bmiCategory: String = ""
    )
}














/*package tw.edu.pu.csim.s1114702.composefirestore


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.s1114702.composefirestore.ui.theme.ComposeFireStoreTheme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.Button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeFireStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /*
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                     */
                    Birth(m = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Birth(m: Modifier){
    var userName by remember { mutableStateOf("李怡蓁")}  //預設文字
    var userWeight by remember { mutableStateOf(3800)}
    var userPassword by remember { mutableStateOf("")}
    var msg by remember { mutableStateOf("訊息")}
    val db = Firebase.firestore


    Column {
        TextField(
            value = userName,
            onValueChange = { newText ->
                userName = newText
            },
            modifier = m,

            label = { Text("姓名") },
            placeholder = { Text("請輸入您的姓名") }  //沒輸入值得時候顯示

        )

        TextField(
            value = userWeight.toString(),
            onValueChange = { newText ->
                if (newText == ""){
                    userWeight = 0
                }
                else{userWeight = newText.toInt()}
            },
            label = { Text("出生體重") },
            keyboardOptions = KeyboardOptions
                (keyboardType = KeyboardType.Number)   //彈出數字鍵盤
        )

        TextField(
            value = userPassword,
            onValueChange = { newText ->
                userPassword = newText
            },
            label = { Text("密碼") },
            placeholder = { Text(text = "請輸入您的密碼") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions
                (keyboardType = KeyboardType.Password)
        )


        Text("您輸入的姓名是：$userName\n出生體重為：$userWeight 公克"
                + "\n密碼：$userPassword")

        Row {
            Button(onClick = { val user = Person(userName, userWeight, userPassword)
                db.collection("users")
                    //.add(user)
                    .document(userName)
                    .set(user)     //資料不存在做新增，資料存在做修改
                    .addOnSuccessListener { documentReference ->
                        msg = "新增/異動資料成功"
                    }
                    .addOnFailureListener { e ->
                        msg = "新增/異動資料失敗：" + e.toString()
                    }
            }) {
                Text("新增/修改資料")
            }
            Button(onClick = { db.collection("users")
                //.whereEqualTo("userName", userName)
                //.whereLessThan("userWeight", userWeight)
                .orderBy("userWeight", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        msg = ""
                        for (document in task.result!!) {
                            msg += "文件id：" + document.id + "\n名字：" + document.data["userName"] +
                                    "\n出生體重：" + document.data["userWeight"].toString() + "\n\n"
                        }
                        if (msg == "") {
                            msg = "查無資料"
                        }
                    }
                }
            }) {
                Text("查詢資料")
            }
            Button(onClick = { db.collection("users")
                .document(userName)
                .delete()
                msg = "刪除資料"
            }) {
                Text("刪除資料")
            }
        }
        Text(text = msg)
    }

}


data class Person(
    var userName: String,
    var userWeight: Int,
    var userPassword: String
)



*/