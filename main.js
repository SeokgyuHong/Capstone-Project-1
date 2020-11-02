var express = require('express');
var app = express();
var nodemailer = require('nodemailer');
var fs = require('fs');
var path = require('path');
var bodyparser = require('body-parser');

var {send_mail}= require('./send_mail');
var {send_fcm} = require('./send_fcm');
var {save_login_info,} = require('./login');


var db_sql = require('./db_sql');
app.use(bodyparser.json())
app.use(express.urlencoded({extended:false}))

app.get('/',(req,res)=>{
});
app.post('/nodemailerTest',send_mail);
app.post('/notification',send_fcm);

app.post('/naver',db_sql.sql_insert_naver);
app.post('/kakao',db_sql.sql_insert_kakao);


app.post('/alarm_count_request',db_sql.sql_alarm_count);
app.post('/alarm_data_request',db_sql.sql_alarm_data_request);
app.listen(8080, function() {
    console.log('Example app listening on port 8080!')
  });


