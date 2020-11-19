var express = require('express');
const getConnection = require('./db.js');
const bodyParser = require("body-parser");
var app = express();
app.use(bodyParser.json());


var fall_alarm = require('./fall_alarm.js');
var sensor_data = require('./sensor_data.js');


app.get('/test', function(){
    console.log('run!');
});

// 낙상 알림
app.post('/fall_alarm',function(req,res){
    fall_alarm.fall_alarm_process(req, res); // 낙상 알람 발생 시, 낙상 알람 처리 모듈 (db저장, fcm)
});

// 주기적으로 data 전달
app.post('/data_transmission', function(req,res){
    sensor_data.measurement_data_tansmission(req,res);
})