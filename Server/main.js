const express = require('express');
const app = express();
const nodemailer = require('nodemailer');
const fs = require('fs');
const path = require('path');
const socketIO = require('socket.io');
const bodyparser = require('body-parser');
const {send_mail}= require('./local_lib/send_mail');

//fcm 정보
const fcm_token_save = require('./fcm/fcm_token_save');

const normal_sign_up = require('./sign_up/normal_sign_up'); // 일반계정 회원가입 모듈
const social_sign_up = require('./sign_up/social_sign_up'); //소셜계정　회원가입 모듈


const normal_sign_in = require('./sign_in/normal_sign_in'); //일반계정 로그인
const social_sign_in = require('./sign_in/social_sign_in'); //소셜계정 로그인

const sign_out = require('./sign_out/sign_out'); //로그아웃


//계정정보체크
const account_deletion = require('./account_information/account_deletion'); //계정삭제
const account_information_check = require('./account_information/account_information_check'); //계정 정보 체크
const account_information_modification = require('./account_information/account_information_modification'); //계정 정보 수정
const password_modification = require('./account_information/password_modification'); //비밀번호 변경


//센서 생성 수정 삭제 리스트 
const sensor_connect = require('./sensor/sensor_connect');
const sensor_modification = require('./sensor/sensor_modification'); 
const sensor_deletion = require('./sensor/sensor_deletion');
const sensor_list = require('./sensor/sensor_list');
//센서 온오프
const sensor_on_off = require('./sensor/sensor_on_off');

//fcm전송
const send_fcm = require('./sensor/send_fcm');
//From 센서 To 클라이언트

app.use(bodyparser.json()) //미들웨어 
app.use(express.urlencoded({extended:false}))


app.engine('html', require('ejs').renderFile);
app.set('view engine', 'html');

app.get('/',(req,res)=>{
  let file_name = __dirname+'/html/daum.html';
  res.render(file_name);
});

//센서등록 
app.post('/sensor_duplication_check',sensor_connect.sensor_duplication_check);
app.post('/sensor_registration',sensor_connect.sensor_registration);

//센서삭제 
app.post('/sensor_deletion',sensor_deletion.sensor_deletion);
//센서 온 오프 
app.post('/sensor_on',sensor_on_off.sensor_on);
app.post('/sensor_off',sensor_on_off.sensor_off);

//센서리스트요청
app.post('/sensor_list_request',sensor_list.sensor_list_request);
//센서수정
app.post('/sensor_modification',sensor_modification.sensor_modification);


app.post('/send_fcm',send_fcm.fall_alarm);


//일반계정 회원가입
app.post('/id_duplication_check',normal_sign_up.id_duplication_check); //아이디 중복여부 체크
app.post('/temp_pw_create',normal_sign_up.temp_pw_create); //임시비밀번호 생성
app.post('/temp_pw_check',normal_sign_up.temp_pw_check); //임시비밀번호 체크
app.post('/normal_sign_up',normal_sign_up.normal_sign_up); //일반계정 회원가입

//소셜계정 회원가입

app.post('/social_sign_up',social_sign_up.social_sign_up);
app.post('/social_sign_in',social_sign_in.social_sign_in); //소셜계정 로그인

//일반계정 로그인
app.post('/normal_sign_in',normal_sign_in.normal_sign_in); //일반계정 로그인



//로그아웃
app.post('/sign_out',sign_out.sign_out);

//계정삭제
app.post('/account_deletion',account_deletion.account_deletion);

//계정정보 체크
app.post('/account_information_check',account_information_check.account_information_check);
app.post('/account_information_modification',account_information_modification.account_information_modification);

//비밀번호변경
app.post('/user_type_check',password_modification.user_type_check); //유저타입 체크함수
app.post('/password_modification',password_modification.password_modification);//패스워드변경

app.listen(8080, function() {
    console.log('Example app listening on port 8080!')
  });
