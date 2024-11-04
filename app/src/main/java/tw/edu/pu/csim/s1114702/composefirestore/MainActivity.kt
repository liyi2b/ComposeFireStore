package tw.edu.pu.csim.s1114702.composefirestore

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



