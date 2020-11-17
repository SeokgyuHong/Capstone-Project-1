var express = require('express');
const getConnection = require('./db.js');
const bodyParser = require("body-parser");
var app = express();
app.use(bodyParser.json());


var sensor_crud = require('./sensor_CRUD.js');
var sensor_connect = require('./sensor_connect.js');

app.get('/test', function(){
    console.log('run!');
});

// 센서 등록
app.post('/sensor_regi',function(req,res){
    sensor_crud.check_and_sensor_crud(req,res,'regi'); //json으로 "m_addr": , "flag": "regi" 넘기기
});

// 센서 삭제
app.post('/sensor_del',function(req,res){
    sensor_crud.check_and_sensor_crud(req,res,'del'); //json으로 "m_addr": , "flag": "del" 넘기기
});

// 센서 위치 수정
app.post('/sensor_update_location',function(req,res){
    sensor_crud.check_and_sensor_crud(req,res,'modi'); //json으로 "m_addr": , "flag": "modi", "modify" : {"id" : , "new_location": ""....} 넘기기
});

// 센서 연결
app.post('/sensor_connect', function(req,res){
    sensor_connect.check_and_sensor_connect(req,res); //json으로 "m_addr": , "info": {"e_addr": , "location": , "nickname": } 넘기기
})

// 센서 출력
app.post('/sensor_list', function(req,res){
    sensor_crud.check_sensor_list(req,res); // json으로 e_addr 보내기
})

// var flag = true;
// while (flag){
//     flag = false;
//     TIMEOUT(3);
// }
// fcm()

app.listen(3000);