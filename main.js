var express = require('express');
var app = express();
var nodemailer = require('nodemailer');
var fs = require('fs');
var path = require('path');
var bodyparser = require('body-parser');

var {send_mail}= require('./send_mail');
var {send_fcm} = require('./send_fcm');
//var {save_login_info} = require('./login');


var db_sql = require('./db_sql');
var db_sql_exam2=require('./db_sql_exam2');
app.use(bodyparser.json())
app.use(express.urlencoded({extended:false}))

app.get('/',(req,res)=>{
});
app.post('/nodemailerTest',send_mail);
app.post('/notification',send_fcm);

app.post('/naver',db_sql.sql_insert_naver);
app.post('/kakao',db_sql.sql_insert_kakao);

app.post('/inst_req',db_sql_exam2.institution_request); //기관정보요청 함수 과제코드
app.post('/inst_modify',db_sql_exam2.institution_modify);//기관정보수정 함수 과제코드
app.post('/inst_insert',db_sql_exam2.institution_insert);
app.post('/alarm_count_request',db_sql.sql_alarm_count);
app.post('/alarm_data_request',db_sql.sql_alarm_data_request);
app.listen(8080, function() {
    console.log('Example app listening on port 8080!')
  });


