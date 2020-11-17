//프로젝트 실사용 social 로그인
//const {insert} = require('../db_sql.js');
const {pool} = require('../secret_info/db_connect');
const info = require('../secret_info/db_loginfo')
const {send_mail} = require('../send_mail');
const format_check = require('../format_check');
const nodemailer = require('nodemailer');
const crypto = require('crypto');





module.exports.social_sign_in = (req,res)=>{
    let email_address = req.body.email_address;//이메일주소
    let user_type = req.body.user_type;
    if(!format_check.e_mail_check(email_address))
    {
        console.log('social_sign_up error')
        res.send({'key':1}) //이메일 양식 체크
        return;
    }
    pool.getConnection().then((conn)=>{
        conn.query(`select email_address,user_type from User
        where email_address='${email_address}'`).then((data)=>{
            //console.log('Test용 '+data[0]['email_address']+data[0]['user_type']);
            if(data[0]===undefined) //등록된것이없으면 아무것도없다면
            {
                res.send({'key':2}) //추가정보입력란으로 넘기기
  
            }
            else if(data[0]['email_address']==email_address && data[0]['user_type']!=user_type)//이미 등록된 이메일 
            {
                res.send({'key':3})//회원가입실패 (이메일중복)
            }
            else //로그인성공
            {
                conn.query(`update User set sign_in_status = '1' 
                where email_address='${email_address}'`).then((data)=>{
                    res.send({'key':4})//로그인성공
                }).catch((err)=>{
                    console.log('social sign up id test error');
                    console.log(err.code)
                    res.send({'key':0,'err_code':err.code});
                })

            }
        }).catch((err)=>{
            console.log('social sign up id test error');
            console.log(err.code)
            res.send({'key':0,'err_code':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log('social sign up id test error');
        console.log(err.code)
        res.send({'key':0,'err_code':err.code});
    })
}