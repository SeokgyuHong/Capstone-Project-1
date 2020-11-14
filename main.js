const express = require('express');
const app = express();
const nodemailer = require('nodemailer');
const fs = require('fs');
const path = require('path');
const bodyparser = require('body-parser');

const {send_mail}= require('./send_mail');
const {send_fcm} = require('./send_fcm');
//var {save_login_info} = require('./login');


const normal_sign_up = require('./sign_up/normal_sign_up'); // 일반계정 회원가입 모듈
const social_sign_up = require('./sign_up/social_sign_up'); //소셜계정　회원가입 모듈
const db_sql = require('./db_sql');
const db_sql_exam2=require('./db_sql_exam2');

const normal_sign_in = require('./sign_in/normal_sign_in'); //일반계정 로그인
const social_sign_in = require('./sign_in/social_sign_in'); //소셜계정 로그인


//계정정보체크
const account_information_check = require('./account_information/account_information_check');
const account_information_modification = require('./account_information/account_information_modification');
const password_modification = require('./account_information/password_modification');

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



//일반계정 회원가입
app.post('/id_duplication_check',normal_sign_up.id_duplication_check); //아이디 중복여부 체크
app.post('/temp_pw_create',normal_sign_up.temp_pw_create); //임시비밀번호 생성
app.post('/temp_pw_check',normal_sign_up.temp_pw_check); //임시비밀번호 체크
app.post('/normal_sign_up',normal_sign_up.normal_sign_up); //일반계정 회원가입

//소셜계정 회원가입
app.post('/id_duplication_check',normal_sign_up.id_duplication_check); //소설계정 회원가입 
app.post('/social_sign_up',social_sign_up.social_sign_up);


//일반계정 로그인
app.post('/normal_sign_in',normal_sign_in.normal_sign_in); //일반계정 로그인

//소셜계정 로그인
app.post('/social_sign_in',social_sign_in.social_sign_in); //소셜계정 로그인

//계정정보 체크
app.post('/account_information_check',account_information_check.account_information_check);
app.post('/account_information_modification',account_information_modification.account_information_modification);

//비밀번호변경
app.post('/user_type_check',password_modification.user_type_check); //유저타입 체크함수
app.post('/password_modification',password_modification.password_modification);//패스워드변경



app.listen(8080, function() {
    console.log('Example app listening on port 8080!')
  });


