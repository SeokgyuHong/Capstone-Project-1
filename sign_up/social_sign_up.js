//프로젝트 실사용 social 로그인
const {log_info} = require('../secret_info/db_loginfo.js');
const {insert} = require('../db_sql.js');
const {pool} = require('../db_connect');
const info = require('../secret_info/db_loginfo')
const {send_mail} = require('../send_mail');
const format_check = require('../format_check');
const nodemailer = require('nodemailer');
const crypto = require('crypto');




module.exports.social_sign_up = (req,res)=>{
    let email_address = req.body.email_address //이메일주소
    let inst_name = req.body.inst_name //기관이름
    let inst_address = req.body.inst_address //기관정보
    let phone_number = req.body.phone_number //휴대폰번호
    let user_type = req.body.user_type //소셜 계정 타입


    if(!format_check.e_mail_check(email_address))
    {
        res.send({'key':1})
        return;
    }
    pool.getConnection().then((conn)=>{
        conn.query(`insert into User (email_address, inst_name, inst_address, phone_number, user_type)
        values('${email_address}','${inst_name}','${inst_address}','${phone_number}','${user_type}')`).then((data)=>{
            console.log('회원가입 성공')
            res.send({'key':1}) // 회원가입성공
        }).catch((err)=>{
            console.log(err)
            res.send({'key':2,'err_code':err.code})//회원가입실패
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code)
        res.send({'key':0,'err_code':err.code}) //시스템에러
    })
}